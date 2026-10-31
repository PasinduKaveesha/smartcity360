package lk.jiat.smartcity360.policy;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Context container holding dynamic sensor telemetry variables and time of day for policy evaluation.
 */
public class PolicyContext {

    private final Map<String, Double> variables = new HashMap<>();
    private final LocalTime time;

    public PolicyContext(Map<String, Double> variables, LocalTime time) {
        if (variables != null) {
            this.variables.putAll(variables);
        }
        this.time = time != null ? time : LocalTime.now();
    }

    public PolicyContext(LocalTime time) {
        this(Map.of(), time);
    }

    public PolicyContext() {
        this(Map.of(), LocalTime.now());
    }

    public double getVariable(String name) {
        Double value = variables.get(name);
        if (value == null) {
            throw new IllegalArgumentException("Variable '" + name + "' not found in policy evaluation context");
        }
        return value;
    }

    public void setVariable(String name, double value) {
        variables.put(name, value);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    public LocalTime getTime() {
        return time;
    }

    public Map<String, Double> getVariables() {
        return Collections.unmodifiableMap(variables);
    }
}
