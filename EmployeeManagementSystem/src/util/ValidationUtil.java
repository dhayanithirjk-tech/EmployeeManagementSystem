package util;

import exception.InvalidInputException;

import java.util.regex.Pattern;

/**
 * Centralised validation routines. Every raw piece of user input passes
 * through here before it is trusted anywhere else in the application -
 * this keeps validation logic in ONE place (Single Responsibility
 * Principle) and means the rest of the code can assume clean data.
 */
public final class ValidationUtil {

    private ValidationUtil() { }

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z][A-Za-z ]{1,49}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");

    public static void validateName(String name) throws InvalidInputException {
        if (name == null || !NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new InvalidInputException("Name should contain only alphabets and spaces (2-50 chars).");
        }
    }

    public static int validateAge(String ageStr) throws InvalidInputException {
        int age = parseInt(ageStr, "Age must be numeric.");
        if (age < 18 || age > 65) {
            throw new InvalidInputException("Age must be between 18 and 65.");
        }
        return age;
    }

    public static void validateGender(String gender) throws InvalidInputException {
        if (gender == null || gender.trim().isEmpty()) {
            throw new InvalidInputException("Gender cannot be empty.");
        }
    }

    public static void validateEmail(String email) throws InvalidInputException {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidInputException("Email is not a valid email address.");
        }
    }

    public static void validatePhone(String phone) throws InvalidInputException {
        if (phone == null || !PHONE_PATTERN.matcher(phone.trim()).matches()) {
            throw new InvalidInputException("Phone number must contain exactly 10 digits.");
        }
    }

    public static double validateSalary(String salaryStr) throws InvalidInputException {
        double salary = parseDouble(salaryStr, "Salary must be numeric.");
        if (salary < 0) {
            throw new InvalidInputException("Negative salary is not allowed.");
        }
        return salary;
    }

    public static int validateLeaveDays(String leaveStr) throws InvalidInputException {
        int days = parseInt(leaveStr, "Leave days must be numeric.");
        if (days < 0) {
            throw new InvalidInputException("Negative leave days are not allowed.");
        }
        return days;
    }

    public static double validatePermissionHours(String hoursStr) throws InvalidInputException {
        double hrs = parseDouble(hoursStr, "Permission hours must be numeric.");
        if (hrs < 0) {
            throw new InvalidInputException("Negative permission hours are not allowed.");
        }
        return hrs;
    }

    public static void validateNotEmpty(String value, String fieldName) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty.");
        }
    }

    public static void validateDate(String date) throws InvalidInputException {
        if (date == null || !date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new InvalidInputException("Date must be in yyyy-MM-dd format.");
        }
    }

    public static int parseInt(String value, String errorMessage) throws InvalidInputException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException | NullPointerException ex) {
            throw new InvalidInputException(errorMessage);
        }
    }

    public static double parseDouble(String value, String errorMessage) throws InvalidInputException {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException | NullPointerException ex) {
            throw new InvalidInputException(errorMessage);
        }
    }
}
