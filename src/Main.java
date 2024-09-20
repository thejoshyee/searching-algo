import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static final String[] STATES = {
        "Alabama", "Alaska", "Arizona", "Arkansas", "California",
        "Colorado", "Connecticut", "Delaware", "Florida", "Georgia",
        "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa",
        "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
        "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri",
        "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey",
        "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio",
        "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
        "South Dakota", "Tennessee", "Texas", "Utah", "Vermont",
        "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do  {
            displayMenu();
            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        displayText();
                        break;
                    case 2:
                        performSearch(scanner);
                        break;
                    case 3:
                        System.out.println("Exiting program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again!");
                }
            } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please try again!");
                    choice = 0;
                }
            } while (choice != 3);

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Display the text");
        System.out.println("2. Search");
        System.out.println("3. Exit program");
        System.out.print("Enter your choice: ");
    }

    private static void displayText() {
        System.out.println("\nList of 50 US States:");
        for (String state : STATES) {
            System.out.println(state);
        }
    }

    private static void performSearch(Scanner scanner) {
        System.out.print("Enter a search pattern: ");
        String pattern = scanner.nextLine().toLowerCase();

        System.out.println("Searching...");
        // Combine all state names into a single lowercase string for searching
        String text = String.join(" ", STATES).toLowerCase();
        List<Integer> occurrences = BoyerMoore.search(text, pattern);

        if (occurrences.isEmpty()) {
            System.out.println("Pattern not found.");
        } else {
            System.out.println("Pattern Found at indices: " + occurrences);
            for (int index : occurrences) {
                int stateIndex = findStateIndex(index);
                if (stateIndex != -1) {
                    System.out.println("Match in state: " + STATES[stateIndex]);
                }
            }
        }
    }

    private static int findStateIndex(int globalIndex) {
        int currentIndex = 0;
        for (int i = 0; i < STATES.length; i++) {
            if (globalIndex >= currentIndex && globalIndex < currentIndex + STATES[i].length()) {
                return i;
            }

            currentIndex += STATES[i].length() + 1;
        }
        return -1;
    }
}

class BoyerMoore {
    private static final int ALPHABET_SIZE = 256;

    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        int patternLength = pattern.length();
        int textLength = text.length();

        // Initialize the bad char array
        int[] lastOccurrence = new int[ALPHABET_SIZE];
        for (var i = 0; i < ALPHABET_SIZE; i++) {
            lastOccurrence[i] = -1;
        }

        // Fill the bad char array
        for (int i = 0; i < patternLength; i++) {
            lastOccurrence[pattern.charAt(i)] = i;
        }

        // Perform the search
        int textIndex = 0;
        while (textIndex <= textLength - patternLength) {
            int patternIndex = patternLength - 1;

            // Compare chars from right to left
            while (patternIndex >= 0 && pattern.charAt(patternIndex) == text.charAt(textIndex + patternIndex)) {
                patternIndex--;
            }

            if (patternIndex < 0) {
                // Pattern found
                occurrences.add(textIndex);
                textIndex += (textIndex + patternLength < textLength) ? patternLength - lastOccurrence[text.charAt(textIndex + patternLength)] : 1;
            } else {
                // Mismatch occurred, shift the pattern
                textIndex += Math.max(1, patternIndex - lastOccurrence[text.charAt(textIndex + patternIndex)]);
            }
        }

        return occurrences;
    }
}