package za.ac.belgiumcampus.cleaninginventory.view;

import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.model.Supplier;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.MaterialDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.SupplierDAOImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MaterialFrame extends JFrame {
    private JTextField txtSearch;
    private JTable tableMaterials;
    private DefaultTableModel tableModel;
    private MaterialDAOImpl materialDAO;
    private SupplierDAOImpl supplierDAO;

    public MaterialFrame() {
        materialDAO = new MaterialDAOImpl();
        supplierDAO = new SupplierDAOImpl();
        initComponents();
        loadTableData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Manage Materials");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
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

        String[] columns = {"ID", "Material Name", "Description", "Quantity", "Unit", "Reorder Level", "Supplier"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMaterials = new JTable(tableModel);
        tableMaterials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMaterials.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tableMaterials);
        scrollPane.setPreferredSize(new Dimension(950, 400));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Add Material");
        JButton btnEdit = new JButton("Edit Material");
        JButton btnDelete = new JButton("Delete Material");
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

        btnAdd.addActionListener(e -> openMaterialDialog(null));
        btnEdit.addActionListener(e -> editMaterial());
        btnDelete.addActionListener(e -> deleteMaterial());
        btnSearch.addActionListener(e -> searchMaterials());
        btnRefresh.addActionListener(e -> loadTableData());
        btnBack.addActionListener(e -> dispose());
        txtSearch.addActionListener(e -> searchMaterials());
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Material> materials = materialDAO.getAllMaterials();
        for (Material m : materials) {
            Supplier supplier = supplierDAO.getSupplierById(m.getSupplierId());
            String supplierName = (supplier != null) ? supplier.getSupplierName() : "N/A";
            tableModel.addRow(new Object[]{
                m.getMaterialId(),
                m.getMaterialName(),
                m.getDescription(),
                m.getQuantity(),
                m.getUnit(),
                m.getReorderLevel(),
                supplierName
            });
        }
    }

    private void searchMaterials() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }
        tableModel.setRowCount(0);
        List<Material> materials = materialDAO.searchMaterials(keyword);
        for (Material m : materials) {
            Supplier supplier = supplierDAO.getSupplierById(m.getSupplierId());
            String supplierName = (supplier != null) ? supplier.getSupplierName() : "N/A";
            tableModel.addRow(new Object[]{
                m.getMaterialId(),
                m.getMaterialName(),
                m.getDescription(),
                m.getQuantity(),
                m.getUnit(),
                m.getReorderLevel(),
                supplierName
            });
        }
    }

    private void openMaterialDialog(Material material) {
        MaterialDialog dialog = new MaterialDialog(this, material);
        dialog.setVisible(true);
        loadTableData();
    }

    private void editMaterial() {
        int selectedRow = tableMaterials.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a material to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long materialId = (long) tableModel.getValueAt(selectedRow, 0);
        Material material = materialDAO.getMaterialById(materialId);
        if (material != null) {
            openMaterialDialog(material);
        }
    }

    private void deleteMaterial() {
        int selectedRow = tableMaterials.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a material to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long materialId = (long) tableModel.getValueAt(selectedRow, 0);
        String materialName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete material: " + materialName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (materialDAO.deleteMaterial(materialId)) {
                JOptionPane.showMessageDialog(this, "Material deleted successfully!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete material!", "Error", JOptionPane.ERROR_MESSAGE);
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
