package za.ac.belgiumcampus.cleaninginventory.view;

import za.ac.belgiumcampus.cleaninginventory.model.User;
import za.ac.belgiumcampus.cleaninginventory.enums.UserRole;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.UserDAOImpl;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField txtFullName, txtUsername, txtEmail;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<UserRole> cmbRole;
    private JButton btnRegister, btnCancel;
    private JLabel lblStatus;
    
    private UserDAOImpl userDAO;
    
    public RegisterFrame() {
        userDAO = new UserDAOImpl();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Register New User");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 450);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel lblTitle = new JLabel("Register New User");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);
        
        // Full Name
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        txtFullName = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(txtFullName, gbc);
        
        // Username
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);
        
        // Email
        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(txtEmail, gbc);
        
        // Password
        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);
        
        // Confirm Password
        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        txtConfirmPassword = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(txtConfirmPassword, gbc);
        
        // Role
        gbc.gridy = 6;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Role:"), gbc);
        cmbRole = new JComboBox<>(UserRole.values());
        gbc.gridx = 1;
        mainPanel.add(cmbRole, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnRegister = new JButton("Register");
        btnCancel = new JButton("Cancel");
        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);
        
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        
        // Status
        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);
        gbc.gridy = 8;
        mainPanel.add(lblStatus, gbc);
        
        // Add actions
        btnRegister.addActionListener(e -> register());
        btnCancel.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void register() {
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        UserRole role = (UserRole) cmbRole.getSelectedItem();
        
        // Validation
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()) {
            lblStatus.setText("Please fill in all fields!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            lblStatus.setText("Passwords do not match!");
            return;
        }
        
        if (password.length() < 6) {
            lblStatus.setText("Password must be at least 6 characters!");
            return;
        }
        
        if (userDAO.usernameExists(username)) {
            lblStatus.setText("Username already exists!");
            return;
        }
        
        if (userDAO.emailExists(email)) {
            lblStatus.setText("Email already registered!");
            return;
        }
        
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password); // Are we considering hashing this hmmmm 
        user.setRole(role);
        
        if (userDAO.addUser(user)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            new LoginFrame().setVisible(true);
            this.dispose();
        } else {
            lblStatus.setText("Registration failed!");
        }
    }
}
