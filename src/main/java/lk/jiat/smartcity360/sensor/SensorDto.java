package lk.jiat.smartcity360.sensor;

/**
 * Data transfer object representing an IoT sensor.
 */
public class SensorDto {

    private Long id;
    private String sensorCode;
    private String sensorType;
    private Long zoneId;
    private String zoneName;
    private Boolean active;
    private Double latestValue;

    public SensorDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Double getLatestValue() {
        return latestValue;
    }

    public void setLatestValue(Double latestValue) {
        this.latestValue = latestValue;
    }
}
