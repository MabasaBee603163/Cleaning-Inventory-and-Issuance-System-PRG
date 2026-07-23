package za.ac.belgiumcampus.cleaninginventory.ui;

import za.ac.belgiumcampus.cleaninginventory.model.Supplier;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.SupplierDAOImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SupplierFrame extends JFrame {
    private JTextField txtSearch;
    private JTable tableSuppliers;
    private DefaultTableModel tableModel;
    private SupplierDAOImpl supplierDAO;

    public SupplierFrame() {
        supplierDAO = new SupplierDAOImpl();
        initComponents();
        loadTableData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Manage Suppliers");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 550);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        txtSearch = new JTextField(20);
        topPanel.add(txtSearch);
        JButton btnSearch = new JButton("Search");
        JButton btnRefresh = new JButton("Refresh");
        topPanel.add(btnSearch);
        topPanel.add(btnRefresh);

        String[] columns = {"ID", "Supplier Name", "Contact Person", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSuppliers = new JTable(tableModel);
        tableSuppliers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableSuppliers);
        scrollPane.setPreferredSize(new Dimension(950, 350));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Add Supplier");
        JButton btnEdit = new JButton("Edit Supplier");
        JButton btnDelete = new JButton("Delete Supplier");
        JButton btnBack = new JButton("Back to Dashboard");

        styleButton(btnAdd, new Color(46, 204, 113));
        styleButton(btnEdit, new Color(52, 152, 219));
        styleButton(btnDelete, new Color(231, 76, 60));
        styleButton(btnRefresh, new Color(149, 165, 166));
        styleButton(btnBack, new Color(155, 89, 182));

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnBack);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnAdd.addActionListener(e -> openSupplierDialog(null));
        btnEdit.addActionListener(e -> editSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());
        btnSearch.addActionListener(e -> searchSuppliers());
        btnRefresh.addActionListener(e -> loadTableData());
        btnBack.addActionListener(e -> dispose());
        txtSearch.addActionListener(e -> searchSuppliers());
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        for (Supplier s : supplierDAO.getAllSuppliers()) {
            tableModel.addRow(new Object[]{
                s.getSupplierId(),
                s.getSupplierName(),
                s.getContactPerson(),
                s.getPhone(),
                s.getEmail(),
                s.getAddress()
            });
        }
    }

    private void searchSuppliers() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }
        tableModel.setRowCount(0);
        for (Supplier s : supplierDAO.searchSuppliers(keyword)) {
            tableModel.addRow(new Object[]{
                s.getSupplierId(),
                s.getSupplierName(),
                s.getContactPerson(),
                s.getPhone(),
                s.getEmail(),
                s.getAddress()
            });
        }
    }

    private void openSupplierDialog(Supplier supplier) {
        SupplierDialog dialog = new SupplierDialog(this, supplier);
        dialog.setVisible(true);
        loadTableData();
    }

    private void editSupplier() {
        int selectedRow = tableSuppliers.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a supplier to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long supplierId = (long) tableModel.getValueAt(selectedRow, 0);
        Supplier supplier = supplierDAO.getSupplierById(supplierId);
        if (supplier != null) {
            openSupplierDialog(supplier);
        }
    }

    private void deleteSupplier() {
        int selectedRow = tableSuppliers.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a supplier to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long supplierId = (long) tableModel.getValueAt(selectedRow, 0);
        String supplierName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete supplier: " + supplierName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (supplierDAO.deleteSupplier(supplierId)) {
                JOptionPane.showMessageDialog(this, "Supplier deleted successfully!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete supplier!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }
}
