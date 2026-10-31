package lk.jiat.smartcity360.policy;

/**
 * Terminal expression representing a constant numeric literal.
 */
public class NumberLiteral implements ValueExpression {

    private final double value;

    public NumberLiteral(double value) {
        this.value = value;
    }

    @Override
    public double interpret(PolicyContext ctx) {
        return value;
    }

    public double getValue() {
        return value;
    }
}
