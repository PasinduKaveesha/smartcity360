package lk.jiat.smartcity360.energy;

import java.util.List;

/**
 * Request payload for previewing packet size transformations across different stream layers.
 */
public class PreviewReadingRequest {

    private String reading;
    private List<String> layers;

    public PreviewReadingRequest() {
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public List<String> getLayers() {
        return layers;
    }

    public void setLayers(List<String> layers) {
        this.layers = layers;
    }
}
