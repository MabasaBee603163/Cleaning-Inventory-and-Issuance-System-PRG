package za.ac.belgiumcampus.cleaninginventory.view;

import za.ac.belgiumcampus.cleaninginventory.service.AuthenticationService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JLabel lblStatus;
    
    private AuthenticationService authService;
    
    public LoginFrame() {
        authService = new AuthenticationService();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Cleaning Inventory System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel lblTitle = new JLabel("Cleaning Inventory System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);
        
        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        
        // Status label
        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);
        gbc.gridy = 4;
        mainPanel.add(lblStatus, gbc);
        
        // Add login action
        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());
        
        // Add register action
        btnRegister.addActionListener(e -> {
            RegisterFrame registerFrame = new RegisterFrame();
            registerFrame.setVisible(true);
            this.dispose();
        });
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please enter username and password!");
            return;
        }
        
        if (authService.login(username, password)) {
            lblStatus.setText("Login successful!");
            // Open Dashboard
            DashboardFrame dashboard = new DashboardFrame(authService);
            dashboard.setVisible(true);
            this.dispose();
        } else {
            lblStatus.setText("Invalid username or password!");
        }
    }
}
