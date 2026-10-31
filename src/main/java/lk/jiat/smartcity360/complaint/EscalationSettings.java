package lk.jiat.smartcity360.complaint;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Application-scoped bean holding runtime configuration for the complaint escalation chain sequence.
 */
@ApplicationScoped
public class EscalationSettings {

    private final List<StaffRole> chainOrder = new ArrayList<>(
            List.of(StaffRole.LOCAL_STAFF, StaffRole.SUPERVISOR, StaffRole.CITY_MANAGER)
    );

    public synchronized List<StaffRole> getChainOrder() {
        return Collections.unmodifiableList(chainOrder);
    }

    public synchronized void setChainOrder(List<StaffRole> newOrder) {
        if (newOrder == null || newOrder.isEmpty()) {
            throw new IllegalArgumentException("Chain order cannot be null or empty");
        }
        chainOrder.clear();
        chainOrder.addAll(newOrder);
    }
}
