package lk.jiat.smartcity360.policy;

/**
 * Non-terminal expression performing a logical OR between two sub-expressions.
 */
public class OrExpression implements PolicyExpression {

    private final PolicyExpression left;
    private final PolicyExpression right;

    public OrExpression(PolicyExpression left, PolicyExpression right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("OR operands cannot be null");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(PolicyContext ctx) {
        return left.interpret(ctx) || right.interpret(ctx);
    }

    public PolicyExpression getLeft() {
        return left;
    }

    public PolicyExpression getRight() {
        return right;
    }
}
