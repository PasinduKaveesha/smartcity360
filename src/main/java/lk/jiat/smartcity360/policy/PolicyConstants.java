package lk.jiat.smartcity360.policy;

/**
 * Constants governing policy evaluation variable names and grammar tokens.
 */
public final class PolicyConstants {

    public static final String VAR_NOISE_LEVEL = "noise_level";
    public static final String VAR_PARKING_OCCUPANCY = "parking_occupancy";
    public static final String VAR_TEMPERATURE = "temperature";

    public static final String OP_OR = "OR";
    public static final String OP_AND = "AND";
    public static final String OP_NOT = "NOT";
    public static final String FN_TIME_BETWEEN = "time_between";

    private PolicyConstants() {
    }
}
