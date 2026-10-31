package lk.jiat.smartcity360.policy;

/**
 * Request payload for validating policy rule syntax without saving.
 */
public class ValidatePolicyRequest {

    private String ruleText;

    public ValidatePolicyRequest() {
    }

    public ValidatePolicyRequest(String ruleText) {
        this.ruleText = ruleText;
    }

    public String getRuleText() {
        return ruleText;
    }

    public void setRuleText(String ruleText) {
        this.ruleText = ruleText;
    }
}
