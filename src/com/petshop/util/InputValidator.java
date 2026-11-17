package com.petshop.util;

import java.util.Scanner;

/**
 * Robust input sanitation and console reading utility.
 * Prevents InputMismatchException and provides clean terminal error recovery (Syllabus Unit 1).
 */
public final class InputValidator {

    private InputValidator() {
        // Private constructor for static utility class
    }

    public static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("  [!] Please enter a whole number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number format. Please enter an integer.");
            }
        }
    }

    public static double readDouble(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("  [!] Please enter a value between %.2f and %.2f.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Invalid number format. Please enter a valid decimal number.");
            }
        }
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("  [!] Input cannot be blank. Please try again.");
        }
    }

    public static boolean readBoolean(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes") || input.equals("true")) {
                return true;
            }
            if (input.equals("n") || input.equals("no") || input.equals("false")) {
                return false;
            }
            System.out.println("  [!] Please enter 'y' for Yes or 'n' for No.");
        }
    }
}
