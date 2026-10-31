package lk.jiat.smartcity360.complaint;

import java.time.LocalDateTime;

/**
 * Data transfer object representing a trace entry in the complaint audit trail.
 */
public class ComplaintTraceDto {

    private Long id;
    private Integer stepNo;
    private String handlerRole;
    private String handlerName;
    private String action;
    private String note;
    private LocalDateTime loggedAt;

    public ComplaintTraceDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStepNo() {
        return stepNo;
    }

    public void setStepNo(Integer stepNo) {
        this.stepNo = stepNo;
    }

    public String getHandlerRole() {
        return handlerRole;
    }

    public void setHandlerRole(String handlerRole) {
        this.handlerRole = handlerRole;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }

    public void setLoggedAt(LocalDateTime loggedAt) {
        this.loggedAt = loggedAt;
    }
}
