package lk.jiat.smartcity360.complaint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data transfer object representing a complaint and its escalation history.
 */
public class ComplaintDto {

    private Long id;
    private Long residentId;
    private String residentName;
    private String title;
    private String description;
    private String severity;
    private String status;
    private Long resolvedByStaffId;
    private String resolvedByStaffName;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
    private List<ComplaintTraceDto> traces = new ArrayList<>();

    public ComplaintDto() {
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

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public List<ComplaintTraceDto> getTraces() {
        return traces;
    }

    public void setTraces(List<ComplaintTraceDto> traces) {
        this.traces = traces;
    }
}
