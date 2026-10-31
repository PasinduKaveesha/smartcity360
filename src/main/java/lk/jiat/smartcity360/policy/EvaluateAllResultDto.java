package lk.jiat.smartcity360.policy;

import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object reporting the aggregate outcome of evaluating all active city policies.
 */
public class EvaluateAllResultDto {

    private int policiesEvaluated;
    private int policiesTriggered;
    private String contextSnapshot;
    private List<PolicyLogDto> results = new ArrayList<>();

    public EvaluateAllResultDto() {
    }

    public int getPoliciesEvaluated() {
        return policiesEvaluated;
    }

    public void setPoliciesEvaluated(int policiesEvaluated) {
        this.policiesEvaluated = policiesEvaluated;
    }

    public int getPoliciesTriggered() {
        return policiesTriggered;
    }

    public void setPoliciesTriggered(int policiesTriggered) {
        this.policiesTriggered = policiesTriggered;
    }

    public String getContextSnapshot() {
        return contextSnapshot;
    }

    public void setContextSnapshot(String contextSnapshot) {
        this.contextSnapshot = contextSnapshot;
    }

    public List<PolicyLogDto> getResults() {
        return results;
    }

    public void setResults(List<PolicyLogDto> results) {
        this.results = results;
    }
}
