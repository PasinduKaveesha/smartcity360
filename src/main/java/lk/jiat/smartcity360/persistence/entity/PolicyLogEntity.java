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
 * Entity representing an audit log entry for an executed policy evaluation.
 */
@Entity
@Table(name = "policy_log")
public class PolicyLogEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private PolicyEntity policy;

    @Column(name = "triggered", nullable = false)
    private Boolean triggered;

    @Column(name = "context_snapshot", nullable = false, length = 500)
    private String contextSnapshot;

    @Column(name = "evaluated_at", nullable = false)
    private LocalDateTime evaluatedAt;

    public PolicyLogEntity() {
    }

    public PolicyLogEntity(PolicyEntity policy, Boolean triggered, String contextSnapshot, LocalDateTime evaluatedAt) {
        this.policy = policy;
        this.triggered = triggered;
        this.contextSnapshot = contextSnapshot;
        this.evaluatedAt = evaluatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PolicyEntity getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyEntity policy) {
        this.policy = policy;
    }

    public Boolean getTriggered() {
        return triggered;
    }

    public void setTriggered(Boolean triggered) {
        this.triggered = triggered;
    }

    public String getContextSnapshot() {
        return contextSnapshot;
    }

    public void setContextSnapshot(String contextSnapshot) {
        this.contextSnapshot = contextSnapshot;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }
}
