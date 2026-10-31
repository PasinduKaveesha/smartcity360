package lk.jiat.smartcity360.complaint;

/**
 * Enumeration of citizen complaint severity levels with ordinal ranking.
 */
public enum Severity {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4);

    private final int rank;

    Severity(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
