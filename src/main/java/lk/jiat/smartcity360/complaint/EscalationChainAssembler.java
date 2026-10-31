package lk.jiat.smartcity360.complaint;

import java.util.List;
import java.util.Map;

/**
 * Assembler dynamically linking ComplaintHandler instances according to a specified sequence.
 */
public final class EscalationChainAssembler {

    private EscalationChainAssembler() {
    }

    public static ComplaintHandler assemble(List<StaffRole> order) {
        return assemble(order, Map.of());
    }

    public static ComplaintHandler assemble(List<StaffRole> order, Map<StaffRole, String> staffNames) {
        if (order == null || order.isEmpty()) {
            return null;
        }

        ComplaintHandler head = null;
        ComplaintHandler current = null;

        for (StaffRole role : order) {
            String name = staffNames.getOrDefault(role, role.name());
            ComplaintHandler handler = createHandler(role, name);
            if (head == null) {
                head = handler;
                current = handler;
            } else {
                current.setNext(handler);
                current = handler;
            }
        }
        return head;
    }

    private static ComplaintHandler createHandler(StaffRole role, String name) {
        return switch (role) {
            case LOCAL_STAFF -> new LocalStaffHandler(name);
            case SUPERVISOR -> new SupervisorHandler(name);
            case CITY_MANAGER -> new CityManagerHandler(name);
        };
    }
}
