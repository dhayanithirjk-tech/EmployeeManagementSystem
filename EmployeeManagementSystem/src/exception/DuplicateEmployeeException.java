package exception;

/** Thrown when an Employee ID that already exists is added again. */
public class DuplicateEmployeeException extends Exception {
    public DuplicateEmployeeException(String message) { super(message); }
}
