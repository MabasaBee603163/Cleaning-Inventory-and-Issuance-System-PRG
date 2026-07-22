package za.ac.belgiumcampus.cleaninginventory.ui;

import za.ac.belgiumcampus.cleaninginventory.model.Supplier;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.SupplierDAOImpl;
import javax.swing.*;
import java.awt.*;

public class SupplierDialog extends JDialog {
    private Supplier supplier;
    private SupplierDAOImpl supplierDAO;
    private boolean isEditMode;
    private JTextField txtSupplierName, txtContactPerson, txtPhone, txtEmail, txtAddress;

    public SupplierDialog(JFrame parent, Supplier supplier) {
        super(parent, "Supplier Details", true);
        this.supplier = supplier;
        this.supplierDAO = new SupplierDAOImpl();
        this.isEditMode = (supplier != null);
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

        JLabel lblTitle = new JLabel(isEditMode ? "Edit Supplier" : "Add New Supplier");
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
        mainPanel.add(new JLabel("Supplier Name:"), gbc);
        txtSupplierName = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtSupplierName, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Contact Person:"), gbc);
        txtContactPerson = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtContactPerson, gbc);

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
        mainPanel.add(new JLabel("Address:"), gbc);
        txtAddress = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtAddress, gbc);

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

        btnSave.addActionListener(e -> saveSupplier());
        btnCancel.addActionListener(e -> dispose());
    }

    private void populateFields() {
        txtSupplierName.setText(supplier.getSupplierName());
        txtContactPerson.setText(supplier.getContactPerson());
        txtPhone.setText(supplier.getPhone());
        txtEmail.setText(supplier.getEmail());
        txtAddress.setText(supplier.getAddress());
    }

    private void saveSupplier() {
        String supplierName = txtSupplierName.getText().trim();
        String contactPerson = txtContactPerson.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();

        if (supplierName.isEmpty()) {
            showError("Supplier name is required!");
            return;
        }

        if (isEditMode) {
            supplier.setSupplierName(supplierName);
            supplier.setContactPerson(contactPerson);
            supplier.setPhone(phone);
            supplier.setEmail(email);
            supplier.setAddress(address);
            if (supplierDAO.updateSupplier(supplier)) {
                JOptionPane.showMessageDialog(this, "Supplier updated successfully!");
                dispose();
            } else {
                showError("Failed to update supplier!");
            }
        } else {
            Supplier newSupplier = new Supplier();
            newSupplier.setSupplierName(supplierName);
            newSupplier.setContactPerson(contactPerson);
            newSupplier.setPhone(phone);
            newSupplier.setEmail(email);
            newSupplier.setAddress(address);
            if (supplierDAO.addSupplier(newSupplier)) {
                JOptionPane.showMessageDialog(this, "Supplier added successfully!");
                dispose();
            } else {
                showError("Failed to add supplier!");
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
