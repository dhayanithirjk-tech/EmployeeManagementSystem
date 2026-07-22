package exception;

/** Generic exception for raw user input that fails validation. */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) { super(message); }
}
