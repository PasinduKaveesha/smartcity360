package lk.jiat.smartcity360.policy;

/**
 * Abstract expression interface in the Interpreter pattern evaluating to a boolean result.
 */
public interface PolicyExpression {

    boolean interpret(PolicyContext ctx);
}
