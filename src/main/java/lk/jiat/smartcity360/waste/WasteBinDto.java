package lk.jiat.smartcity360.waste;

import java.time.LocalDateTime;

/**
 * Data transfer object representing a municipal smart waste bin.
 */
public class WasteBinDto {

    private Long id;
    private String binCode;
    private Long zoneId;
    private String zoneName;
    private String locationDesc;
    private Integer capacityLitres;
    private Integer fillPercent;
    private LocalDateTime lastCollectedAt;
    private Boolean dueForCollection;

    public WasteBinDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
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

    public LocalDateTime getLastCollectedAt() {
        return lastCollectedAt;
    }

    public void setLastCollectedAt(LocalDateTime lastCollectedAt) {
        this.lastCollectedAt = lastCollectedAt;
    }

    public Boolean getDueForCollection() {
        return dueForCollection;
    }

    public void setDueForCollection(Boolean dueForCollection) {
        this.dueForCollection = dueForCollection;
    }
}
