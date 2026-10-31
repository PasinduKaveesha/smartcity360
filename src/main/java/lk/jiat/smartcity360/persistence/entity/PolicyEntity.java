package lk.jiat.smartcity360.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity representing an automated smart city policy rule.
 */
@Entity
@Table(name = "policy")
public class PolicyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "rule_text", nullable = false, length = 500)
    private String ruleText;

    @Column(name = "action_text", nullable = false, length = 200)
    private String actionText;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PolicyEntity() {
    }

    public PolicyEntity(String name, String ruleText, String actionText, Boolean enabled, LocalDateTime updatedAt) {
        this.name = name;
        this.ruleText = ruleText;
        this.actionText = actionText;
        this.enabled = enabled;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuleText() {
        return ruleText;
    }

    public void setRuleText(String ruleText) {
        this.ruleText = ruleText;
    }

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
