package exception;

/** Thrown when a lookup for an Employee (by ID, name, etc.) fails. */
public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String message) { super(message); }
}
