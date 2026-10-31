package lk.jiat.smartcity360.complaint;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lk.jiat.smartcity360.persistence.dao.ComplaintDao;
import lk.jiat.smartcity360.persistence.dao.ResidentDao;
import lk.jiat.smartcity360.persistence.dao.StaffMemberDao;
import lk.jiat.smartcity360.persistence.entity.ComplaintEntity;
import lk.jiat.smartcity360.persistence.entity.ComplaintTraceEntity;
import lk.jiat.smartcity360.persistence.entity.ResidentEntity;
import lk.jiat.smartcity360.persistence.entity.StaffMemberEntity;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service managing citizen complaint submission, chain-of-responsibility routing, and audit logs.
 */
@Stateless
public class ComplaintService {

    @Inject
    private ComplaintDao complaintDao;

    @Inject
    private ResidentDao residentDao;

    @Inject
    private StaffMemberDao staffMemberDao;

    @Inject
    private EscalationSettings escalationSettings;

    public ComplaintDto submitComplaint(CreateComplaintRequest req) {
        ResidentEntity resident = residentDao.findById(req.getResidentId())
                .orElseThrow(() -> new IllegalArgumentException("Resident not found with id: " + req.getResidentId()));

        Map<StaffRole, StaffMemberEntity> staffMap = loadStaffMap();
        Map<StaffRole, String> staffNames = new EnumMap<>(StaffRole.class);
        staffMap.forEach((role, staff) -> staffNames.put(role, staff.getFullName()));

        List<StaffRole> order = escalationSettings.getChainOrder();
        ComplaintHandler chainHead = EscalationChainAssembler.assemble(order, staffNames);

        Severity severity = Severity.valueOf(req.getSeverity().toUpperCase());
        CitizenComplaint domainComplaint = new CitizenComplaint(null, req.getTitle(), req.getDescription(), severity);
        domainComplaint.setResidentId(resident.getId());

        if (chainHead != null) {
            chainHead.handle(domainComplaint);
        } else {
            domainComplaint.setStatus(ComplaintStatus.UNRESOLVED);
        }

        ComplaintEntity entity = persistComplaint(resident, domainComplaint, staffMap);
        return toDto(entity);
    }

    public List<ComplaintDto> getAllComplaints() {
        return complaintDao.findAllWithTraces().stream().map(this::toDto).toList();
    }

    public ComplaintDto getComplaintById(Long id) {
        ComplaintEntity entity = complaintDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Complaint not found with id: " + id));
        return toDto(entity);
    }

    public EscalationChainDto getChainOrder() {
        List<String> order = escalationSettings.getChainOrder().stream().map(Enum::name).toList();
        return new EscalationChainDto(order);
    }

    public EscalationChainDto updateChainOrder(UpdateChainRequest req) {
        List<StaffRole> newOrder = req.getOrder().stream()
                .map(s -> StaffRole.valueOf(s.toUpperCase()))
                .toList();
        escalationSettings.setChainOrder(newOrder);
        return getChainOrder();
    }

    private Map<StaffRole, StaffMemberEntity> loadStaffMap() {
        Map<StaffRole, StaffMemberEntity> map = new EnumMap<>(StaffRole.class);
        for (StaffRole role : StaffRole.values()) {
            Optional<StaffMemberEntity> s = staffMemberDao.findFirstByRole(role.name());
            s.ifPresent(member -> map.put(role, member));
        }
        return map;
    }

    private ComplaintEntity persistComplaint(ResidentEntity resident, CitizenComplaint domain, Map<StaffRole, StaffMemberEntity> staffMap) {
        ComplaintEntity entity = new ComplaintEntity();
        entity.setResident(resident);
        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        entity.setSeverity(domain.getSeverity().name());
        entity.setStatus(domain.getStatus().name());
        entity.setCreatedAt(LocalDateTime.now());

        if (domain.getStatus() == ComplaintStatus.RESOLVED && !domain.getTrace().isEmpty()) {
            EscalationStep lastStep = domain.getTrace().get(domain.getTrace().size() - 1);
            StaffMemberEntity staff = staffMap.get(lastStep.getRole());
            entity.setResolvedByStaff(staff);
            entity.setResolvedAt(LocalDateTime.now());
        }

        for (EscalationStep step : domain.getTrace()) {
            ComplaintTraceEntity traceEntity = new ComplaintTraceEntity();
            traceEntity.setStepNo(step.getStepNumber());
            traceEntity.setHandlerRole(step.getRole().name());
            traceEntity.setHandlerName(step.getHandlerName());
            traceEntity.setAction(step.getAction());
            traceEntity.setNote(step.getNote());
            traceEntity.setLoggedAt(step.getTime());
            entity.addTrace(traceEntity);
        }

        complaintDao.save(entity);
        return entity;
    }

    private ComplaintDto toDto(ComplaintEntity e) {
        ComplaintDto dto = new ComplaintDto();
        dto.setId(e.getId());
        dto.setResidentId(e.getResident().getId());
        dto.setResidentName(e.getResident().getFullName());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setSeverity(e.getSeverity());
        dto.setStatus(e.getStatus());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setResolvedAt(e.getResolvedAt());

        if (e.getResolvedByStaff() != null) {
            dto.setResolvedByStaffId(e.getResolvedByStaff().getId());
            dto.setResolvedByStaffName(e.getResolvedByStaff().getFullName());
        }

        for (ComplaintTraceEntity t : e.getTraces()) {
            ComplaintTraceDto traceDto = new ComplaintTraceDto();
            traceDto.setId(t.getId());
            traceDto.setStepNo(t.getStepNo());
            traceDto.setHandlerRole(t.getHandlerRole());
            traceDto.setHandlerName(t.getHandlerName());
            traceDto.setAction(t.getAction());
            traceDto.setNote(t.getNote());
            traceDto.setLoggedAt(t.getLoggedAt());
            dto.getTraces().add(traceDto);
        }
        return dto;
    }
}
