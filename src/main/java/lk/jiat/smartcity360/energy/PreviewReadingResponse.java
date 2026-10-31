package lk.jiat.smartcity360.energy;

/**
 * Response payload containing packet size preview metrics and layer description.
 */
public class PreviewReadingResponse {

    private int rawSize;
    private int securedSize;
    private String description;
    private boolean roundTripMatches;

    public PreviewReadingResponse() {
    }

    public PreviewReadingResponse(int rawSize, int securedSize, String description, boolean roundTripMatches) {
        this.rawSize = rawSize;
        this.securedSize = securedSize;
        this.description = description;
        this.roundTripMatches = roundTripMatches;
    }

    public int getRawSize() {
        return rawSize;
    }

    public void setRawSize(int rawSize) {
        this.rawSize = rawSize;
    }

    public int getSecuredSize() {
        return securedSize;
    }

    public void setSecuredSize(int securedSize) {
        this.securedSize = securedSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRoundTripMatches() {
        return roundTripMatches;
    }

    public void setRoundTripMatches(boolean roundTripMatches) {
        this.roundTripMatches = roundTripMatches;
    }
}
