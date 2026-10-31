package lk.jiat.smartcity360.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity representing an audit step in the complaint escalation chain.
 */
@Entity
@Table(name = "complaint_trace")
public class ComplaintTraceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id", nullable = false)
    private ComplaintEntity complaint;

    @Column(name = "step_no", nullable = false)
    private Integer stepNo;

    @Column(name = "handler_role", nullable = false, length = 20)
    private String handlerRole;

    @Column(name = "handler_name", nullable = false, length = 100)
    private String handlerName;

    @Column(name = "action", nullable = false, length = 20)
    private String action;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "logged_at", nullable = false)
    private LocalDateTime loggedAt;

    public ComplaintTraceEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComplaintEntity getComplaint() {
        return complaint;
    }

    public void setComplaint(ComplaintEntity complaint) {
        this.complaint = complaint;
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
