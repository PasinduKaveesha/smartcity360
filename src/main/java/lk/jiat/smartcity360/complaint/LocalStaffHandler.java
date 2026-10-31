package lk.jiat.smartcity360.complaint;

import java.time.LocalDateTime;

/**
 * Concrete handler resolving complaints of LOW severity or escalating higher severities.
 */
public class LocalStaffHandler extends ComplaintHandler {

    public LocalStaffHandler(String handlerName) {
        super(StaffRole.LOCAL_STAFF, handlerName);
    }

    public LocalStaffHandler() {
        this("Local Staff Officer");
    }

    @Override
    public void handle(CitizenComplaint complaint) {
        if (complaint.getSeverity().getRank() <= Severity.LOW.getRank()) {
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
