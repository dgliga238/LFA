import java.util.*;

class Grammar {
    //Non-terminal symbols
    private static final char[] VN = {'S', 'F', 'D'};
    //Terminal symbols
    private static final char[] VT = {'a', 'b', 'c'};
    //Production rules
    private static final String[] P = {
            "S → aF",
            "F → bF",
            "F → cD",
            "S → bS",
            "D → cS",
            "D → a",
            "F → a"
    };
    //Random object for generating random productions
    private static final Random random = new Random();

    // Function to generate a specified number of valid strings
    public static List<String> generateStrings(int count) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StringBuilder sb = new StringBuilder();
            generateString('S', sb);
            strings.add(sb.toString());
        }
        return strings;
    }

    // Recursive function to generate a string following the grammar rules
    private static void generateString(char symbol, StringBuilder sb) {
        if (isTerminal(symbol)) {
            sb.append(symbol);
        } else {
            List<String> productions = getProductionsForNonTerminal(symbol);
            String selectedProduction = productions.get(random.nextInt(productions.size()));
            for (char c : selectedProduction.toCharArray()) {
                if (c != '→' && c != ' ') {
                    generateString(c, sb);
                }
            }
        }
    }

    // Function to check if a symbol is terminal
    private static boolean isTerminal(char symbol) {
        for (char vt : VT) {
            if (symbol == vt) {
                return true;
            }
        }
        return false;
    }

    // Function to get productions for a given non-terminal symbol
    private static List<String> getProductionsForNonTerminal(char symbol) {
        List<String> productions = new ArrayList<>();
        for (String production : P) {
            if (production.charAt(0) == symbol) {
                productions.add(production.substring(4));
            }
        }
        return productions;
    }
}



