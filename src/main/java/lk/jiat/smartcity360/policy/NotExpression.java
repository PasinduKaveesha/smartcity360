package lk.jiat.smartcity360.policy;

/**
 * Non-terminal expression performing a logical NOT inversion on an operand expression.
 */
public class NotExpression implements PolicyExpression {

    private final PolicyExpression operand;

    public NotExpression(PolicyExpression operand) {
        if (operand == null) {
            throw new IllegalArgumentException("NOT operand cannot be null");
        }
        this.operand = operand;
    }

    @Override
    public boolean interpret(PolicyContext ctx) {
        return !operand.interpret(ctx);
    }

    public PolicyExpression getOperand() {
        return operand;
    }
}
