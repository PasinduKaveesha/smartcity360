package lk.jiat.smartcity360.streetlight;

/**
 * Data transfer object representing a streetlight hardware model.
 */
public class StreetlightModelDto {

    private String modelCode;
    private String lampType;
    private Integer wattage;
    private Integer maxLumens;
    private String dimProfile;
    private String manufacturer;

    public StreetlightModelDto() {
    }

    public StreetlightModelDto(String modelCode, String lampType, Integer wattage, Integer maxLumens, String dimProfile, String manufacturer) {
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
