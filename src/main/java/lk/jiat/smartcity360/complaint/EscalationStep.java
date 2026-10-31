package lk.jiat.smartcity360.complaint;

import java.time.LocalDateTime;

/**
 * Extrinsic record of a single step taken during complaint chain-of-responsibility processing.
 */
public class EscalationStep {

    private int stepNumber;
    private StaffRole role;
    private String handlerName;
    private String action;
    private String note;
    private LocalDateTime time;

    public EscalationStep() {
    }

    public EscalationStep(int stepNumber, StaffRole role, String handlerName, String action, String note, LocalDateTime time) {
        this.stepNumber = stepNumber;
        this.role = role;
        this.handlerName = handlerName;
        this.action = action;
        this.note = note;
        this.time = time;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
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

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
