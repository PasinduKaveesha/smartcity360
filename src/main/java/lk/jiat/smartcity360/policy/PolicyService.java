package lk.jiat.smartcity360.policy;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lk.jiat.smartcity360.persistence.dao.PolicyDao;
import lk.jiat.smartcity360.persistence.dao.PolicyLogDao;
import lk.jiat.smartcity360.persistence.dao.SensorReadingDao;
import lk.jiat.smartcity360.persistence.entity.PolicyEntity;
import lk.jiat.smartcity360.persistence.entity.PolicyLogEntity;
import lk.jiat.smartcity360.persistence.entity.SensorReadingEntity;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service managing policy rule lifecycle, syntax validation, and dynamic engine evaluation against sensor feeds.
 */
@Stateless
public class PolicyService {

    @Inject
    private PolicyDao policyDao;

    @Inject
    private PolicyLogDao policyLogDao;

    @Inject
    private SensorReadingDao sensorReadingDao;

    public List<PolicyDto> getAll() {
        return policyDao.findAll().stream().map(this::toDto).toList();
    }

    public PolicyDto create(CreatePolicyRequest req) {
        PolicyParser.parse(req.getRuleText());
        PolicyEntity entity = new PolicyEntity();
        entity.setName(req.getName());
        entity.setRuleText(req.getRuleText());
        entity.setActionText(req.getActionText());
        entity.setEnabled(req.getEnabled() != null ? req.getEnabled() : true);
        entity.setUpdatedAt(LocalDateTime.now());
        policyDao.save(entity);
        return toDto(entity);
    }

    public PolicyDto update(Long id, UpdatePolicyRequest req) {
        PolicyEntity entity = policyDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Policy not found: " + id));

        PolicyParser.parse(req.getRuleText());
        entity.setName(req.getName());
        entity.setRuleText(req.getRuleText());
        entity.setActionText(req.getActionText());
        if (req.getEnabled() != null) {
            entity.setEnabled(req.getEnabled());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        policyDao.update(entity);
        return toDto(entity);
    }

    public void delete(Long id) {
        policyDao.deleteById(id);
    }

    public ValidationResultDto validate(ValidatePolicyRequest req) {
        try {
            PolicyParser.parse(req.getRuleText());
            return new ValidationResultDto(true, "Rule syntax is valid", null);
        } catch (PolicySyntaxException e) {
            return new ValidationResultDto(false, e.getMessage(), e.getPosition());
        }
    }

    public EvaluateAllResultDto evaluateAll() {
        PolicyContext ctx = buildContext();
        String snapshot = buildSnapshot(ctx);
        List<PolicyEntity> enabledPolicies = policyDao.findAllEnabled();

        EvaluateAllResultDto result = new EvaluateAllResultDto();
        result.setPoliciesEvaluated(enabledPolicies.size());
        result.setContextSnapshot(snapshot);

        int triggeredCount = 0;
        for (PolicyEntity p : enabledPolicies) {
            boolean triggered = PolicyEngine.evaluate(p.getRuleText(), ctx);
            if (triggered) {
                triggeredCount++;
            }
            PolicyLogEntity log = new PolicyLogEntity(p, triggered, snapshot, LocalDateTime.now());
            policyLogDao.save(log);
            result.getResults().add(toLogDto(log));
        }

        result.setPoliciesTriggered(triggeredCount);
        return result;
    }

    public List<PolicyLogDto> getRecentLogs(int limit) {
        int count = (limit > 0 && limit <= 200) ? limit : 50;
        return policyLogDao.findRecentLogs(count).stream().map(this::toLogDto).toList();
    }

    public PolicyContext buildContext() {
        Map<String, Double> vars = new HashMap<>();
        vars.put(PolicyConstants.VAR_NOISE_LEVEL, readLatestValue("NOISE", 50.0));
        vars.put(PolicyConstants.VAR_PARKING_OCCUPANCY, readLatestValue("PARKING", 40.0));
        vars.put(PolicyConstants.VAR_TEMPERATURE, readLatestValue("TEMPERATURE", 28.0));
        return new PolicyContext(vars, LocalTime.now());
    }

    private double readLatestValue(String type, double fallback) {
        Optional<SensorReadingEntity> reading = sensorReadingDao.findLatestByType(type);
        return reading.map(SensorReadingEntity::getValue).orElse(fallback);
    }

    private String buildSnapshot(PolicyContext ctx) {
        return String.format("time=%s, noise_level=%.1f, parking_occupancy=%.1f, temperature=%.1f",
                ctx.getTime().withNano(0),
                ctx.getVariable(PolicyConstants.VAR_NOISE_LEVEL),
                ctx.getVariable(PolicyConstants.VAR_PARKING_OCCUPANCY),
                ctx.getVariable(PolicyConstants.VAR_TEMPERATURE));
    }

    private PolicyDto toDto(PolicyEntity e) {
        PolicyDto dto = new PolicyDto();
        dto.setId(e.getId());
        dto.setName(e.getName());
        dto.setRuleText(e.getRuleText());
        dto.setActionText(e.getActionText());
        dto.setEnabled(e.getEnabled());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }

    private PolicyLogDto toLogDto(PolicyLogEntity l) {
        PolicyLogDto dto = new PolicyLogDto();
        dto.setId(l.getId());
        dto.setPolicyId(l.getPolicy().getId());
        dto.setPolicyName(l.getPolicy().getName());
        dto.setActionText(l.getPolicy().getActionText());
        dto.setTriggered(l.getTriggered());
        dto.setContextSnapshot(l.getContextSnapshot());
        dto.setEvaluatedAt(l.getEvaluatedAt());
        return dto;
    }
}
