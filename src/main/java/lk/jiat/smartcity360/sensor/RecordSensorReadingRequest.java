package lk.jiat.smartcity360.sensor;

/**
 * Request payload for recording an IoT sensor reading.
 */
public class RecordSensorReadingRequest {

    private Double value;

    public RecordSensorReadingRequest() {
    }

    public RecordSensorReadingRequest(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
