package lk.jiat.smartcity360.complaint;

/**
 * Constants governing complaint escalation actions and trace messages.
 */
public final class ComplaintConstants {

    public static final String ACTION_HANDLED = "HANDLED";
    public static final String ACTION_ESCALATED = "ESCALATED";
    public static final String ACTION_UNRESOLVED = "UNRESOLVED";

    public static final String NOTE_RESOLVED = "Complaint resolved within role authority";
    public static final String NOTE_ESCALATED = "Severity exceeds role authority limit; escalating to next handler";
    public static final String NOTE_TERMINATED = "End of escalation chain reached; marked unresolved";

    private ComplaintConstants() {
    }
}
