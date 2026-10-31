package lk.jiat.smartcity360.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Entity representing intrinsic Flyweight data for streetlight models.
 */
@Entity
@Table(name = "streetlight_model")
public class StreetlightModelEntity implements Serializable {

    @Id
    @Column(name = "model_code", nullable = false, length = 40)
    private String modelCode;

    @Column(name = "lamp_type", nullable = false, length = 50)
    private String lampType;

    @Column(name = "wattage", nullable = false)
    private Integer wattage;

    @Column(name = "max_lumens", nullable = false)
    private Integer maxLumens;

    @Column(name = "dim_profile", nullable = false, length = 50)
    private String dimProfile;

    @Column(name = "manufacturer", nullable = false, length = 100)
    private String manufacturer;

    public StreetlightModelEntity() {
    }

    public StreetlightModelEntity(String modelCode, String lampType, Integer wattage, Integer maxLumens, String dimProfile, String manufacturer) {
        this.modelCode = modelCode;
        this.lampType = lampType;
        this.wattage = wattage;
        this.maxLumens = maxLumens;
        this.dimProfile = dimProfile;
        this.manufacturer = manufacturer;
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

    public String getDimProfile() {
        return dimProfile;
    }

    public void setDimProfile(String dimProfile) {
        this.dimProfile = dimProfile;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
