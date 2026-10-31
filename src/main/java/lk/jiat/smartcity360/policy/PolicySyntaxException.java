package lk.jiat.smartcity360.policy;

/**
 * Exception thrown when a policy rule violates grammar specifications, reporting exact token position.
 */
public class PolicySyntaxException extends RuntimeException {

    private final int position;

    public PolicySyntaxException(String message, int position) {
        super(String.format("Syntax error at position %d: %s", position, message));
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
