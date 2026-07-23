package za.ac.belgiumcampus.cleaninginventory.service;

import za.ac.belgiumcampus.cleaninginventory.dao.UserDAO;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.UserDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.model.User;
import za.ac.belgiumcampus.cleaninginventory.utils.PasswordUtil;
import za.ac.belgiumcampus.cleaninginventory.utils.Validator;

/**
 * Authenticates users by username and SHA-256 password hash.
 */
public class AuthenticationService {

    private final UserDAO userDAO;

    public AuthenticationService() {
        this(new UserDAOImpl());
    }

    public AuthenticationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Validates credentials and returns the authenticated user, or null if invalid.
     */
    public User login(String username, String password) {
        Validator.requireNonBlank(username, "Username");
        Validator.requireNonBlank(password, "Password");

        User user = userDAO.getUserByUsername(username.trim());
        if (user == null) {
            return null;
        }

        if (!PasswordUtil.matches(password, user.getPasswordHash())) {
            return null;
        }

        return user;
    }
}
