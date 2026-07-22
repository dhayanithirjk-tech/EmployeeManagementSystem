package model;

/** A login account for the system (admin or staff). */
public class User {
    private final String username;
    private String passwordHash;
    private final String role;

    public User(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }

    public String toFileLine() {
        return username + "|" + passwordHash + "|" + role;
    }

    public static User fromFileLine(String line) {
        String[] p = line.split("\\|", -1);
        return new User(p[0], p[1], p[2]);
    }
}
