package za.ac.belgiumcampus.cleaninginventory.model;
import java.time.LocalDateTime;
import za.ac.belgiumcampus.cleaninginventory.enums.UserRole;


public class User {
    private long userId;

    private String username;

    private String passwordHash;

    private String fullName;

    private String email;

    private UserRole role;

    private LocalDateTime createdAt;

    public User() {
    }


    
    public User(long userId, String username, String passwordHash, String fullName, String email, UserRole role, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters
    public long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
public String toString() {
    return "User{" +
            "userId=" + userId +
            ", username='" + username + '\'' +
            ", fullName='" + fullName + '\'' +
            ", email='" + email + '\'' +
            ", role=" + role +
            ", createdAt=" + createdAt +
            '}';
}
}
