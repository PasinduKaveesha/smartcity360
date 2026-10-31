package lk.jiat.smartcity360.streetlight;

/**
 * Request payload for modifying the operational state or dimming level of a streetlight.
 */
public class UpdateLampStateRequest {

    private String state;
    private Integer brightnessPercent;

    public UpdateLampStateRequest() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getBrightnessPercent() {
        return brightnessPercent;
    }

    public void setBrightnessPercent(Integer brightnessPercent) {
        this.brightnessPercent = brightnessPercent;
    }
}
