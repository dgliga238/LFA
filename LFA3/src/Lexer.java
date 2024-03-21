import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    // Define patterns for tokens
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern OPERATOR_PATTERN = Pattern.compile("[+\\-*/]");

    // Token types
    enum TokenType {
        IDENTIFIER,
        NUMBER,
        OPERATOR
    }

    // Token class representing a token with its type and value
    static class Token {
        TokenType type;
        String value;

        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }

    // Method to tokenize input text
    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher;

        while (!input.isEmpty()) {
            // Match identifier
            matcher = IDENTIFIER_PATTERN.matcher(input);
            if (matcher.lookingAt()) {
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group()));
                input = input.substring(matcher.end());
                continue;
            }

            // Match number
            matcher = NUMBER_PATTERN.matcher(input);
            if (matcher.lookingAt()) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group()));
                input = input.substring(matcher.end());
                continue;
            }

            // Match operator
            matcher = OPERATOR_PATTERN.matcher(input);
            if (matcher.lookingAt()) {
                tokens.add(new Token(TokenType.OPERATOR, matcher.group()));
                input = input.substring(matcher.end());
                continue;
            }

            // If no match is found, skip the character
            input = input.substring(1);
        }

        return tokens;
    }

    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        String input = "result = (a + b) * (50 - d)";
        List<Token> tokens = lexer.tokenize(input);

        // Print tokens
        for (Token token : tokens) {
            System.out.println(token.type + ": " + token.value);
        }
    }
}
