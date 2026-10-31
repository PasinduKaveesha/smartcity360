package lk.jiat.smartcity360.complaint;

import java.time.LocalDateTime;

/**
 * Concrete handler resolving complaints up to CRITICAL severity or escalating further.
 */
public class CityManagerHandler extends ComplaintHandler {

    public CityManagerHandler(String handlerName) {
        super(StaffRole.CITY_MANAGER, handlerName);
    }

    public CityManagerHandler() {
        this("City Manager");
    }

    @Override
    public void handle(CitizenComplaint complaint) {
        if (complaint.getSeverity().getRank() <= Severity.CRITICAL.getRank()) {
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
