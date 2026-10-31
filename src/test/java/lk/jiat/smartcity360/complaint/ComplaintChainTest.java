package lk.jiat.smartcity360.complaint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests verifying the Chain of Responsibility pattern implementation for complaint escalation.
 */
class ComplaintChainTest {

    @Test
    @DisplayName("Each severity rating resolves at the appropriate handler authority level")
    void testSeverityRoutingAndTraceLength() {
        ComplaintHandler chain = EscalationChainAssembler.assemble(
                List.of(StaffRole.LOCAL_STAFF, StaffRole.SUPERVISOR, StaffRole.CITY_MANAGER)
        );

        // 1. LOW severity -> handled by Local Staff at step 1
        CitizenComplaint low = new CitizenComplaint(1L, "Pothole", "Minor pothole", Severity.LOW);
        chain.handle(low);
        assertEquals(ComplaintStatus.RESOLVED, low.getStatus());
        assertEquals(1, low.getTrace().size());
        assertEquals(StaffRole.LOCAL_STAFF, low.getTrace().get(0).getRole());
        assertEquals(ComplaintConstants.ACTION_HANDLED, low.getTrace().get(0).getAction());

        // 2. MEDIUM severity -> escalated by Local Staff, handled by Supervisor
        CitizenComplaint med = new CitizenComplaint(2L, "Water pressure", "Low pressure", Severity.MEDIUM);
        chain.handle(med);
        assertEquals(ComplaintStatus.RESOLVED, med.getStatus());
        assertEquals(2, med.getTrace().size());
        assertEquals(ComplaintConstants.ACTION_ESCALATED, med.getTrace().get(0).getAction());
        assertEquals(StaffRole.SUPERVISOR, med.getTrace().get(1).getRole());
        assertEquals(ComplaintConstants.ACTION_HANDLED, med.getTrace().get(1).getAction());

        // 3. HIGH severity -> escalated by Local Staff, handled by Supervisor
        CitizenComplaint high = new CitizenComplaint(3L, "Power line sagging", "Dangerous line", Severity.HIGH);
        chain.handle(high);
        assertEquals(ComplaintStatus.RESOLVED, high.getStatus());
        assertEquals(2, high.getTrace().size());
        assertEquals(StaffRole.SUPERVISOR, high.getTrace().get(1).getRole());

        // 4. CRITICAL severity -> escalated to City Manager
        CitizenComplaint crit = new CitizenComplaint(4L, "Gas leak explosion risk", "Emergency leak", Severity.CRITICAL);
        chain.handle(crit);
        assertEquals(ComplaintStatus.RESOLVED, crit.getStatus());
        assertEquals(3, crit.getTrace().size());
        assertEquals(StaffRole.CITY_MANAGER, crit.getTrace().get(2).getRole());
        assertEquals(ComplaintConstants.ACTION_HANDLED, crit.getTrace().get(2).getAction());
    }

    @Test
    @DisplayName("Chain without a capable handler marks complaint UNRESOLVED and records terminal step")
    void testChainWithoutCapableHandlerEndsUnresolved() {
        // Chain with only LocalStaff (can only resolve LOW)
        ComplaintHandler chain = EscalationChainAssembler.assemble(List.of(StaffRole.LOCAL_STAFF));

        CitizenComplaint high = new CitizenComplaint(5L, "Major water pipe burst", "Flooding street", Severity.HIGH);
        chain.handle(high);

        assertEquals(ComplaintStatus.UNRESOLVED, high.getStatus());
        assertEquals(1, high.getTrace().size());
        assertEquals(ComplaintConstants.ACTION_UNRESOLVED, high.getTrace().get(0).getAction());
    }

    @Test
    @DisplayName("Reordered chain alters resolution routing and handler assignments")
    void testReorderedChainBehavesDifferently() {
        // Inverted chain starting directly with City Manager
        ComplaintHandler invertedChain = EscalationChainAssembler.assemble(
                List.of(StaffRole.CITY_MANAGER, StaffRole.SUPERVISOR, StaffRole.LOCAL_STAFF)
        );

        CitizenComplaint low = new CitizenComplaint(6L, "Litter on road", "Small trash pile", Severity.LOW);
        invertedChain.handle(low);

        // City manager resolves up to CRITICAL, so handles LOW at step 1 immediately
        assertEquals(ComplaintStatus.RESOLVED, low.getStatus());
        assertEquals(1, low.getTrace().size());
        assertEquals(StaffRole.CITY_MANAGER, low.getTrace().get(0).getRole());
        assertEquals(ComplaintConstants.ACTION_HANDLED, low.getTrace().get(0).getAction());
    }
}
