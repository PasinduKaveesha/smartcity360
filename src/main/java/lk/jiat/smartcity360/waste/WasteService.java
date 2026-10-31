package lk.jiat.smartcity360.waste;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lk.jiat.smartcity360.persistence.dao.WasteBinDao;
import lk.jiat.smartcity360.persistence.dao.ZoneDao;
import lk.jiat.smartcity360.persistence.entity.WasteBinEntity;
import lk.jiat.smartcity360.persistence.entity.ZoneEntity;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service orchestrating municipal waste bin telemetry, threshold monitoring, and collection dispatch.
 */
@Stateless
public class WasteService {

    @Inject
    private WasteBinDao wasteBinDao;

    @Inject
    private ZoneDao zoneDao;

    public List<WasteBinDto> getAll() {
        return wasteBinDao.findAll().stream().map(this::toDto).toList();
    }

    public WasteBinDto create(CreateWasteBinRequest req) {
        ZoneEntity zone = zoneDao.findById(req.getZoneId())
                .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + req.getZoneId()));

        WasteBinEntity entity = new WasteBinEntity();
        entity.setBinCode(req.getBinCode());
        entity.setZone(zone);
        entity.setLocationDesc(req.getLocationDesc());
        entity.setCapacityLitres(req.getCapacityLitres());
        entity.setFillPercent(req.getFillPercent() != null ? req.getFillPercent() : 0);
        wasteBinDao.save(entity);
        return toDto(entity);
    }

    public WasteBinDto updateFill(Long id, UpdateFillRequest req) {
        if (req.getFillPercent() < WasteConstants.MIN_FILL_PERCENT || req.getFillPercent() > WasteConstants.MAX_FILL_PERCENT) {
            throw new IllegalArgumentException("Fill percentage must be between 0 and 100");
        }
        WasteBinEntity entity = wasteBinDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Waste bin not found: " + id));

        entity.setFillPercent(req.getFillPercent());
        wasteBinDao.update(entity);
        return toDto(entity);
    }

    public List<WasteBinDto> getDue() {
        return wasteBinDao.findDueForCollection(WasteConstants.THRESHOLD_COLLECTION_PERCENT)
                .stream().map(this::toDto).toList();
    }

    public WasteBinDto collect(Long id) {
        WasteBinEntity entity = wasteBinDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Waste bin not found: " + id));

        entity.setFillPercent(0);
        entity.setLastCollectedAt(LocalDateTime.now());
        wasteBinDao.update(entity);
        return toDto(entity);
    }

    private WasteBinDto toDto(WasteBinEntity e) {
        WasteBinDto dto = new WasteBinDto();
        dto.setId(e.getId());
        dto.setBinCode(e.getBinCode());
        dto.setZoneId(e.getZone().getId());
        dto.setZoneName(e.getZone().getName());
        dto.setLocationDesc(e.getLocationDesc());
        dto.setCapacityLitres(e.getCapacityLitres());
        dto.setFillPercent(e.getFillPercent());
        dto.setLastCollectedAt(e.getLastCollectedAt());
        dto.setDueForCollection(e.getFillPercent() >= WasteConstants.THRESHOLD_COLLECTION_PERCENT);
        return dto;
    }
}
