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

    public static String classifyGrammar() {
        Set<Character> terminals = new HashSet<>();
        Set<Character> nonTerminals = new HashSet<>();
        Set<String> productions = new HashSet<>();

        // Extract terminals, non-terminals, and productions from the grammar
        for (char vt : VT) {
            terminals.add(vt);
        }

        for (char vn : VN) {
            nonTerminals.add(vn);
        }

        for (String production : P) {
            productions.add(production);
        }

        // Determine the type of grammar based on Chomsky hierarchy

        if (isType3Grammar(productions, terminals, nonTerminals)) {
            return "Type 3";
        } else if (isType2Grammar(productions, terminals, nonTerminals))  {
            return "Type 2";
        } else if (isType1Grammar(productions, terminals, nonTerminals)) {
            return "Type 1";
        } else if (isType0Grammar(productions)) {
            return "Type 0";
        } else {
            return "Unknown Type";
        }
    }

    // Helper function to check if the grammar is a Type 0 grammar
    private static boolean isType0Grammar(Set<String> productions) {
        // Type 0 grammars are unrestricted, so any grammar is Type 0 if it's not lower in the hierarchy
        return true;
    }

    // Helper function to check if the grammar is a Type 1 grammar
    private static boolean isType1Grammar(Set<String> productions, Set<Character> terminals, Set<Character> nonTerminals) {
        // Type 1 grammars are context-sensitive, which means left-hand side of each production has to be longer
        // or equal to the right-hand side. Checking for this condition.
        for (String production : productions) {
            String[] parts = production.split("→");
            String lhs = parts[0].trim();
            String rhs = parts[1].trim();
            if (lhs.length() < rhs.length()) {
                return false;
            }
        }
        return true;
    }

    // Helper function to check if the grammar is a Type 2 grammar
    private static boolean isType2Grammar(Set<String> productions, Set<Character> terminals, Set<Character> nonTerminals) {
        // Type 2 grammars are context-free, so each production rule must have only one non-terminal on the left-hand side
        for (String production : productions) {
            String[] parts = production.split("→");
            String lhs = parts[0].trim();
            String rhs = parts[1].trim();
            if (lhs.length() != 1 || !nonTerminals.contains(lhs.charAt(0))) {
                return false;
            }
        }
        return true;
    }

    // Helper function to check if the grammar is a Type 3 grammar
    private static boolean isType3Grammar(Set<String> productions, Set<Character> terminals, Set<Character> nonTerminals) {
        // Type 3 grammars are regular, so each production rule must have only one non-terminal on the left-hand side
        // and terminal/non-terminal symbols must appear only at the start or end of the right-hand side
        for (String production : productions) {
            String[] parts = production.split("→");
            String lhs = parts[0].trim();
            String rhs = parts[1].trim();

            if (lhs.length() != 1 || !nonTerminals.contains(lhs.charAt(0))) {
                return false;
            }

            char firstChar = rhs.charAt(0);
            char lastChar = rhs.charAt(rhs.length() - 1);
            if ((!terminals.contains(firstChar) && !nonTerminals.contains(firstChar)) ||
                    (!terminals.contains(lastChar) && !nonTerminals.contains(lastChar))) {
                return false;
            }
        }
        return true;
    }
}


