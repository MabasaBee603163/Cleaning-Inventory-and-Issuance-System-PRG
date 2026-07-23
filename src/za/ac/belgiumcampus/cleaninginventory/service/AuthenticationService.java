package za.ac.belgiumcampus.cleaninginventory.service;

import za.ac.belgiumcampus.cleaninginventory.dao.UserDAO;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.UserDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.enums.UserRole;
import za.ac.belgiumcampus.cleaninginventory.model.User;
import za.ac.belgiumcampus.cleaninginventory.utils.PasswordUtil;
import za.ac.belgiumcampus.cleaninginventory.utils.Validator;

/**
 * Session-aware authentication used by the Swing UI.
 * Passwords are stored/compared as SHA-256 hex digests.
 */
public class AuthenticationService {

    private final UserDAO userDAO;
    private User currentUser;

    public AuthenticationService() {
        this(new UserDAOImpl());
    }

    public AuthenticationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean login(String username, String password) {
        Validator.requireNonBlank(username, "Username");
        Validator.requireNonBlank(password, "Password");

        User user = userDAO.authenticateUser(username.trim(), PasswordUtil.hash(password));
        if (user == null) {
            return false;
        }
        currentUser = user;
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean registerUser(String username, String password, String fullName, String email, String role) {
        Validator.requireNonBlank(username, "Username");
        Validator.requireNonBlank(password, "Password");
        Validator.requireNonBlank(fullName, "Full name");
        Validator.requireNonBlank(email, "Email");
        Validator.requireNonBlank(role, "Role");

        if (userDAO.usernameExists(username.trim()) || userDAO.emailExists(email.trim())) {
            return false;
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setRole(UserRole.valueOf(role.trim().toUpperCase()));
        return userDAO.addUser(user);
    }
}
