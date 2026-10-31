package lk.jiat.smartcity360.resident;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lk.jiat.smartcity360.persistence.dao.ResidentDao;
import lk.jiat.smartcity360.persistence.dao.StaffMemberDao;
import lk.jiat.smartcity360.persistence.dao.ZoneDao;
import lk.jiat.smartcity360.persistence.entity.ResidentEntity;
import lk.jiat.smartcity360.persistence.entity.StaffMemberEntity;
import lk.jiat.smartcity360.persistence.entity.ZoneEntity;
import java.util.List;

/**
 * Service managing citizen resident registrations and municipal staff listings.
 */
@Stateless
public class ResidentService {

    @Inject
    private ResidentDao residentDao;

    @Inject
    private StaffMemberDao staffMemberDao;

    @Inject
    private ZoneDao zoneDao;

    public List<ResidentDto> getAllResidents() {
        return residentDao.findAll().stream().map(this::toResidentDto).toList();
    }

    public ResidentDto createResident(CreateResidentRequest req) {
        ZoneEntity zone = null;
        if (req.getZoneId() != null) {
            zone = zoneDao.findById(req.getZoneId())
                    .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + req.getZoneId()));
        }

        ResidentEntity entity = new ResidentEntity();
        entity.setFullName(req.getFullName());
        entity.setEmail(req.getEmail());
        entity.setAddress(req.getAddress());
        entity.setZone(zone);
        residentDao.save(entity);
        return toResidentDto(entity);
    }

    public List<StaffMemberDto> getAllStaff() {
        return staffMemberDao.findAll().stream().map(this::toStaffDto).toList();
    }

    private ResidentDto toResidentDto(ResidentEntity e) {
        ResidentDto dto = new ResidentDto();
        dto.setId(e.getId());
        dto.setFullName(e.getFullName());
        dto.setEmail(e.getEmail());
        dto.setAddress(e.getAddress());
        if (e.getZone() != null) {
            dto.setZoneId(e.getZone().getId());
            dto.setZoneName(e.getZone().getName());
        }
        return dto;
    }

    private StaffMemberDto toStaffDto(StaffMemberEntity s) {
        return new StaffMemberDto(s.getId(), s.getFullName(), s.getRole(), s.getEmail());
    }
}
