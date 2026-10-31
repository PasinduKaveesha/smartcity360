package lk.jiat.smartcity360.policy;

/**
 * Comparison operators supported in numeric policy rule evaluations.
 */
public enum ComparisonOperator {
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<="),
    EQ("=="),
    NEQ("!=");

    private final String symbol;

    ComparisonOperator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean apply(double left, double right) {
        return switch (this) {
            case GT -> left > right;
            case LT -> left < right;
            case GTE -> left >= right;
            case LTE -> left <= right;
            case EQ -> Double.compare(left, right) == 0;
            case NEQ -> Double.compare(left, right) != 0;
        };
    }

    public static ComparisonOperator fromSymbol(String symbol) {
        for (ComparisonOperator op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown comparison operator symbol: " + symbol);
    }
}
