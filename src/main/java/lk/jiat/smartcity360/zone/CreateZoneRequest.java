package lk.jiat.smartcity360.zone;

/**
 * Request payload for creating a new zone or building.
 */
public class CreateZoneRequest {

    private String name;
    private String level;
    private Long parentId;
    private Double baseUsageKwh;

    public CreateZoneRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Double getBaseUsageKwh() {
        return baseUsageKwh;
    }

    public void setBaseUsageKwh(Double baseUsageKwh) {
        this.baseUsageKwh = baseUsageKwh;
    }
}
