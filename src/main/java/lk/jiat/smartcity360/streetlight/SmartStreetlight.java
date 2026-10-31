package lk.jiat.smartcity360.streetlight;

/**
 * Concrete contextual object holding extrinsic streetlight data and referencing a Flyweight model.
 */
public class SmartStreetlight {

    private Long id;
    private double latitude;
    private double longitude;
    private Long zoneId;
    private LampState state = LampState.ON;
    private int brightnessPercent = StreetlightConstants.MAX_BRIGHTNESS;
    private final StreetlightModel model;

    public SmartStreetlight(Long id, double latitude, double longitude, Long zoneId, StreetlightModel model) {
        if (model == null) {
            throw new IllegalArgumentException("StreetlightModel flyweight reference cannot be null");
        }
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoneId = zoneId;
        this.model = model;
    }

    public SmartStreetlight(Long id, double latitude, double longitude, Long zoneId, StreetlightModel model, LampState state, int brightnessPercent) {
        this(id, latitude, longitude, zoneId, model);
        this.state = state;
        this.brightnessPercent = brightnessPercent;
    }

    public void turnOn() {
        this.state = LampState.ON;
        this.brightnessPercent = StreetlightConstants.MAX_BRIGHTNESS;
    }

    public void turnOff() {
        this.state = LampState.OFF;
        this.brightnessPercent = StreetlightConstants.MIN_BRIGHTNESS;
    }

    public void dim(int percent) {
        if (percent < StreetlightConstants.MIN_BRIGHTNESS || percent > StreetlightConstants.MAX_BRIGHTNESS) {
            throw new IllegalArgumentException("Brightness percent must be between 0 and 100");
        }
        this.brightnessPercent = percent;
        if (percent == StreetlightConstants.MIN_BRIGHTNESS) {
            this.state = LampState.OFF;
        } else if (percent == StreetlightConstants.MAX_BRIGHTNESS) {
            this.state = LampState.ON;
        } else {
            this.state = LampState.DIMMED;
        }
    }

    public String describe() {
        return String.format("Streetlight #%d [%s (%dW, %dlm by %s)] at (%.4f, %.4f) in zone %d - State: %s (%d%%)",
                id, model.getModelCode(), model.getWattage(), model.getMaxLumens(), model.getManufacturer(),
                latitude, longitude, zoneId, state, brightnessPercent);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public LampState getState() {
        return state;
    }

    public int getBrightnessPercent() {
        return brightnessPercent;
    }

    public StreetlightModel getModel() {
        return model;
    }
}
