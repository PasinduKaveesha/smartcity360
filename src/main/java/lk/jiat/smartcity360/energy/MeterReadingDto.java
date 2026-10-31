package lk.jiat.smartcity360.energy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data transfer object representing a recorded energy meter reading.
 */
public class MeterReadingDto {

    private Long id;
    private Long meterId;
    private BigDecimal readingKwh;
    private Integer rawSize;
    private Integer securedSize;
    private String layersApplied;
    private LocalDateTime recordedAt;

    public MeterReadingDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
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
