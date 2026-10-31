package lk.jiat.smartcity360.policy;

/**
 * Execution engine compiling and evaluating policy rules against a dynamic runtime context.
 */
public final class PolicyEngine {

    private PolicyEngine() {
    }

    public static boolean evaluate(String ruleText, PolicyContext ctx) {
        if (ruleText == null || ruleText.isBlank()) {
            throw new IllegalArgumentException("Rule text cannot be null or blank");
        }
        if (ctx == null) {
            throw new IllegalArgumentException("PolicyContext cannot be null");
        }

        PolicyExpression expression = PolicyParser.parse(ruleText);
        return expression.interpret(ctx);
    }
}
