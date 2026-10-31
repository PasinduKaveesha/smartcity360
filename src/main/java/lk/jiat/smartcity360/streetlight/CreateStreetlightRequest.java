package lk.jiat.smartcity360.streetlight;

import java.math.BigDecimal;

/**
 * Request payload for creating an individual streetlight fixture.
 */
public class CreateStreetlightRequest {

    private String modelCode;
    private Long zoneId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String state;
    private Integer brightnessPercent;

    public CreateStreetlightRequest() {
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getBrightnessPercent() {
        return brightnessPercent;
    }

    public void setBrightnessPercent(Integer brightnessPercent) {
        this.brightnessPercent = brightnessPercent;
    }
}
