package lk.jiat.smartcity360.zone;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object representing a node in the zone tree hierarchy.
 */
public class ZoneNodeDto {

    private Long id;
    private String name;
    private String level;
    private Boolean powered;
    private Double energyUsageKwh;
    private Double baseUsageKwh;
    private Long parentId;
    private List<ZoneNodeDto> children = new ArrayList<>();

    public ZoneNodeDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getPowered() {
        return powered;
    }

    public void setPowered(Boolean powered) {
        this.powered = powered;
    }

    public Double getEnergyUsageKwh() {
        return energyUsageKwh;
    }

    public void setEnergyUsageKwh(Double energyUsageKwh) {
        this.energyUsageKwh = energyUsageKwh;
    }

    public Double getBaseUsageKwh() {
        return baseUsageKwh;
    }

    public void setBaseUsageKwh(Double baseUsageKwh) {
        this.baseUsageKwh = baseUsageKwh;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<ZoneNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<ZoneNodeDto> children) {
        this.children = children;
    }
}
