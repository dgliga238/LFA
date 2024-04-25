import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    // Define patterns for tokens
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    private static final Pattern OPERATOR_PATTERN = Pattern.compile("[+\\-*/]");
    private static final Pattern PAREN_PATTERN = Pattern.compile("[()]");

    // Token types
    enum TokenType {
        IDENTIFIER,
        NUMBER,
        OPERATOR,
        LPAREN,  // Left Parenthesis "("
        RPAREN   // Right Parenthesis ")"
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

            // Match parentheses
            matcher = PAREN_PATTERN.matcher(input);
            if (matcher.lookingAt()) {
                String paren = matcher.group();
                if (paren.equals("(")) {
                    tokens.add(new Token(TokenType.LPAREN, paren));
                } else {
                    tokens.add(new Token(TokenType.RPAREN, paren));
                }
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
        String input = "a + b * c";
        List<Token> tokens = lexer.tokenize(input);

        // Print tokens
        for (Token token : tokens) {
            System.out.println(token.type + ": " + token.value);
        }

        Parser parser = new Parser(tokens);
        Node rootNode = parser.parse();

        // Print AST
        System.out.println("AST:");
        printAST(rootNode);
    }

    // AST Node types
    enum NodeType {
        BINARY_OP,
        IDENTIFIER,
        NUMBER
    }

    // AST Node class
    static class Node {
        NodeType type;
        String value;
        Node left;
        Node right;

        Node(NodeType type, String value) {
            this.type = type;
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    public static class Parser {
        int currentTokenIndex;
        List<Token> tokens;

        Parser(List<Token> tokens) {
            this.tokens = tokens;
            this.currentTokenIndex = 0;
        }

        Node parse() {
            return expression();
        }

        // EBNF for expression: expression ::= term { ('+' | '-') term }
        private Node expression() {
            Node left = term();
            while (currentTokenIndex < tokens.size()) {
                Token token = tokens.get(currentTokenIndex);
                if (token.type == TokenType.OPERATOR && (token.value.equals("+") || token.value.equals("-"))) {
                    currentTokenIndex++;
                    Node right = term();
                    Node opNode = new Node(NodeType.BINARY_OP, token.value);
                    opNode.left = left;
                    opNode.right = right;
                    left = opNode;
                } else {
                    break;
                }
            }
            return left;
        }

        // EBNF for term: term ::= factor { ('*' | '/') factor }
        private Node term() {
            Node left = factor();
            while (currentTokenIndex < tokens.size()) {
                Token token = tokens.get(currentTokenIndex);
                if (token.type == TokenType.OPERATOR && (token.value.equals("*") || token.value.equals("/"))) {
                    currentTokenIndex++;
                    Node right = factor();
                    Node opNode = new Node(NodeType.BINARY_OP, token.value);
                    opNode.left = left;
                    opNode.right = right;
                    left = opNode;
                } else {
                    break;
                }
            }
            return left;
        }

        // EBNF for factor: factor ::= NUMBER | IDENTIFIER | '(' expression ')'
        private Node factor() {
            Token token = tokens.get(currentTokenIndex++);
            if (token.type == TokenType.NUMBER || token.type == TokenType.IDENTIFIER) {
                return new Node(token.type == TokenType.NUMBER ? NodeType.NUMBER : NodeType.IDENTIFIER, token.value);
            } else if (token.type == TokenType.LPAREN) {
                Node exprNode = expression();
                // Expecting a closing parenthesis
                if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).type == TokenType.RPAREN) {
                    currentTokenIndex++; // Consume the ')'
                    return exprNode;
                } else {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
            } else {
                throw new IllegalArgumentException("Unexpected token: " + token.value);
            }
        }
    }

    private static void printAST(Node node) {
        if (node == null) return;
        if (node.type == NodeType.BINARY_OP) {
            System.out.println("Binary Operator: " + node.value);
        } else {
            System.out.println("Operand: " + node.value);
        }
        printAST(node.left);
        printAST(node.right);
    }
}
