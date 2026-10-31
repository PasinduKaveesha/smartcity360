package lk.jiat.smartcity360.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity representing a municipal smart waste bin.
 */
@Entity
@Table(name = "waste_bin")
public class WasteBinEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bin_code", nullable = false, unique = true, length = 50)
    private String binCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private ZoneEntity zone;

    @Column(name = "location_desc", length = 200)
    private String locationDesc;

    @Column(name = "capacity_litres", nullable = false)
    private Integer capacityLitres;

    @Column(name = "fill_percent", nullable = false)
    private Integer fillPercent;

    @Column(name = "last_collected_at")
    private LocalDateTime lastCollectedAt;

    public WasteBinEntity() {
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

    public ZoneEntity getZone() {
        return zone;
    }

    public void setZone(ZoneEntity zone) {
        this.zone = zone;
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
}
