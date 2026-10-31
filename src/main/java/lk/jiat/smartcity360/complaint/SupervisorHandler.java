package lk.jiat.smartcity360.complaint;

import java.time.LocalDateTime;

/**
 * Concrete handler resolving complaints up to HIGH severity or escalating higher severities.
 */
public class SupervisorHandler extends ComplaintHandler {

    public SupervisorHandler(String handlerName) {
        super(StaffRole.SUPERVISOR, handlerName);
    }

    public SupervisorHandler() {
        this("District Supervisor");
    }

    @Override
    public void handle(CitizenComplaint complaint) {
        if (complaint.getSeverity().getRank() <= Severity.HIGH.getRank()) {
            complaint.setStatus(ComplaintStatus.RESOLVED);
            complaint.setResolvedByStaffName(handlerName);
            complaint.addTraceStep(new EscalationStep(
                    0, role, handlerName, ComplaintConstants.ACTION_HANDLED, ComplaintConstants.NOTE_RESOLVED, LocalDateTime.now()
            ));
        } else {
            passToNext(complaint);
        }
    }
}
