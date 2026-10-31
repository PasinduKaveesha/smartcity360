package lk.jiat.smartcity360.complaint;

import java.time.LocalDateTime;

/**
 * Abstract handler in the Chain of Responsibility for processing citizen complaints.
 */
public abstract class ComplaintHandler {

    private ComplaintHandler next;
    protected final StaffRole role;
    protected final String handlerName;

    protected ComplaintHandler(StaffRole role, String handlerName) {
        this.role = role;
        this.handlerName = handlerName != null ? handlerName : role.name();
    }

    public ComplaintHandler getNext() {
        return next;
    }

    public void setNext(ComplaintHandler next) {
        this.next = next;
    }

    public abstract void handle(CitizenComplaint complaint);

    protected void passToNext(CitizenComplaint complaint) {
        if (next != null) {
            complaint.addTraceStep(new EscalationStep(
                    0, role, handlerName, ComplaintConstants.ACTION_ESCALATED, ComplaintConstants.NOTE_ESCALATED, LocalDateTime.now()
            ));
            next.handle(complaint);
        } else {
            complaint.setStatus(ComplaintStatus.UNRESOLVED);
            complaint.addTraceStep(new EscalationStep(
                    0, role, handlerName, ComplaintConstants.ACTION_UNRESOLVED, ComplaintConstants.NOTE_TERMINATED, LocalDateTime.now()
            ));
        }
    }
}
