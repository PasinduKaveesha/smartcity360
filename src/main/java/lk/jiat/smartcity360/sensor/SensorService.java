package lk.jiat.smartcity360.sensor;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lk.jiat.smartcity360.persistence.dao.SensorDao;
import lk.jiat.smartcity360.persistence.dao.SensorReadingDao;
import lk.jiat.smartcity360.persistence.dao.ZoneDao;
import lk.jiat.smartcity360.persistence.entity.SensorEntity;
import lk.jiat.smartcity360.persistence.entity.SensorReadingEntity;
import lk.jiat.smartcity360.persistence.entity.ZoneEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service managing IoT sensors and time-series telemetry data acquisition.
 */
@Stateless
public class SensorService {

    @Inject
    private SensorDao sensorDao;

    @Inject
    private SensorReadingDao sensorReadingDao;

    @Inject
    private ZoneDao zoneDao;

    public List<SensorDto> getAll() {
        List<SensorEntity> sensors = sensorDao.findAll();
        List<SensorDto> dtos = new ArrayList<>();
        for (SensorEntity s : sensors) {
            Double latest = sensorReadingDao.findLatestBySensorId(s.getId())
                    .map(SensorReadingEntity::getValue).orElse(null);
            dtos.add(toDto(s, latest));
        }
        return dtos;
    }

    public SensorDto create(CreateSensorRequest req) {
        ZoneEntity zone = zoneDao.findById(req.getZoneId())
                .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + req.getZoneId()));

        SensorEntity entity = new SensorEntity();
        entity.setSensorCode(req.getSensorCode());
        entity.setSensorType(req.getSensorType().toUpperCase());
        entity.setZone(zone);
        entity.setActive(req.getActive() != null ? req.getActive() : true);
        sensorDao.save(entity);
        return toDto(entity, null);
    }

    public SensorReadingDto recordReading(Long sensorId, RecordSensorReadingRequest req) {
        SensorEntity sensor = sensorDao.findById(sensorId)
                .orElseThrow(() -> new IllegalArgumentException("Sensor not found: " + sensorId));

        SensorReadingEntity reading = new SensorReadingEntity();
        reading.setSensor(sensor);
        reading.setValue(req.getValue());
        reading.setRecordedAt(LocalDateTime.now());
        sensorReadingDao.save(reading);
        return toReadingDto(reading);
    }

    public List<SensorReadingDto> getLatestReadings() {
        List<SensorReadingDto> list = new ArrayList<>();
        for (SensorEntity s : sensorDao.findAll()) {
            sensorReadingDao.findLatestBySensorId(s.getId()).ifPresent(r -> list.add(toReadingDto(r)));
        }
        return list;
    }

    private SensorDto toDto(SensorEntity s, Double latestValue) {
        SensorDto dto = new SensorDto();
        dto.setId(s.getId());
        dto.setSensorCode(s.getSensorCode());
        dto.setSensorType(s.getSensorType());
        dto.setZoneId(s.getZone().getId());
        dto.setZoneName(s.getZone().getName());
        dto.setActive(s.getActive());
        dto.setLatestValue(latestValue);
        return dto;
    }

    private SensorReadingDto toReadingDto(SensorReadingEntity r) {
        SensorReadingDto dto = new SensorReadingDto();
        dto.setId(r.getId());
        dto.setSensorId(r.getSensor().getId());
        dto.setSensorCode(r.getSensor().getSensorCode());
        dto.setValue(r.getValue());
        dto.setRecordedAt(r.getRecordedAt());
        return dto;
    }
}
