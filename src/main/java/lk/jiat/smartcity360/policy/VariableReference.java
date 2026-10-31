package lk.jiat.smartcity360.policy;

/**
 * Terminal expression resolving a dynamic variable name against the PolicyContext.
 */
public class VariableReference implements ValueExpression {

    private final String variableName;

    public VariableReference(String variableName) {
        if (variableName == null || variableName.isBlank()) {
            throw new IllegalArgumentException("Variable name cannot be null or blank");
        }
        this.variableName = variableName;
    }

    @Override
    public double interpret(PolicyContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("PolicyContext cannot be null");
        }
        return ctx.getVariable(variableName);
    }

    public String getVariableName() {
        return variableName;
    }
}
