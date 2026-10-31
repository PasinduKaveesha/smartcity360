package lk.jiat.smartcity360.complaint;

import java.util.ArrayList;
import java.util.List;

/**
 * Domain entity representing a citizen complaint being routed through the escalation chain.
 */
public class CitizenComplaint {

    private Long id;
    private Long residentId;
    private String title;
    private String description;
    private Severity severity;
    private ComplaintStatus status = ComplaintStatus.OPEN;
    private Long resolvedByStaffId;
    private String resolvedByStaffName;
    private final List<EscalationStep> trace = new ArrayList<>();

    public CitizenComplaint() {
    }

    public CitizenComplaint(Long id, String title, String description, Severity severity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.severity = severity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public Long getResolvedByStaffId() {
        return resolvedByStaffId;
    }

    public void setResolvedByStaffId(Long resolvedByStaffId) {
        this.resolvedByStaffId = resolvedByStaffId;
    }

    public String getResolvedByStaffName() {
        return resolvedByStaffName;
    }

    public void setResolvedByStaffName(String resolvedByStaffName) {
        this.resolvedByStaffName = resolvedByStaffName;
    }

    public List<EscalationStep> getTrace() {
        return trace;
    }

    public void addTraceStep(EscalationStep step) {
        step.setStepNumber(trace.size() + 1);
        trace.add(step);
    }
}
