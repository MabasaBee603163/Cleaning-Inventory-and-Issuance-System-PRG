package za.ac.belgiumcampus.cleaninginventory.ui;

import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueHeader;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueItem;
import za.ac.belgiumcampus.cleaninginventory.model.Supplier;
import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.MaterialDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.StockIssueDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.SupplierDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.CleanerDAOImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportFrame extends JFrame {
    private JComboBox<String> cmbReportType;
    private JTable tableReport;
    private DefaultTableModel tableModel;
    private JLabel lblReportTitle;
    private JLabel lblSummary;
    private MaterialDAOImpl materialDAO;
    private SupplierDAOImpl supplierDAO;
    private CleanerDAOImpl cleanerDAO;
    private StockIssueDAOImpl stockIssueDAO;

    public ReportFrame() {
        materialDAO = new MaterialDAOImpl();
        supplierDAO = new SupplierDAOImpl();
        cleanerDAO = new CleanerDAOImpl();
        stockIssueDAO = new StockIssueDAOImpl();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Report Controls"));
        topPanel.add(new JLabel("Report Type:"));
        cmbReportType = new JComboBox<>(new String[]{
            "Inventory Report",
            "Low Stock Report",
            "Supplier List",
            "Cleaner List",
            "Issuance History",
            "Detailed Issuance Report"
        });
        cmbReportType.setPreferredSize(new Dimension(200, 30));
        topPanel.add(cmbReportType);

        JButton btnGenerate = new JButton("Generate Report");
        styleButton(btnGenerate, new Color(52, 152, 219));
        topPanel.add(btnGenerate);

        JButton btnExport = new JButton("Export to CSV");
        styleButton(btnExport, new Color(46, 204, 113));
        topPanel.add(btnExport);

        JButton btnRefresh = new JButton("Refresh");
        styleButton(btnRefresh, new Color(149, 165, 166));
        topPanel.add(btnRefresh);

        JButton btnBack = new JButton("Back to Dashboard");
        styleButton(btnBack, new Color(155, 89, 182));
        topPanel.add(btnBack);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel reportPanel = new JPanel(new BorderLayout(5, 5));
        reportPanel.setBorder(BorderFactory.createTitledBorder("Report Data"));

        lblReportTitle = new JLabel("Select a report type and click Generate");
        lblReportTitle.setFont(new Font("Arial", Font.BOLD, 14));
        reportPanel.add(lblReportTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableReport = new JTable(tableModel);
        tableReport.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tableReport);
        scrollPane.setPreferredSize(new Dimension(1000, 450));
        reportPanel.add(scrollPane, BorderLayout.CENTER);

        lblSummary = new JLabel(" ");
        lblSummary.setFont(new Font("Arial", Font.ITALIC, 12));
        reportPanel.add(lblSummary, BorderLayout.SOUTH);

        mainPanel.add(reportPanel, BorderLayout.CENTER);
        add(mainPanel);

        btnGenerate.addActionListener(e -> generateReport());
        btnExport.addActionListener(e -> exportToCSV());
        btnRefresh.addActionListener(e -> {
            if (tableModel.getRowCount() > 0) {
                generateReport();
            }
        });
        btnBack.addActionListener(e -> dispose());
        cmbReportType.addActionListener(e -> {
            tableModel.setRowCount(0);
            lblReportTitle.setText(cmbReportType.getSelectedItem() + " - Click Generate to view");
            lblSummary.setText(" ");
        });
    }

    private void generateReport() {
        String selectedReport = (String) cmbReportType.getSelectedItem();
        tableModel.setRowCount(0);

        switch (selectedReport) {
            case "Inventory Report":
                generateInventoryReport();
                break;
            case "Low Stock Report":
                generateLowStockReport();
                break;
            case "Supplier List":
                generateSupplierReport();
                break;
            case "Cleaner List":
                generateCleanerReport();
                break;
            case "Issuance History":
                generateIssuanceHistoryReport();
                break;
            case "Detailed Issuance Report":
                generateDetailedIssuanceReport();
                break;
        }
    }

    private void generateInventoryReport() {
        String[] columns = {"ID", "Material Name", "Description", "Quantity", "Unit", "Reorder Level", "Supplier", "Status"};
        tableModel.setDataVector(new Object[][]{}, columns);

        for (Material m : materialDAO.getAllMaterials()) {
            String status = m.isLowStock() ? "LOW STOCK" : "OK";
            tableModel.addRow(new Object[]{
                m.getMaterialId(),
                m.getMaterialName(),
                m.getDescription() != null ? m.getDescription() : "",
                m.getQuantity(),
                m.getUnit() != null ? m.getUnit() : "",
                m.getReorderLevel(),
                m.getSupplierName() != null ? m.getSupplierName() : "N/A",
                status
            });
        }

        long lowStockCount = materialDAO.getAllMaterials().stream().filter(Material::isLowStock).count();
        lblReportTitle.setText("Inventory Report - Total: " + tableModel.getRowCount() + " materials, " + lowStockCount + " low stock items");
        lblSummary.setText("Generated: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void generateLowStockReport() {
        String[] columns = {"ID", "Material Name", "Quantity", "Reorder Level", "Unit", "Supplier", "Shortage"};
        tableModel.setDataVector(new Object[][]{}, columns);

        for (Material m : materialDAO.getLowStockMaterials()) {
            int shortage = m.getReorderLevel() - m.getQuantity();
            tableModel.addRow(new Object[]{
                m.getMaterialId(),
                m.getMaterialName(),
                m.getQuantity(),
                m.getReorderLevel(),
                m.getUnit() != null ? m.getUnit() : "",
                m.getSupplierName() != null ? m.getSupplierName() : "N/A",
                shortage
            });
        }

        lblReportTitle.setText("Low Stock Report - " + tableModel.getRowCount() + " items below reorder level");
        lblSummary.setText("Generated: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void generateSupplierReport() {
        String[] columns = {"ID", "Supplier Name", "Contact Person", "Phone", "Email", "Address"};
        tableModel.setDataVector(new Object[][]{}, columns);

        for (Supplier s : supplierDAO.getAllSuppliers()) {
            tableModel.addRow(new Object[]{
                s.getSupplierId(),
                s.getSupplierName(),
                s.getContactPerson() != null ? s.getContactPerson() : "",
                s.getPhone() != null ? s.getPhone() : "",
                s.getEmail() != null ? s.getEmail() : "",
                s.getAddress() != null ? s.getAddress() : ""
            });
        }

        lblReportTitle.setText("Supplier List - " + tableModel.getRowCount() + " suppliers");
        lblSummary.setText("Generated: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void generateCleanerReport() {
        String[] columns = {"ID", "First Name", "Last Name", "Full Name", "Phone", "Email", "Department"};
        tableModel.setDataVector(new Object[][]{}, columns);

        for (Cleaner c : cleanerDAO.getAllCleaners()) {
            tableModel.addRow(new Object[]{
                c.getCleanerId(),
                c.getFirstName(),
                c.getLastName(),
                c.getFullName(),
                c.getPhone() != null ? c.getPhone() : "",
                c.getEmail() != null ? c.getEmail() : "",
                c.getDepartment() != null ? c.getDepartment() : ""
            });
        }

        lblReportTitle.setText("Cleaner List - " + tableModel.getRowCount() + " cleaners");
        lblSummary.setText("Generated: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void generateIssuanceHistoryReport() {
        String[] columns = {"Issue ID", "Cleaner", "User", "Date", "Items", "Remarks"};
        tableModel.setDataVector(new Object[][]{}, columns);

        for (StockIssueHeader h : stockIssueDAO.getAllIssuances()) {
            List<StockIssueItem> items = stockIssueDAO.getIssueItems(h.getIssueId());
            int totalItems = items.stream().mapToInt(StockIssueItem::getQuantity).sum();
            tableModel.addRow(new Object[]{
                h.getIssueId(),
                h.getCleanerName() != null ? h.getCleanerName() : "N/A",
                h.getUserName() != null ? h.getUserName() : "N/A",
                h.getIssueDate() != null ? h.getIssueDate().toString() : "",
                totalItems,
                h.getRemarks() != null ? h.getRemarks() : ""
            });
        }

        lblReportTitle.setText("Issuance History - " + tableModel.getRowCount() + " issuances");
        lblSummary.setText("Generated: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void generateDetailedIssuanceReport() {
        String[] columns = {"Issue ID", "Issue Date", "Cleaner", "User", "Material", "Quantity", "Remarks"};
        tableModel.setDataVector(new Object[][]{}, columns);

        for (StockIssueHeader h : stockIssueDAO.getAllIssuances()) {
            for (StockIssueItem item : stockIssueDAO.getIssueItems(h.getIssueId())) {
                tableModel.addRow(new Object[]{
                    h.getIssueId(),
                    h.getIssueDate() != null ? h.getIssueDate().toString() : "",
                    h.getCleanerName() != null ? h.getCleanerName() : "N/A",
                    h.getUserName() != null ? h.getUserName() : "N/A",
                    item.getMaterialName() != null ? item.getMaterialName() : "Unknown",
                    item.getQuantity(),
                    h.getRemarks() != null ? h.getRemarks() : ""
                });
            }
        }

        int totalItems = tableModel.getRowCount();
        lblReportTitle.setText("Detailed Issuance Report - " + totalItems + " items issued");
        lblSummary.setText("Generated: " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void exportToCSV() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data to export! Generate a report first.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(cmbReportType.getSelectedItem() + ".csv"));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.FileWriter writer = new java.io.FileWriter(file);

                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.append("\"").append(tableModel.getColumnName(i)).append("\"");
                    if (i < tableModel.getColumnCount() - 1) writer.append(",");
                }
                writer.append("\n");

                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        Object value = tableModel.getValueAt(row, col);
                        String strValue = value != null ? value.toString() : "";
                        writer.append("\"").append(strValue.replace("\"", "\"\"")).append("\"");
                        if (col < tableModel.getColumnCount() - 1) writer.append(",");
                    }
                    writer.append("\n");
                }

                writer.close();
                JOptionPane.showMessageDialog(this, "Report exported successfully to: " + file.getName());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error exporting file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
