package ui;

import exception.AuthenticationException;
import model.User;
import service.AuthenticationService;
import util.Constants;
import util.InputReader;

/** Handles the login screen and the max-3-attempts lockout rule. */
public class LoginUI {

    private final AuthenticationService authService;
    private final InputReader input;

    public LoginUI(AuthenticationService authService, InputReader input) {
        this.authService = authService;
        this.input = input;
    }

    /** Returns the authenticated User, or null if attempts were exhausted. */
    public User login() {
        ConsoleUtil.printHeader("EMPLOYEE MANAGEMENT SYSTEM");
        System.out.println("                Version 1.0");
        System.out.println(ConsoleUtil.LINE);

        int attemptsLeft = Constants.MAX_LOGIN_ATTEMPTS;

        while (attemptsLeft > 0) {
            System.out.println();
            String username = input.readLine("Username: ");
            String password = input.readPassword("Password: ");

            try {
                User user = authService.login(username, password);
                authService.recordLoginAttempt(username, true);
                ConsoleUtil.printSuccess("Login successful. Welcome, " + user.getUsername() + "!");
                return user;
            } catch (AuthenticationException ex) {
                authService.recordLoginAttempt(username, false);
                attemptsLeft--;
                ConsoleUtil.printError(ex.getMessage() + " (" + attemptsLeft + " attempt(s) remaining)");
            }
        }

        ConsoleUtil.printError("Maximum login attempts exceeded. Terminating application.");
        return null;
    }
}
