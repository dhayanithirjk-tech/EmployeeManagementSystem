package service;

import exception.AuthenticationException;
import model.User;

public interface AuthenticationService {
    User login(String username, String password) throws AuthenticationException;
    void changePassword(String username, String oldPassword, String newPassword) throws AuthenticationException;
    void recordLoginAttempt(String username, boolean success);
}
