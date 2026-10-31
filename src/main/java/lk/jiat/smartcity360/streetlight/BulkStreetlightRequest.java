package lk.jiat.smartcity360.streetlight;

/**
 * Request payload or query specification for bulk generating streetlight fixtures.
 */
public class BulkStreetlightRequest {

    private int count;
    private Long zoneId;

    public BulkStreetlightRequest() {
    }

    public BulkStreetlightRequest(int count, Long zoneId) {
        this.count = count;
        this.zoneId = zoneId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }
}
