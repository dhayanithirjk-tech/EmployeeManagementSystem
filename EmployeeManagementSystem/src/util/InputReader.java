package util;

import exception.InvalidInputException;

import java.io.Console;
import java.util.Scanner;

/**
 * Wraps a single shared {@link Scanner} and provides read-and-validate
 * helper methods so the UI layer never crashes on bad input. Every
 * "read a field" method loops, re-prompting on {@link InvalidInputException}
 * instead of propagating it up and killing the menu flow.
 */
public class InputReader {

    private final Scanner scanner;

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /** Reads a menu choice as an int; returns -1 (an always-invalid choice) on bad input. */
    public int readMenuChoice(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            System.out.println();
            System.out.println("-------------------------------------------");
            System.out.println("Invalid Input");
            System.out.println("Please enter a valid numeric value.");
            System.out.println("Returning to menu...");
            System.out.println("-------------------------------------------");
            return -1;
        }
    }

    /** Password entry: masked via java.io.Console when running in a real terminal. */
    public String readPassword(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] pwd = console.readPassword(prompt);
            return pwd == null ? "" : new String(pwd);
        }
        // Fallback (e.g. running inside an IDE) - Console is unavailable, so echo cannot be masked.
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public String readValidatedName(String prompt) {
        while (true) {
            try {
                String value = readLine(prompt);
                ValidationUtil.validateName(value);
                return value;
            } catch (InvalidInputException ex) {
                printError(ex.getMessage());
            }
        }
    }

    public int readValidatedAge(String prompt) {
        while (true) {
            try {
                return ValidationUtil.validateAge(readLine(prompt));
            } catch (InvalidInputException ex) {
                printError(ex.getMessage());
            }
        }
    }

    public String readValidatedEmail(String prompt) {
        while (true) {
            try {
                String value = readLine(prompt);
                ValidationUtil.validateEmail(value);
                return value;
            } catch (InvalidInputException ex) {
                printError(ex.getMessage());
            }
        }
    }

    public String readValidatedPhone(String prompt) {
        while (true) {
            try {
                String value = readLine(prompt);
                ValidationUtil.validatePhone(value);
                return value;
            } catch (InvalidInputException ex) {
                printError(ex.getMessage());
            }
        }
    }

    public double readValidatedSalary(String prompt) {
        while (true) {
            try {
                return ValidationUtil.validateSalary(readLine(prompt));
            } catch (InvalidInputException ex) {
                printError(ex.getMessage());
            }
        }
    }

    public int readValidatedLeaveDays(String prompt) {
        while (true) {
            try {
                return ValidationUtil.validateLeaveDays(readLine(prompt));
            } catch (InvalidInputException ex) {
                printError(ex.getMessage());
            }
        }
    }

    public double readValidatedPermissionHours(String prompt) {
        while (true) {
            try {
                return ValidationUtil.validatePermissionHours(readLine(prompt));
            } catch (InvalidInputException ex) {
                printError(ex.getMessage());
            }
        }
    }

    public String readNonEmpty(String prompt, String fieldName) {
        while (true) {
            try {
                String value = readLine(prompt);
                ValidationUtil.validateNotEmpty(value, fieldName);
                return value;
            } catch (InvalidInputException ex) {
                printError(ex.getMessage());
            }
        }
    }

    public String readValidatedDate(String prompt) {
        while (true) {
            try {
                String value = readLine(prompt);
                ValidationUtil.validateDate(value);
                return value;
            } catch (InvalidInputException ex) {
                printError(ex.getMessage());
            }
        }
    }

    public void printError(String message) {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Invalid Input");
        System.out.println(message);
        System.out.println("-------------------------------------------");
    }

    public void pause() {
        System.out.print("\nPress ENTER to continue...");
        scanner.nextLine();
    }
}
