package lk.jiat.smartcity360.streetlight;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lk.jiat.smartcity360.persistence.dao.StreetlightDao;
import lk.jiat.smartcity360.persistence.dao.StreetlightModelDao;
import lk.jiat.smartcity360.persistence.dao.ZoneDao;
import lk.jiat.smartcity360.persistence.entity.StreetlightEntity;
import lk.jiat.smartcity360.persistence.entity.StreetlightModelEntity;
import lk.jiat.smartcity360.persistence.entity.ZoneEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service orchestrating streetlight inventory, Flyweight model resolution, and memory statistics.
 */
@Stateless
public class StreetlightService {

    @Inject
    private StreetlightDao streetlightDao;

    @Inject
    private StreetlightModelDao streetlightModelDao;

    @Inject
    private ZoneDao zoneDao;

    public List<StreetlightDto> getPaged(Long zoneId, int page, int size) {
        int pageNum = Math.max(1, page);
        int pageSize = (size > 0 && size <= 100) ? size : 20;
        List<StreetlightEntity> entities = streetlightDao.findPaged(zoneId, pageNum, pageSize);

        List<StreetlightDto> dtos = new ArrayList<>();
        for (StreetlightEntity entity : entities) {
            StreetlightModel flyweight = resolveModel(entity.getModel());
            SmartStreetlight domain = new SmartStreetlight(
                    entity.getId(),
                    entity.getLatitude().doubleValue(),
                    entity.getLongitude().doubleValue(),
                    entity.getZone().getId(),
                    flyweight,
                    LampState.valueOf(entity.getState()),
                    entity.getBrightnessPercent()
            );
            dtos.add(toDto(entity, domain));
        }
        return dtos;
    }

    public StreetlightStatsDto getStats() {
        long totalLights = streetlightDao.count();
        ensureRegistryWarmed();
        int uniqueModels = StreetlightModelRegistry.size();
        long requests = StreetlightModelRegistry.getRequestCount();

        long sharedBytes = (uniqueModels * StreetlightConstants.MODEL_MEMORY_BYTES)
                + (totalLights * StreetlightConstants.LIGHT_EXTRINSIC_MEMORY_BYTES);
        long unsharedBytes = totalLights * StreetlightConstants.LIGHT_UNSHARED_MEMORY_BYTES;
        long savedBytes = Math.max(0, unsharedBytes - sharedBytes);
        double savingsPct = unsharedBytes > 0 ? ((double) savedBytes / unsharedBytes) * 100.0 : 0.0;

        StreetlightStatsDto stats = new StreetlightStatsDto();
        stats.setTotalLights(totalLights);
        stats.setUniqueModelsInRegistry(uniqueModels);
        stats.setRegistryRequests(requests);
        stats.setSharedMemoryBytes(sharedBytes);
        stats.setUnsharedMemoryBytes(unsharedBytes);
        stats.setBytesSaved(savedBytes);
        stats.setSavingsPercent(Math.round(savingsPct * 100.0) / 100.0);
        return stats;
    }

    public StreetlightDto create(CreateStreetlightRequest req) {
        StreetlightModelEntity modelEntity = streetlightModelDao.findById(req.getModelCode())
                .orElseThrow(() -> new IllegalArgumentException("Streetlight model not found: " + req.getModelCode()));
        ZoneEntity zone = zoneDao.findById(req.getZoneId())
                .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + req.getZoneId()));

        StreetlightEntity entity = new StreetlightEntity();
        entity.setModel(modelEntity);
        entity.setZone(zone);
        entity.setLatitude(req.getLatitude() != null ? req.getLatitude() : BigDecimal.valueOf(6.9271));
        entity.setLongitude(req.getLongitude() != null ? req.getLongitude() : BigDecimal.valueOf(79.8612));
        entity.setState(req.getState() != null ? req.getState().toUpperCase() : LampState.ON.name());
        entity.setBrightnessPercent(req.getBrightnessPercent() != null ? req.getBrightnessPercent() : 100);
        streetlightDao.save(entity);

        StreetlightModel model = resolveModel(modelEntity);
        SmartStreetlight domain = new SmartStreetlight(entity.getId(), entity.getLatitude().doubleValue(),
                entity.getLongitude().doubleValue(), zone.getId(), model,
                LampState.valueOf(entity.getState()), entity.getBrightnessPercent());
        return toDto(entity, domain);
    }

    public int bulkGenerate(int count, Long zoneId) {
        if (count <= 0 || count > StreetlightConstants.MAX_BULK_COUNT) {
            throw new IllegalArgumentException("Count must be between 1 and " + StreetlightConstants.MAX_BULK_COUNT);
        }
        List<StreetlightModelEntity> models = streetlightModelDao.findAll();
        if (models.isEmpty()) {
            throw new IllegalStateException("No streetlight models found in database to clone");
        }
        ZoneEntity zone = zoneId != null ? zoneDao.findById(zoneId).orElse(null) : null;
        if (zone == null) {
            zone = zoneDao.findAll().stream().filter(z -> "STREET".equals(z.getLevel())).findFirst()
                    .orElseThrow(() -> new IllegalStateException("No street zones found for streetlight placement"));
        }

        List<StreetlightEntity> batch = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StreetlightModelEntity m = models.get(i % models.size());
            StreetlightEntity e = new StreetlightEntity();
            e.setModel(m);
            e.setZone(zone);
            e.setLatitude(BigDecimal.valueOf(6.9270 + (i * 0.00005)));
            e.setLongitude(BigDecimal.valueOf(79.8612 + (i * 0.00005)));
            e.setState(LampState.ON.name());
            e.setBrightnessPercent(100);
            batch.add(e);
        }
        streetlightDao.batchInsert(batch, StreetlightConstants.DEFAULT_BATCH_SIZE);
        return count;
    }

    public StreetlightDto updateState(Long id, UpdateLampStateRequest req) {
        StreetlightEntity entity = streetlightDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Streetlight not found: " + id));

        StreetlightModel model = resolveModel(entity.getModel());
        SmartStreetlight domain = new SmartStreetlight(entity.getId(), entity.getLatitude().doubleValue(),
                entity.getLongitude().doubleValue(), entity.getZone().getId(), model,
                LampState.valueOf(entity.getState()), entity.getBrightnessPercent());

        if ("ON".equalsIgnoreCase(req.getState())) {
            domain.turnOn();
        } else if ("OFF".equalsIgnoreCase(req.getState())) {
            domain.turnOff();
        } else if (req.getBrightnessPercent() != null) {
            domain.dim(req.getBrightnessPercent());
        }

        entity.setState(domain.getState().name());
        entity.setBrightnessPercent(domain.getBrightnessPercent());
        streetlightDao.update(entity);
        return toDto(entity, domain);
    }

    private StreetlightModel resolveModel(StreetlightModelEntity e) {
        return StreetlightModelRegistry.getModel(
                e.getModelCode(),
                e.getLampType(),
                e.getWattage(),
                e.getMaxLumens(),
                e.getDimProfile(),
                e.getManufacturer()
        );
    }

    private void ensureRegistryWarmed() {
        if (StreetlightModelRegistry.size() == 0) {
            for (StreetlightModelEntity m : streetlightModelDao.findAll()) {
                resolveModel(m);
            }
        }
    }

    private StreetlightDto toDto(StreetlightEntity entity, SmartStreetlight domain) {
        StreetlightDto dto = new StreetlightDto();
        dto.setId(entity.getId());
        dto.setModelCode(entity.getModel().getModelCode());
        dto.setLampType(entity.getModel().getLampType());
        dto.setWattage(entity.getModel().getWattage());
        dto.setMaxLumens(entity.getModel().getMaxLumens());
        dto.setZoneId(entity.getZone().getId());
        dto.setZoneName(entity.getZone().getName());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setState(domain.getState().name());
        dto.setBrightnessPercent(domain.getBrightnessPercent());
        dto.setDescription(domain.describe());
        return dto;
    }
}
