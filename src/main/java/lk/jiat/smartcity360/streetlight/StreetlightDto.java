package lk.jiat.smartcity360.streetlight;

import java.math.BigDecimal;

/**
 * Data transfer object representing a streetlight fixture.
 */
public class StreetlightDto {

    private Long id;
    private String modelCode;
    private String lampType;
    private Integer wattage;
    private Integer maxLumens;
    private Long zoneId;
    private String zoneName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String state;
    private Integer brightnessPercent;
    private String description;

    public StreetlightDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getLampType() {
        return lampType;
    }

    public void setLampType(String lampType) {
        this.lampType = lampType;
    }

    public Integer getWattage() {
        return wattage;
    }

    public void setWattage(Integer wattage) {
        this.wattage = wattage;
    }

    public Integer getMaxLumens() {
        return maxLumens;
    }

    public void setMaxLumens(Integer maxLumens) {
        this.maxLumens = maxLumens;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
