package za.ac.belgiumcampus.cleaninginventory.ui;

import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.model.Supplier;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.MaterialDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.SupplierDAOImpl;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MaterialDialog extends JDialog {
    private Material material;
    private MaterialDAOImpl materialDAO;
    private SupplierDAOImpl supplierDAO;
    private boolean isEditMode;
    private JTextField txtMaterialName, txtQuantity, txtUnit, txtReorderLevel;
    private JTextArea txtDescription;
    private JComboBox<Supplier> cmbSupplier;

    public MaterialDialog(JFrame parent, Material material) {
        super(parent, "Material Details", true);
        this.material = material;
        this.materialDAO = new MaterialDAOImpl();
        this.supplierDAO = new SupplierDAOImpl();
        this.isEditMode = (material != null);
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

        JLabel lblTitle = new JLabel(isEditMode ? "Edit Material" : "Add New Material");
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
        mainPanel.add(new JLabel("Material Name:"), gbc);
        txtMaterialName = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtMaterialName, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Description:"), gbc);
        txtDescription = new JTextArea(3, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        gbc.gridx = 1;
        mainPanel.add(descScroll, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Quantity:"), gbc);
        txtQuantity = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtQuantity, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Unit:"), gbc);
        txtUnit = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtUnit, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Reorder Level:"), gbc);
        txtReorderLevel = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtReorderLevel, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Supplier:"), gbc);
        cmbSupplier = new JComboBox<>();
        loadSuppliers();
        gbc.gridx = 1;
        mainPanel.add(cmbSupplier, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnSave = new JButton(isEditMode ? "Update" : "Save");
        JButton btnCancel = new JButton("Cancel");
        styleButton(btnSave, isEditMode ? new Color(52, 152, 219) : new Color(46, 204, 113));
        styleButton(btnCancel, new Color(149, 165, 166));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        btnSave.addActionListener(e -> saveMaterial());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadSuppliers() {
        cmbSupplier.removeAllItems();
        cmbSupplier.addItem(null);
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        for (Supplier s : suppliers) {
            cmbSupplier.addItem(s);
        }
    }

    private void populateFields() {
        txtMaterialName.setText(material.getMaterialName());
        txtDescription.setText(material.getDescription());
        txtQuantity.setText(String.valueOf(material.getQuantity()));
        txtUnit.setText(material.getUnit());
        txtReorderLevel.setText(String.valueOf(material.getReorderLevel()));
        for (int i = 0; i < cmbSupplier.getItemCount(); i++) {
            Supplier s = cmbSupplier.getItemAt(i);
            if (s != null && s.getSupplierId() == material.getSupplierId()) {
                cmbSupplier.setSelectedIndex(i);
                break;
            }
        }
    }

    private void saveMaterial() {
        String name = txtMaterialName.getText().trim();
        String description = txtDescription.getText().trim();
        String quantityStr = txtQuantity.getText().trim();
        String unit = txtUnit.getText().trim();
        String reorderStr = txtReorderLevel.getText().trim();
        Supplier supplier = (Supplier) cmbSupplier.getSelectedItem();

        if (name.isEmpty() || quantityStr.isEmpty() || reorderStr.isEmpty()) {
            showError("Please fill in all required fields!");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            int reorderLevel = Integer.parseInt(reorderStr);
            if (quantity < 0 || reorderLevel < 0) {
                showError("Quantity and reorder level must be positive!");
                return;
            }
            long supplierId = (supplier != null) ? supplier.getSupplierId() : 0;

            if (isEditMode) {
                material.setMaterialName(name);
                material.setDescription(description);
                material.setQuantity(quantity);
                material.setUnit(unit);
                material.setReorderLevel(reorderLevel);
                material.setSupplierId(supplierId);
                if (materialDAO.updateMaterial(material)) {
                    JOptionPane.showMessageDialog(this, "Material updated successfully!");
                    dispose();
                } else {
                    showError("Failed to update material!");
                }
            } else {
                Material newMaterial = new Material();
                newMaterial.setMaterialName(name);
                newMaterial.setDescription(description);
                newMaterial.setQuantity(quantity);
                newMaterial.setUnit(unit);
                newMaterial.setReorderLevel(reorderLevel);
                newMaterial.setSupplierId(supplierId);
                if (materialDAO.addMaterial(newMaterial)) {
                    JOptionPane.showMessageDialog(this, "Material added successfully!");
                    dispose();
                } else {
                    showError("Failed to add material!");
                }
            }
        } catch (NumberFormatException ex) {
            showError("Quantity and reorder level must be numbers!");
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
