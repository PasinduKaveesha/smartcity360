package lk.jiat.smartcity360.streetlight;

import java.util.Objects;

/**
 * Immutable Flyweight intrinsic state representing shared streetlight hardware specifications.
 */
public final class StreetlightModel {

    private final String modelCode;
    private final String lampType;
    private final int wattage;
    private final int maxLumens;
    private final String dimProfile;
    private final String manufacturer;

    public StreetlightModel(String modelCode, String lampType, int wattage, int maxLumens, String dimProfile, String manufacturer) {
        if (modelCode == null || modelCode.isBlank()) {
            throw new IllegalArgumentException("modelCode cannot be null or blank");
        }
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

    public String getLampType() {
        return lampType;
    }

    public int getWattage() {
        return wattage;
    }

    public int getMaxLumens() {
        return maxLumens;
    }

    public String getDimProfile() {
        return dimProfile;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetlightModel that = (StreetlightModel) o;
        return Objects.equals(modelCode, that.modelCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelCode);
    }
}
