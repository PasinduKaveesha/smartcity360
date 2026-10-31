package lk.jiat.smartcity360.energy;

import java.util.List;

/**
 * Request payload for creating a new energy meter.
 */
public class CreateMeterRequest {

    private String meterCode;
    private Long zoneId;
    private List<String> layers;

    public CreateMeterRequest() {
    }

    public String getMeterCode() {
        return meterCode;
    }

    public void setMeterCode(String meterCode) {
        this.meterCode = meterCode;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public List<String> getLayers() {
        return layers;
    }

    public void setLayers(List<String> layers) {
        this.layers = layers;
    }
}
