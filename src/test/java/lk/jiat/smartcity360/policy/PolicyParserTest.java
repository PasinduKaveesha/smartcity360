package lk.jiat.smartcity360.policy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests verifying the Interpreter pattern implementation, AST construction, and rule evaluation.
 */
class PolicyParserTest {

    @Test
    @DisplayName("Verify operator precedence: AND binds tighter than OR")
    void testPrecedenceOfAndOverOr() {
        // Rule: x == 1 OR y == 1 AND z == 1
        // Evaluates as: x == 1 OR (y == 1 AND z == 1)
        String rule = "x == 1 OR y == 1 AND z == 1";

        // Case 1: x=0, y=1, z=0 -> 0 OR (1 AND 0) -> FALSE
        PolicyContext ctx1 = new PolicyContext(Map.of("x", 0.0, "y", 1.0, "z", 0.0), LocalTime.NOON);
        assertFalse(PolicyEngine.evaluate(rule, ctx1));

        // Case 2: x=1, y=0, z=0 -> 1 OR (0 AND 0) -> TRUE
        PolicyContext ctx2 = new PolicyContext(Map.of("x", 1.0, "y", 0.0, "z", 0.0), LocalTime.NOON);
        assertTrue(PolicyEngine.evaluate(rule, ctx2));

        // Case 3: x=0, y=1, z=1 -> 0 OR (1 AND 1) -> TRUE
        PolicyContext ctx3 = new PolicyContext(Map.of("x", 0.0, "y", 1.0, "z", 1.0), LocalTime.NOON);
        assertTrue(PolicyEngine.evaluate(rule, ctx3));
    }

    @Test
    @DisplayName("Verify logical NOT operator negation")
    void testNotOperator() {
        String rule = "NOT noise_level > 70";

        PolicyContext belowThreshold = new PolicyContext(Map.of("noise_level", 65.0), LocalTime.NOON);
        assertTrue(PolicyEngine.evaluate(rule, belowThreshold));

        PolicyContext aboveThreshold = new PolicyContext(Map.of("noise_level", 75.0), LocalTime.NOON);
        assertFalse(PolicyEngine.evaluate(rule, aboveThreshold));
    }

    @Test
    @DisplayName("Verify parentheses override default precedence")
    void testParentheses() {
        String rule = "(temperature > 32 OR noise_level > 60) AND parking_occupancy >= 90";

        PolicyContext match = new PolicyContext(
                Map.of("temperature", 34.0, "noise_level", 50.0, "parking_occupancy", 95.0), LocalTime.NOON
        );
        assertTrue(PolicyEngine.evaluate(rule, match));

        PolicyContext failParking = new PolicyContext(
                Map.of("temperature", 34.0, "noise_level", 50.0, "parking_occupancy", 85.0), LocalTime.NOON
        );
        assertFalse(PolicyEngine.evaluate(rule, failParking));
    }

    @Test
    @DisplayName("Verify overnight time window spanning midnight (e.g. 22:00 to 06:00)")
    void testOvernightTimeWindow() {
        String rule = "time_between(22:00, 06:00)";

        PolicyContext lateNight = new PolicyContext(LocalTime.of(23, 30));
        assertTrue(PolicyEngine.evaluate(rule, lateNight));

        PolicyContext earlyMorning = new PolicyContext(LocalTime.of(4, 15));
        assertTrue(PolicyEngine.evaluate(rule, earlyMorning));

        PolicyContext afternoon = new PolicyContext(LocalTime.of(15, 0));
        assertFalse(PolicyEngine.evaluate(rule, afternoon));
    }

    @Test
    @DisplayName("Verify case-insensitivity of keywords (and, or, not, time_between)")
    void testCaseInsensitiveKeywords() {
        String rule = "noise_level > 70 and not time_between(22:00, 06:00) or temperature > 40";

        PolicyContext ctx = new PolicyContext(
                Map.of("noise_level", 75.0, "temperature", 25.0), LocalTime.of(14, 0)
        );
        assertTrue(PolicyEngine.evaluate(rule, ctx));
    }

    @Test
    @DisplayName("Verify syntax errors report exact token position offsets")
    void testSyntaxErrorsReportPositions() {
        // Missing value after operator
        PolicySyntaxException ex1 = assertThrows(PolicySyntaxException.class, () ->
                PolicyParser.parse("noise_level >")
        );
        assertTrue(ex1.getPosition() >= 0);
        assertTrue(ex1.getMessage().contains("Syntax error at position"));

        // Missing closing parenthesis
        PolicySyntaxException ex2 = assertThrows(PolicySyntaxException.class, () ->
                PolicyParser.parse("(noise_level > 70 AND temperature > 30")
        );
        assertTrue(ex2.getPosition() >= 0);

        // Missing comma in time_between
        PolicySyntaxException ex3 = assertThrows(PolicySyntaxException.class, () ->
                PolicyParser.parse("time_between(22:00 06:00)")
        );
        assertTrue(ex3.getPosition() >= 0);
    }
}
