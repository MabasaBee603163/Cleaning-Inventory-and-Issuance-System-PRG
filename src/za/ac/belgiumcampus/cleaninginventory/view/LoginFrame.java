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
        setSize(400, 350);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel lblTitle = new JLabel("Cleaning Inventory System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        JLabel lblSubtitle = new JLabel("Login to access the system");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        mainPanel.add(lblSubtitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(15);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtUsername, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(txtPassword, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(100, 35));

        btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(46, 204, 113));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 5;
        mainPanel.add(lblStatus, gbc);

        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login());

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
            DashboardFrame dashboard = new DashboardFrame(authService);
            dashboard.setVisible(true);
            this.dispose();
        } else {
            lblStatus.setText("Invalid username or password!");
            txtPassword.setText("");
        }
    }
}
