package lk.jiat.smartcity360.policy;

import java.util.ArrayList;
import java.util.List;

/**
 * Lexical analyzer tokenizing smart city policy rule string into a list of typed tokens.
 */
public class PolicyTokenizer {

    public enum TokenType {
        NUMBER,
        IDENT,
        TIME,
        OR,
        AND,
        NOT,
        TIME_BETWEEN,
        OP,
        LPAREN,
        RPAREN,
        COMMA,
        EOF
    }

    public static class Token {
        private final TokenType type;
        private final String text;
        private final int position;

        public Token(TokenType type, String text, int position) {
            this.type = type;
            this.text = text;
            this.position = position;
        }

        public TokenType getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        public int getPosition() {
            return position;
        }
    }

    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        if (input == null || input.isBlank()) {
            tokens.add(new Token(TokenType.EOF, "", 0));
            return tokens;
        }

        int i = 0;
        int len = input.length();
        while (i < len) {
            char c = input.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (c == '(') {
                tokens.add(new Token(TokenType.LPAREN, "(", i++));
            } else if (c == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")", i++));
            } else if (c == ',') {
                tokens.add(new Token(TokenType.COMMA, ",", i++));
            } else if (isOperatorStart(c)) {
                int start = i;
                String op = readOperator(input, i);
                i += op.length();
                tokens.add(new Token(TokenType.OP, op, start));
            } else if (Character.isDigit(c)) {
                int start = i;
                Token token = readNumberOrTime(input, start);
                i += token.getText().length();
                tokens.add(token);
            } else if (Character.isLetter(c) || c == '_') {
                int start = i;
                String word = readWord(input, start);
                i += word.length();
                tokens.add(createWordToken(word, start));
            } else {
                throw new PolicySyntaxException("Unexpected character '" + c + "'", i);
            }
        }
        tokens.add(new Token(TokenType.EOF, "", len));
        return tokens;
    }

    private static boolean isOperatorStart(char c) {
        return c == '>' || c == '<' || c == '=' || c == '!';
    }

    private static String readOperator(String input, int start) {
        if (start + 1 < input.length()) {
            String two = input.substring(start, start + 2);
            if (two.equals(">=") || two.equals("<=") || two.equals("==") || two.equals("!=")) {
                return two;
            }
        }
        char c = input.charAt(start);
        if (c == '>' || c == '<') {
            return String.valueOf(c);
        }
        throw new PolicySyntaxException("Invalid operator symbol: '" + c + "'", start);
    }

    private static Token readNumberOrTime(String input, int start) {
        int i = start;
        while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.' || input.charAt(i) == ':')) {
            i++;
        }
        String text = input.substring(start, i);
        if (text.contains(":")) {
            return new Token(TokenType.TIME, text, start);
        }
        return new Token(TokenType.NUMBER, text, start);
    }

    private static String readWord(String input, int start) {
        int i = start;
        while (i < input.length() && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')) {
            i++;
        }
        return input.substring(start, i);
    }

    private static Token createWordToken(String word, int pos) {
        String upper = word.toUpperCase();
        return switch (upper) {
            case "OR" -> new Token(TokenType.OR, word, pos);
            case "AND" -> new Token(TokenType.AND, word, pos);
            case "NOT" -> new Token(TokenType.NOT, word, pos);
            case "TIME_BETWEEN" -> new Token(TokenType.TIME_BETWEEN, word, pos);
            default -> new Token(TokenType.IDENT, word, pos);
        };
    }
}
