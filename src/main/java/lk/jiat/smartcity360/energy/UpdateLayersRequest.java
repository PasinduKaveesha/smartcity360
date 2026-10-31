package lk.jiat.smartcity360.energy;

import java.util.List;

/**
 * Request payload for modifying the active stream layers on an energy meter.
 */
public class UpdateLayersRequest {

    private List<String> layers;

    public UpdateLayersRequest() {
    }

    public List<String> getLayers() {
        return layers;
    }

    public void setLayers(List<String> layers) {
        this.layers = layers;
    }
}
