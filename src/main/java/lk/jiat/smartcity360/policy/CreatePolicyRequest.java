package lk.jiat.smartcity360.policy;

/**
 * Request payload for creating a new automated policy rule.
 */
public class CreatePolicyRequest {

    private String name;
    private String ruleText;
    private String actionText;
    private Boolean enabled = true;

    public CreatePolicyRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuleText() {
        return ruleText;
    }

    public void setRuleText(String ruleText) {
        this.ruleText = ruleText;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
