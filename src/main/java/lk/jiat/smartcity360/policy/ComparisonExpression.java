package lk.jiat.smartcity360.policy;

/**
 * Non-terminal expression comparing two numeric value expressions using a comparison operator.
 */
public class ComparisonExpression implements PolicyExpression {

    private final ValueExpression left;
    private final ValueExpression right;
    private final ComparisonOperator operator;

    public ComparisonExpression(ValueExpression left, ComparisonOperator operator, ValueExpression right) {
        if (left == null || right == null || operator == null) {
            throw new IllegalArgumentException("Comparison operands and operator cannot be null");
        }
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public boolean interpret(PolicyContext ctx) {
        double leftVal = left.interpret(ctx);
        double rightVal = right.interpret(ctx);
        return operator.apply(leftVal, rightVal);
    }

    public ValueExpression getLeft() {
        return left;
    }

    public ValueExpression getRight() {
        return right;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }
}
