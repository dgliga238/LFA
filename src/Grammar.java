import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Class representing a context-free grammar
class Grammar {
    // Define the non-terminal symbols
    private static final char[] VN = {'S', 'F', 'D'};
    // Define the terminal symbols
    private static final char[] VT = {'a', 'b', 'c'};
    // Define the production rules
    private static final String[] P = {
            "S → aF",
            "F → bF",
            "F → cD",
            "S → bS",
            "D → cS",
            "D → a",
            "F → a"
    };
    // Create a Random object for generating random productions
    private static final Random random = new Random();

    // Function to generate a specified number of valid strings
    public static List<String> generateStrings(int count) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StringBuilder sb = new StringBuilder();
            generateString('S', sb); // Start the generation process from the start symbol 'S'
            strings.add(sb.toString()); // Add the generated string to the list
        }
        return strings; // Return the list of generated strings
    }

    // Recursive function to generate a string following the grammar rules
    private static void generateString(char symbol, StringBuilder sb) {
        if (isTerminal(symbol)) { // If the symbol is terminal, append it to the string
            sb.append(symbol);
        } else { // If the symbol is non-terminal, select a production and expand it recursively
            List<String> productions = getProductionsForNonTerminal(symbol);
            String selectedProduction = productions.get(random.nextInt(productions.size())); // Choose a random production
            for (char c : selectedProduction.toCharArray()) { // Iterate through the characters of the selected production
                if (c != '→' && c != ' ') { // Ignore the arrow symbol and spaces
                    generateString(c, sb); // Recursively expand the non-terminal symbol
                }
            }
        }
    }

    // Function to check if a symbol is terminal
    private static boolean isTerminal(char symbol) {
        for (char vt : VT) { // Iterate through the terminal symbols
            if (symbol == vt) {
                return true; // If the symbol matches any terminal symbol, return true
            }
        }
        return false; // If the symbol doesn't match any terminal symbol, return false
    }

    // Function to get productions for a given non-terminal symbol
    private static List<String> getProductionsForNonTerminal(char symbol) {
        List<String> productions = new ArrayList<>();
        for (String production : P) { // Iterate through the production rules
            if (production.charAt(0) == symbol) { // If the production rule starts with the given non-terminal symbol
                productions.add(production.substring(4)); // Add the production to the list, excluding the arrow symbol and spaces
            }
        }
        return productions; // Return the list of productions for the given non-terminal symbol
    }
}



