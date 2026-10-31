package lk.jiat.smartcity360.energy;

import java.math.BigDecimal;

/**
 * Request payload for submitting a new energy meter reading.
 */
public class SubmitReadingRequest {

    private BigDecimal readingKwh;

    public SubmitReadingRequest() {
    }

    public BigDecimal getReadingKwh() {
        return readingKwh;
    }

    public void setReadingKwh(BigDecimal readingKwh) {
        this.readingKwh = readingKwh;
    }
}
