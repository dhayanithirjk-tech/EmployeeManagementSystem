package ui;

import exception.AuthenticationException;
import model.User;
import service.AuthenticationService;
import util.Constants;
import util.FileManager;
import util.InputReader;

import java.util.List;

public class UserAccountMenu {

    private final AuthenticationService authService;
    private final InputReader input;
    private User currentUser;

    public UserAccountMenu(AuthenticationService authService, InputReader input, User currentUser) {
        this.authService = authService;
        this.input = input;
        this.currentUser = currentUser;
    }

    /** Returns false if the user chose to log out (caller should return to login screen). */
    public boolean show() {
        boolean back = false;
        while (!back) {
            ConsoleUtil.printHeader("USER ACCOUNT");
            System.out.println(" 1. Change Password");
            System.out.println(" 2. View Profile");
            System.out.println(" 3. Login History");
            System.out.println(" 4. Logout");
            System.out.println(" 5. Back");
            System.out.println(ConsoleUtil.LINE);

            int choice = input.readMenuChoice("Choose Option: ");
            switch (choice) {
                case 1: changePassword(); break;
                case 2: viewProfile(); break;
                case 3: loginHistory(); break;
                case 4: return false; // signal logout
                case 5: back = true; break;
                default: ConsoleUtil.printError("Invalid menu choice. Please try again.");
            }
        }
        return true;
    }

    private void changePassword() {
        ConsoleUtil.printSubHeader("Change Password");
        try {
            String oldPassword = input.readPassword("Current Password: ");
            String newPassword = input.readPassword("New Password: ");
            String confirm = input.readPassword("Confirm New Password: ");
            if (!newPassword.equals(confirm)) {
                ConsoleUtil.printError("New password and confirmation do not match.");
            } else {
                authService.changePassword(currentUser.getUsername(), oldPassword, newPassword);
                ConsoleUtil.printSuccess("Password changed successfully.");
            }
        } catch (AuthenticationException ex) {
            ConsoleUtil.printError(ex.getMessage());
        }
        input.pause();
    }

    private void viewProfile() {
        ConsoleUtil.printSubHeader("Profile");
        System.out.println("Username : " + currentUser.getUsername());
        System.out.println("Role     : " + currentUser.getRole());
        input.pause();
    }

    private void loginHistory() {
        ConsoleUtil.printSubHeader("Login History");
        List<String> lines = FileManager.readAllLines(Constants.LOGIN_HISTORY_FILE);
        if (lines.isEmpty()) {
            System.out.println("No login history available.");
        } else {
            int start = Math.max(0, lines.size() - 20);
            for (int i = start; i < lines.size(); i++) {
                System.out.println(lines.get(i));
            }
        }
        input.pause();
    }
}
