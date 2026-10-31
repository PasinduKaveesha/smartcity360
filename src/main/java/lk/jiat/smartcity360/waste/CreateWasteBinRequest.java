package lk.jiat.smartcity360.waste;

/**
 * Request payload for creating a new smart waste bin.
 */
public class CreateWasteBinRequest {

    private String binCode;
    private Long zoneId;
    private String locationDesc;
    private Integer capacityLitres;
    private Integer fillPercent;

    public CreateWasteBinRequest() {
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public Integer getCapacityLitres() {
        return capacityLitres;
    }

    public void setCapacityLitres(Integer capacityLitres) {
        this.capacityLitres = capacityLitres;
    }

    public Integer getFillPercent() {
        return fillPercent;
    }

    public void setFillPercent(Integer fillPercent) {
        this.fillPercent = fillPercent;
    }
}
