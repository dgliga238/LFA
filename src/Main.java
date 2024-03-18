import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> validStrings = Grammar.generateStrings(5);
        for (String string : validStrings) {
            System.out.println(string);
        }
        String classification = Grammar.classifyGrammar();

        // Print the classification
        System.out.println("Classification of the grammar: " + classification);
    }
}
