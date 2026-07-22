package za.ac.belgiumcampus.cleaninginventory.ui;

import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.CleanerDAOImpl;
import javax.swing.*;
import java.awt.*;

public class CleanerDialog extends JDialog {
    private Cleaner cleaner;
    private CleanerDAOImpl cleanerDAO;
    private boolean isEditMode;
    private JTextField txtFirstName, txtLastName, txtPhone, txtEmail, txtDepartment;

    public CleanerDialog(JFrame parent, Cleaner cleaner) {
        super(parent, "Cleaner Details", true);
        this.cleaner = cleaner;
        this.cleanerDAO = new CleanerDAOImpl();
        this.isEditMode = (cleaner != null);
        initComponents();
        if (isEditMode) {
            populateFields();
        }
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel(isEditMode ? "Edit Cleaner" : "Add New Cleaner");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(lblTitle, gbc);
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("First Name:"), gbc);
        txtFirstName = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtFirstName, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Last Name:"), gbc);
        txtLastName = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtLastName, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Phone:"), gbc);
        txtPhone = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtPhone, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtEmail, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Department:"), gbc);
        txtDepartment = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtDepartment, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnSave = new JButton(isEditMode ? "Update" : "Save");
        JButton btnCancel = new JButton("Cancel");
        styleButton(btnSave, isEditMode ? new Color(52, 152, 219) : new Color(46, 204, 113));
        styleButton(btnCancel, new Color(149, 165, 166));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        btnSave.addActionListener(e -> saveCleaner());
        btnCancel.addActionListener(e -> dispose());
    }

    private void populateFields() {
        txtFirstName.setText(cleaner.getFirstName());
        txtLastName.setText(cleaner.getLastName());
        txtPhone.setText(cleaner.getPhone());
        txtEmail.setText(cleaner.getEmail());
        txtDepartment.setText(cleaner.getDepartment());
    }

    private void saveCleaner() {
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String department = txtDepartment.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            showError("First name and last name are required!");
            return;
        }

        if (isEditMode) {
            cleaner.setFirstName(firstName);
            cleaner.setLastName(lastName);
            cleaner.setPhone(phone);
            cleaner.setEmail(email);
            cleaner.setDepartment(department);
            if (cleanerDAO.updateCleaner(cleaner)) {
                JOptionPane.showMessageDialog(this, "Cleaner updated successfully!");
                dispose();
            } else {
                showError("Failed to update cleaner!");
            }
        } else {
            Cleaner newCleaner = new Cleaner();
            newCleaner.setFirstName(firstName);
            newCleaner.setLastName(lastName);
            newCleaner.setPhone(phone);
            newCleaner.setEmail(email);
            newCleaner.setDepartment(department);
            if (cleanerDAO.addCleaner(newCleaner)) {
                JOptionPane.showMessageDialog(this, "Cleaner added successfully!");
                dispose();
            } else {
                showError("Failed to add cleaner!");
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }
}
