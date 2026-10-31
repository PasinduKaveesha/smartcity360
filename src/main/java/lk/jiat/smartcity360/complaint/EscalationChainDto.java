package lk.jiat.smartcity360.complaint;

import java.util.List;

/**
 * Data transfer object representing the active escalation chain handler sequence.
 */
public class EscalationChainDto {

    private List<String> order;

    public EscalationChainDto() {
    }

    public EscalationChainDto(List<String> order) {
        this.order = order;
    }

    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }
}
