package lk.jiat.smartcity360.waste;

/**
 * Request payload for updating the fill percentage level of a waste bin.
 */
public class UpdateFillRequest {

    private Integer fillPercent;

    public UpdateFillRequest() {
    }

    public UpdateFillRequest(Integer fillPercent) {
        this.fillPercent = fillPercent;
    }

    public Integer getFillPercent() {
        return fillPercent;
    }

    public void setFillPercent(Integer fillPercent) {
        this.fillPercent = fillPercent;
    }
}
