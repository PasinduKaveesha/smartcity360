package lk.jiat.smartcity360.policy;

import java.time.LocalDateTime;

/**
 * Data transfer object representing a logged evaluation execution of a policy.
 */
public class PolicyLogDto {

    private Long id;
    private Long policyId;
    private String policyName;
    private Boolean triggered;
    private String actionText;
    private String contextSnapshot;
    private LocalDateTime evaluatedAt;

    public PolicyLogDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Boolean getTriggered() {
        return triggered;
    }

    public void setTriggered(Boolean triggered) {
        this.triggered = triggered;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public String getContextSnapshot() {
        return contextSnapshot;
    }

    public void setContextSnapshot(String contextSnapshot) {
        this.contextSnapshot = contextSnapshot;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }
}
