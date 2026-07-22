package za.ac.belgiumcampus.cleaninginventory.view;

import za.ac.belgiumcampus.cleaninginventory.service.AuthenticationService;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField txtFullName, txtUsername, txtEmail;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JComboBox<String> cmbRole;
    private JButton btnRegister, btnCancel;
    private JLabel lblStatus;
    private AuthenticationService authService;

    public RegisterFrame() {
        authService = new AuthenticationService();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Register New User");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 500);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Register New User");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        txtFullName = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtFullName, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtUsername, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtEmail, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        mainPanel.add(txtPassword, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        txtConfirmPassword = new JPasswordField(20);
        gbc.gridx = 1;
        mainPanel.add(txtConfirmPassword, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Role:"), gbc);
        cmbRole = new JComboBox<>(new String[]{"ADMIN", "STOREKEEPER", "SUPERVISOR"});
        gbc.gridx = 1;
        mainPanel.add(cmbRole, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(46, 204, 113));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setPreferredSize(new Dimension(100, 35));

        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setPreferredSize(new Dimension(100, 35));

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnCancel);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 8;
        mainPanel.add(lblStatus, gbc);

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
        String role = (String) cmbRole.getSelectedItem();

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please fill in all required fields!");
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

        if (!email.contains("@")) {
            lblStatus.setText("Please enter a valid email address!");
            return;
        }

        if (authService.registerUser(username, password, fullName, email, role)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            this.dispose();
        } else {
            lblStatus.setText("Username or email already exists!");
        }
    }
}
