package service;

import exception.AuthenticationException;
import model.User;
import util.Constants;
import util.FileManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default {@link AuthenticationService}.
 * <p>
 * Credentials are persisted in {@code data/users.txt} as
 * {@code username|sha256(password)|role}. A default admin account
 * (admin / admin123) is auto-created on first run if the file is empty.
 */
public class AuthenticationServiceImpl implements AuthenticationService {

    private final Map<String, User> users = new HashMap<>();

    public AuthenticationServiceImpl() {
        loadUsers();
        if (users.isEmpty()) {
            createDefaultAdmin();
        }
    }

    private void createDefaultAdmin() {
        User admin = new User("admin", hash("admin123"), "ADMIN");
        users.put(admin.getUsername(), admin);
        saveUsers();
    }

    @Override
    public User login(String username, String password) throws AuthenticationException {
        User user = users.get(username);
        if (user == null || !user.getPasswordHash().equals(hash(password))) {
            throw new AuthenticationException("Invalid username or password.");
        }
        return user;
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword)
            throws AuthenticationException {
        User user = users.get(username);
        if (user == null || !user.getPasswordHash().equals(hash(oldPassword))) {
            throw new AuthenticationException("Current password is incorrect.");
        }
        if (newPassword == null || newPassword.length() < 4) {
            throw new AuthenticationException("New password must be at least 4 characters long.");
        }
        user.setPasswordHash(hash(newPassword));
        saveUsers();
    }

    @Override
    public void recordLoginAttempt(String username, boolean success) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String line = timestamp + " | " + username + " | " + (success ? "SUCCESS" : "FAILURE");
        FileManager.appendLine(Constants.LOGIN_HISTORY_FILE, line);
    }

    private void loadUsers() {
        List<String> lines = FileManager.readAllLines(Constants.USERS_FILE);
        for (String line : lines) {
            try {
                User u = User.fromFileLine(line);
                users.put(u.getUsername(), u);
            } catch (Exception ex) {
                System.out.println("Warning: skipping corrupt user record.");
            }
        }
    }

    private void saveUsers() {
        java.util.List<String> lines = new java.util.ArrayList<>();
        for (User u : users.values()) {
            lines.add(u.toFileLine());
        }
        FileManager.writeAllLines(Constants.USERS_FILE, lines);
    }

    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException ex) {
            // SHA-256/UTF-8 are always available on a standard JVM; fall back defensively.
            return String.valueOf(input.hashCode());
        }
    }
}
