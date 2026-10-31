package lk.jiat.smartcity360.policy;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Recursive descent parser constructing an AST of PolicyExpressions from tokenized rules.
 */
public class PolicyParser {

    private final List<PolicyTokenizer.Token> tokens;
    private int current = 0;

    public PolicyParser(List<PolicyTokenizer.Token> tokens) {
        this.tokens = tokens;
    }

    public static PolicyExpression parse(String ruleText) {
        List<PolicyTokenizer.Token> tokens = PolicyTokenizer.tokenize(ruleText);
        PolicyParser parser = new PolicyParser(tokens);
        PolicyExpression expr = parser.parseRule();
        if (!parser.isAtEnd()) {
            throw new PolicySyntaxException("Unexpected trailing tokens", parser.peek().getPosition());
        }
        return expr;
    }

    public PolicyExpression parseRule() {
        return parseOrExpr();
    }

    private PolicyExpression parseOrExpr() {
        PolicyExpression expr = parseAndExpr();
        while (match(PolicyTokenizer.TokenType.OR)) {
            PolicyExpression right = parseAndExpr();
            expr = new OrExpression(expr, right);
        }
        return expr;
    }

    private PolicyExpression parseAndExpr() {
        PolicyExpression expr = parseUnary();
        while (match(PolicyTokenizer.TokenType.AND)) {
            PolicyExpression right = parseUnary();
            expr = new AndExpression(expr, right);
        }
        return expr;
    }

    private PolicyExpression parseUnary() {
        if (match(PolicyTokenizer.TokenType.NOT)) {
            return new NotExpression(parseUnary());
        }
        return parsePrimary();
    }

    private PolicyExpression parsePrimary() {
        if (match(PolicyTokenizer.TokenType.LPAREN)) {
            PolicyExpression expr = parseOrExpr();
            consume(PolicyTokenizer.TokenType.RPAREN, "Expected ')' after expression");
            return expr;
        }
        if (match(PolicyTokenizer.TokenType.TIME_BETWEEN)) {
            return parseTimeBetween();
        }
        return parseComparison();
    }

    private PolicyExpression parseTimeBetween() {
        consume(PolicyTokenizer.TokenType.LPAREN, "Expected '(' after 'time_between'");
        PolicyTokenizer.Token startTok = consume(PolicyTokenizer.TokenType.TIME, "Expected start time (HH:mm)");
        consume(PolicyTokenizer.TokenType.COMMA, "Expected ',' between time values");
        PolicyTokenizer.Token endTok = consume(PolicyTokenizer.TokenType.TIME, "Expected end time (HH:mm)");
        consume(PolicyTokenizer.TokenType.RPAREN, "Expected ')' after time values");

        try {
            LocalTime start = LocalTime.parse(startTok.getText());
            LocalTime end = LocalTime.parse(endTok.getText());
            return new TimeWindow(start, end);
        } catch (DateTimeParseException e) {
            throw new PolicySyntaxException("Invalid time format (expected HH:mm)", startTok.getPosition());
        }
    }

    private PolicyExpression parseComparison() {
        ValueExpression left = parseValue();
        PolicyTokenizer.Token opTok = consume(PolicyTokenizer.TokenType.OP, "Expected comparison operator");
        ComparisonOperator op = ComparisonOperator.fromSymbol(opTok.getText());
        ValueExpression right = parseValue();
        return new ComparisonExpression(left, op, right);
    }

    private ValueExpression parseValue() {
        if (match(PolicyTokenizer.TokenType.NUMBER)) {
            return new NumberLiteral(Double.parseDouble(previous().getText()));
        }
        if (match(PolicyTokenizer.TokenType.IDENT)) {
            return new VariableReference(previous().getText());
        }
        throw new PolicySyntaxException("Expected number or variable identifier", peek().getPosition());
    }

    private boolean match(PolicyTokenizer.TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private PolicyTokenizer.Token consume(PolicyTokenizer.TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw new PolicySyntaxException(message, peek().getPosition());
    }

    private boolean check(PolicyTokenizer.TokenType type) {
        return !isAtEnd() && peek().getType() == type;
    }

    private PolicyTokenizer.Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == PolicyTokenizer.TokenType.EOF;
    }

    private PolicyTokenizer.Token peek() {
        return tokens.get(current);
    }

    private PolicyTokenizer.Token previous() {
        return tokens.get(current - 1);
    }
}
