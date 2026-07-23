package za.ac.belgiumcampus.cleaninginventory.ui;

import za.ac.belgiumcampus.cleaninginventory.controller.LoginController;
import za.ac.belgiumcampus.cleaninginventory.model.User;
import za.ac.belgiumcampus.cleaninginventory.utils.ActionResult;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Login screen. Calls LoginController (backend) — never talks to the database directly.
 */
public class LoginFrame extends JFrame {

    private final LoginController loginController = new LoginController();
    private final JTextField usernameField = new JTextField("admin", 18);
    private final JPasswordField passwordField = new JPasswordField("admin123", 18);

    public LoginFrame() {
        setTitle("Cleaning Inventory and Issuance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(titleLabel.getFont().deriveFont(20f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Username"), gbc);

        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Password"), gbc);

        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin());

        gbc.gridy = 3;
        gbc.gridx = 1;
        formPanel.add(loginButton, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        ActionResult<User> result = loginController.login(username, password);
        if (!result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Login failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = result.getData();
        JOptionPane.showMessageDialog(
                this,
                "Welcome, " + user.getFullName() + " (" + user.getRole() + ")\nBackend login OK.",
                "Login successful",
                JOptionPane.INFORMATION_MESSAGE);

        // DashboardFrame is still a teammate stub — keep login open for now until dashboard is ready.
        // When DashboardFrame is implemented, open it here and dispose this frame.
    }
}
