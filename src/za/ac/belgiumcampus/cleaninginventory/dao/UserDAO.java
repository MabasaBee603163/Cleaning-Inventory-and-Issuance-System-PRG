package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.User;
import java.util.List;

public interface UserDAO {

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(long userId);

    User getUserById(long userId);

    User getUserByUsername(String username);

    List<User> getAllUsers();
}