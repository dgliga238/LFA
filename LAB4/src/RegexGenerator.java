import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RegexGenerator {
    private static final Random randomNumberGenerator = new Random();
    public static List<String> generateFromRegex(String regex, int numStrings) {
        List<String> resultList = new ArrayList<>();

        // Loop to generate the specified number of strings
        for (int s = 0; s < numStrings; s++) {
            StringBuilder resultBuilder = new StringBuilder();
            String lastAppendedString = "";

            // Loop through each character in the regex pattern
            for (int i = 0; i < regex.length(); i++) {
                char currentChar = regex.charAt(i);
                switch (currentChar) {
                    case '(':
                        // Process groups enclosed in parentheses
                        int closingIndex = regex.indexOf(')', i);
                        String[] options = regex.substring(i + 1, closingIndex).split("\\|");
                        String chosenOption = options[randomNumberGenerator.nextInt(options.length)];
                        lastAppendedString = chosenOption;
                        resultBuilder.append(chosenOption);
                        i = closingIndex; // Skip to the end of the group
                        break;
                    case '*':
                        // Handle zero or more repetitions of the lastAppendedString
                        if (lastAppendedString.length() > 0) {
                            int repetitions = randomNumberGenerator.nextInt(5);
                            for (int j = 0; j < repetitions; j++) {
                                resultBuilder.append(lastAppendedString);
                            }
                        }
                        break;
                    case '+':
                        // Handle one or more repetitions of the lastAppendedString
                        int repetitionsPlus = 1 + randomNumberGenerator.nextInt(5);
                        for (int j = 0; j < repetitionsPlus; j++) {
                            resultBuilder.append(lastAppendedString);
                        }
                        break;
                    case '?':
                        // Check if the next character is present and append if true
                        if (randomNumberGenerator.nextBoolean()) {
                            resultBuilder.append(lastAppendedString);
                        }
                        break;
                    case '^':
                        // Handle repetition count specified after '^'
                        int repeatCount = Character.getNumericValue(regex.charAt(i + 1));
                        for (int j = 0; j < repeatCount - 1; j++) {
                            resultBuilder.append(lastAppendedString);
                        }
                        i++; // Move past the repetition count
                        break;
                    default:
                        // Append literal characters directly
                        lastAppendedString = Character.toString(currentChar);
                        resultBuilder.append(currentChar);
                        break;
                }
            }
            // Add the generated string to the result list
            resultList.add(resultBuilder.toString());
        }
        return resultList;
    }

    // Method to describe the processing steps of a regex pattern
    public static void describeRegexProcessing(String regex) {
        StringBuilder descriptionBuilder = new StringBuilder();
        int stepCounter = 1;

        // Start building the description
        descriptionBuilder.append("Processing sequence for regex: ").append(regex).append("\n");

        // Loop through each character in the regex pattern
        for (int i = 0; i < regex.length(); i++) {
            char currentChar = regex.charAt(i);
            switch (currentChar) {
                case '(':
                    // Describe group found in parentheses
                    int closingIndex = regex.indexOf(')', i);
                    descriptionBuilder.append(stepCounter++).append(". Found a group '(")
                            .append(regex.substring(i + 1, closingIndex)).append(")'.\n");
                    i = closingIndex; // Move past the group
                    break;
                case '+':
                    // Describe the '+' symbol indicating repetition
                    descriptionBuilder.append(stepCounter++).append(". Found a '+', indicating repetition.\n");
                    break;
                case '*':
                    // Describe the '*' symbol indicating zero or more repetition
                    descriptionBuilder.append(stepCounter++).append(". Found a '*', indicating zero or more repetition.\n");
                    break;
                case '?':
                    // Describe the '?' symbol indicating optional repetition
                    descriptionBuilder.append(stepCounter++).append(". Found a '?', indicating optional repetition.\n");
                    break;
                case '^':
                    // Describe the '^' symbol indicating repetition count
                    int repeatCount = Character.getNumericValue(regex.charAt(i + 1));
                    descriptionBuilder.append(stepCounter++).append(". Found a '^', repeating previous ")
                            .append(repeatCount).append(" times.\n");
                    i++; // Move past the repetition count
                    break;
                default:
                    // Describe literal characters found in the regex
                    descriptionBuilder.append(stepCounter++).append(". Found '").append(currentChar).append("'.\n");
                    break;
            }
        }

        // Add the end of processing message
        descriptionBuilder.append(stepCounter).append(". End of processing.\n");

        // Print the generated description
        System.out.println(descriptionBuilder.toString());
    }

    public static void main(String[] args) {
        int numStringsToGenerate = 5; // Number of strings to generate for each regex

        // Generate and print strings for different regex patterns
        System.out.println(" expression 1:");
        List<String> regex1Strings = generateFromRegex("O(P|Q|R)+2(3|4)", numStringsToGenerate);
        for (String str : regex1Strings) {
            System.out.println(str);
        }

        System.out.println("\nexpression 2:");
        List<String> regex2Strings = generateFromRegex("A*B(C|D|E)F(G|H|I)^2.", numStringsToGenerate);
        for (String str : regex2Strings) {
            System.out.println(str);
        }

        System.out.println("\nexpression 3:");
        List<String> regex3Strings = generateFromRegex("J+K(L|M|N)*O?(P|Q)^3.", numStringsToGenerate);
        for (String str : regex3Strings) {
            System.out.println(str);
        }

        // Describe the processing of a regex pattern
        System.out.println("\nDescribe regex processing for next regex:");
        describeRegexProcessing("J+K(L|M|N)*O?(P|Q)^3.");
    }
}
