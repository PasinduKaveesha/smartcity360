package lk.jiat.smartcity360.sensor;

/**
 * Constants governing sensor telemetry thresholds and metric units.
 */
public final class SensorConstants {

    public static final String UNIT_DECIBELS = "dB";
    public static final String UNIT_PERCENT = "%";
    public static final String UNIT_CELSIUS = "°C";

    public static final double NOISE_ALERT_THRESHOLD = 70.0;
    public static final double PARKING_FULL_THRESHOLD = 95.0;
    public static final double HEAT_WARNING_THRESHOLD = 35.0;

    private SensorConstants() {
    }
}
