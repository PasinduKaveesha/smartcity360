package lk.jiat.smartcity360.sensor;

import java.time.LocalDateTime;

/**
 * Data transfer object representing a recorded sensor reading.
 */
public class SensorReadingDto {

    private Long id;
    private Long sensorId;
    private String sensorCode;
    private Double value;
    private LocalDateTime recordedAt;

    public SensorReadingDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
