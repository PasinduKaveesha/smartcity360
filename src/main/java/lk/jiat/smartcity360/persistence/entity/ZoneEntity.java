package lk.jiat.smartcity360.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a hierarchical city zone (City, District, Street, or Building).
 */
@Entity
@Table(name = "zone")
public class ZoneEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "level", nullable = false, length = 20)
    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ZoneEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<ZoneEntity> children = new ArrayList<>();

    @Column(name = "powered", nullable = false)
    private Boolean powered = true;

    @Column(name = "base_usage_kwh", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseUsageKwh = BigDecimal.ZERO;

    public ZoneEntity() {
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

    public ZoneEntity getParent() {
        return parent;
    }

    public void setParent(ZoneEntity parent) {
        this.parent = parent;
    }

    public List<ZoneEntity> getChildren() {
        return children;
    }

    public void setChildren(List<ZoneEntity> children) {
        this.children = children;
    }

    public Boolean getPowered() {
        return powered;
    }

    public void setPowered(Boolean powered) {
        this.powered = powered;
    }

    public BigDecimal getBaseUsageKwh() {
        return baseUsageKwh;
    }

    public void setBaseUsageKwh(BigDecimal baseUsageKwh) {
        this.baseUsageKwh = baseUsageKwh;
    }
}
