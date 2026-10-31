package lk.jiat.smartcity360.energy;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lk.jiat.smartcity360.persistence.dao.EnergyMeterDao;
import lk.jiat.smartcity360.persistence.dao.MeterReadingDao;
import lk.jiat.smartcity360.persistence.dao.ZoneDao;
import lk.jiat.smartcity360.persistence.entity.EnergyMeterEntity;
import lk.jiat.smartcity360.persistence.entity.MeterReadingEntity;
import lk.jiat.smartcity360.persistence.entity.ZoneEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service managing energy meters and decorated stream transmission pipelines.
 */
@Stateless
public class EnergyService {

    @Inject
    private EnergyMeterDao energyMeterDao;

    @Inject
    private MeterReadingDao meterReadingDao;

    @Inject
    private ZoneDao zoneDao;

    public List<EnergyMeterDto> getAllMeters() {
        return energyMeterDao.findAll().stream().map(this::toDto).toList();
    }

    public EnergyMeterDto createMeter(CreateMeterRequest req) {
        ZoneEntity zone = zoneDao.findById(req.getZoneId())
                .orElseThrow(() -> new IllegalArgumentException("Zone not found with ID: " + req.getZoneId()));

        EnergyMeterEntity meter = new EnergyMeterEntity();
        meter.setMeterCode(req.getMeterCode());
        meter.setZone(zone);
        meter.setLayers(req.getLayers() != null ? String.join(",", req.getLayers()) : "");
        meter.setInstalledAt(LocalDateTime.now());
        energyMeterDao.save(meter);
        return toDto(meter);
    }

    public EnergyMeterDto updateLayers(Long meterId, UpdateLayersRequest req) {
        EnergyMeterEntity meter = energyMeterDao.findById(meterId)
                .orElseThrow(() -> new IllegalArgumentException("Meter not found with ID: " + meterId));
        meter.setLayers(req.getLayers() != null ? String.join(",", req.getLayers()) : "");
        energyMeterDao.update(meter);
        return toDto(meter);
    }

    public MeterReadingDto recordReading(Long meterId, SubmitReadingRequest req) {
        EnergyMeterEntity meter = energyMeterDao.findById(meterId)
                .orElseThrow(() -> new IllegalArgumentException("Meter not found with ID: " + meterId));

        String valueStr = req.getReadingKwh().toPlainString();
        List<LayerType> layerTypes = parseLayers(meter.getLayers());
        MeterStream stream = StreamLayerAssembler.assemble(new RawMeterStream(), layerTypes);

        byte[] rawBytes = new RawMeterStream().transmit(valueStr);
        byte[] securedBytes = stream.transmit(valueStr);
        String decoded = stream.receive(securedBytes);

        if (!valueStr.equals(decoded)) {
            throw new IllegalStateException("Round-trip validation failed for meter reading");
        }

        MeterReadingEntity entity = new MeterReadingEntity();
        entity.setMeter(meter);
        entity.setReadingKwh(req.getReadingKwh());
        entity.setRawSize(rawBytes.length);
        entity.setSecuredSize(securedBytes.length);
        entity.setLayersApplied(meter.getLayers());
        entity.setRecordedAt(LocalDateTime.now());
        meterReadingDao.save(entity);

        return toReadingDto(entity);
    }

    public List<MeterReadingDto> getReadings(Long meterId) {
        return meterReadingDao.findByMeterId(meterId).stream().map(this::toReadingDto).toList();
    }

    public PreviewReadingResponse preview(PreviewReadingRequest req) {
        String val = req.getReading() != null ? req.getReading() : "0.0";
        List<LayerType> layerTypes = parseLayers(req.getLayers() != null ? String.join(",", req.getLayers()) : "");
        MeterStream stream = StreamLayerAssembler.assemble(new RawMeterStream(), layerTypes);

        byte[] rawBytes = new RawMeterStream().transmit(val);
        byte[] securedBytes = stream.transmit(val);
        String decoded = stream.receive(securedBytes);

        return new PreviewReadingResponse(
                rawBytes.length,
                securedBytes.length,
                stream.getDescription(),
                val.equals(decoded)
        );
    }

    public List<LayerType> parseLayers(String layersStr) {
        List<LayerType> list = new ArrayList<>();
        if (layersStr == null || layersStr.isBlank()) {
            return list;
        }
        Arrays.stream(layersStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(s -> list.add(LayerType.valueOf(s.toUpperCase())));
        return list;
    }

    private EnergyMeterDto toDto(EnergyMeterEntity e) {
        EnergyMeterDto dto = new EnergyMeterDto();
        dto.setId(e.getId());
        dto.setMeterCode(e.getMeterCode());
        dto.setZoneId(e.getZone().getId());
        dto.setZoneName(e.getZone().getName());
        dto.setLayers(e.getLayers());
        dto.setInstalledAt(e.getInstalledAt());
        return dto;
    }

    private MeterReadingDto toReadingDto(MeterReadingEntity e) {
        MeterReadingDto dto = new MeterReadingDto();
        dto.setId(e.getId());
        dto.setMeterId(e.getMeter().getId());
        dto.setReadingKwh(e.getReadingKwh());
        dto.setRawSize(e.getRawSize());
        dto.setSecuredSize(e.getSecuredSize());
        dto.setLayersApplied(e.getLayersApplied());
        dto.setRecordedAt(e.getRecordedAt());
        return dto;
    }
}
