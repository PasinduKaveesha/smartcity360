package lk.jiat.smartcity360.complaint;

import java.util.List;

/**
 * Request payload for reordering the complaint escalation chain.
 */
public class UpdateChainRequest {

    private List<String> order;

    public UpdateChainRequest() {
    }

    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }
}
