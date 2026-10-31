package lk.jiat.smartcity360.policy;

/**
 * Value expression interface in the Interpreter pattern evaluating to a numeric double result.
 */
public interface ValueExpression {

    double interpret(PolicyContext ctx);
}
