package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.User;
import java.util.List;

public interface UserDAO {
    User authenticateUser(String username, String password);
    boolean addUser(User user);
    User getUserById(long userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(long userId);
    boolean usernameExists(String username);
    boolean emailExists(String email);
}
