package exception;

/** Thrown when employee data fails business/domain validation rules. */
public class InvalidEmployeeException extends Exception {
    public InvalidEmployeeException(String message) { super(message); }
}
