package exception;

/** Thrown for login failures, invalid credentials, or account lockouts. */
public class AuthenticationException extends Exception {
    public AuthenticationException(String message) { super(message); }
}
