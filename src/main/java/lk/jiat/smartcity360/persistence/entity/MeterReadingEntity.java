package lk.jiat.smartcity360.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing an energy meter reading recorded through decorated stream layers.
 */
@Entity
@Table(name = "meter_reading")
public class MeterReadingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    private EnergyMeterEntity meter;

    @Column(name = "reading_kwh", nullable = false, precision = 12, scale = 3)
    private BigDecimal readingKwh;

    @Column(name = "raw_size", nullable = false)
    private Integer rawSize;

    @Column(name = "secured_size", nullable = false)
    private Integer securedSize;

    @Column(name = "layers_applied", length = 100)
    private String layersApplied;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    public MeterReadingEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnergyMeterEntity getMeter() {
        return meter;
    }

    public void setMeter(EnergyMeterEntity meter) {
        this.meter = meter;
    }

    public BigDecimal getReadingKwh() {
        return readingKwh;
    }

    public void setReadingKwh(BigDecimal readingKwh) {
        this.readingKwh = readingKwh;
    }

    public Integer getRawSize() {
        return rawSize;
    }

    public void setRawSize(Integer rawSize) {
        this.rawSize = rawSize;
    }

    public Integer getSecuredSize() {
        return securedSize;
    }

    public void setSecuredSize(Integer securedSize) {
        this.securedSize = securedSize;
    }

    public String getLayersApplied() {
        return layersApplied;
    }

    public void setLayersApplied(String layersApplied) {
        this.layersApplied = layersApplied;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
