package exception;

/** Thrown when a salary-related figure violates business rules. */
public class InvalidSalaryException extends Exception {
    public InvalidSalaryException(String message) { super(message); }
}
