package lk.jiat.smartcity360.sensor;

/**
 * Request payload for creating a new sensor.
 */
public class CreateSensorRequest {

    private String sensorCode;
    private String sensorType;
    private Long zoneId;
    private Boolean active = true;

    public CreateSensorRequest() {
    }

    public String getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(String sensorCode) {
        this.sensorCode = sensorCode;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
