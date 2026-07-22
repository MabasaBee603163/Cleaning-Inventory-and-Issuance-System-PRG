package za.ac.belgiumcampus.cleaninginventory.ui;

import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;
import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueHeader;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueItem;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.CleanerDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.MaterialDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.StockIssueDAOImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StockIssueFrame extends JFrame {
    private JComboBox<Cleaner> cmbCleaner;
    private JComboBox<Material> cmbMaterial;
    private JTextField txtQuantity;
    private JTextArea txtRemarks;
    private JTable tableItems;
    private JTable tableIssuances;
    private DefaultTableModel itemsTableModel;
    private DefaultTableModel issuancesTableModel;
    private JLabel lblStatus;
    private MaterialDAOImpl materialDAO;
    private CleanerDAOImpl cleanerDAO;
    private StockIssueDAOImpl stockIssueDAO;
    private List<StockIssueItem> currentItems;

    public StockIssueFrame() {
        materialDAO = new MaterialDAOImpl();
        cleanerDAO = new CleanerDAOImpl();
        stockIssueDAO = new StockIssueDAOImpl();
        currentItems = new ArrayList<>();
        initComponents();
        loadCombos();
        loadIssuanceTable();
        updateItemTable();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Stock Issuance Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel issuePanel = new JPanel(new GridBagLayout());
        issuePanel.setBorder(BorderFactory.createTitledBorder("Issue Stock to Cleaner"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        issuePanel.add(new JLabel("Select Cleaner:"), gbc);
        cmbCleaner = new JComboBox<>();
        cmbCleaner.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        issuePanel.add(cmbCleaner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        issuePanel.add(new JLabel("Select Material:"), gbc);
        cmbMaterial = new JComboBox<>();
        cmbMaterial.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        issuePanel.add(cmbMaterial, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        issuePanel.add(new JLabel("Quantity:"), gbc);
        txtQuantity = new JTextField(10);
        txtQuantity.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 1;
        issuePanel.add(txtQuantity, gbc);

        JButton btnAddItem = new JButton("Add to List");
        styleButton(btnAddItem, new Color(46, 204, 113));
        gbc.gridx = 2;
        gbc.gridy = 2;
        issuePanel.add(btnAddItem, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        issuePanel.add(new JLabel("Remarks:"), gbc);
        txtRemarks = new JTextArea(2, 30);
        txtRemarks.setLineWrap(true);
        txtRemarks.setWrapStyleWord(true);
        JScrollPane remarksScroll = new JScrollPane(txtRemarks);
        remarksScroll.setPreferredSize(new Dimension(300, 50));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        issuePanel.add(remarksScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        String[] itemsColumns = {"Material", "Quantity", "Available Stock"};
        itemsTableModel = new DefaultTableModel(itemsColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableItems = new JTable(itemsTableModel);
        tableItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane itemsScroll = new JScrollPane(tableItems);
        itemsScroll.setPreferredSize(new Dimension(500, 100));
        issuePanel.add(itemsScroll, gbc);

        JButton btnRemoveItem = new JButton("Remove Selected");
        styleButton(btnRemoveItem, new Color(231, 76, 60));
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        issuePanel.add(btnRemoveItem, gbc);

        JButton btnIssueStock = new JButton("Issue Stock");
        styleButton(btnIssueStock, new Color(52, 152, 219));
        gbc.gridx = 2;
        gbc.gridy = 5;
        issuePanel.add(btnIssueStock, gbc);

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        issuePanel.add(lblStatus, gbc);

        mainPanel.add(issuePanel, BorderLayout.NORTH);

        JPanel historyPanel = new JPanel(new BorderLayout(5, 5));
        historyPanel.setBorder(BorderFactory.createTitledBorder("Issuance History"));

        String[] historyColumns = {"Issue ID", "Cleaner", "User", "Date", "Remarks", "Items"};
        issuancesTableModel = new DefaultTableModel(historyColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableIssuances = new JTable(issuancesTableModel);
        tableIssuances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane historyScroll = new JScrollPane(tableIssuances);
        historyScroll.setPreferredSize(new Dimension(900, 200));
        historyPanel.add(historyScroll, BorderLayout.CENTER);

        JPanel historyButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefresh = new JButton("Refresh History");
        JButton btnBack = new JButton("Back to Dashboard");
        styleButton(btnRefresh, new Color(149, 165, 166));
        styleButton(btnBack, new Color(155, 89, 182));
        historyButtonPanel.add(btnRefresh);
        historyButtonPanel.add(btnBack);
        historyPanel.add(historyButtonPanel, BorderLayout.SOUTH);

        mainPanel.add(historyPanel, BorderLayout.CENTER);
        add(mainPanel);

        btnAddItem.addActionListener(e -> addItemToIssue());
        btnRemoveItem.addActionListener(e -> removeItemFromIssue());
        btnIssueStock.addActionListener(e -> issueStock());
        btnRefresh.addActionListener(e -> {
            loadIssuanceTable();
            loadCombos();
        });
        btnBack.addActionListener(e -> dispose());
        txtQuantity.addActionListener(e -> addItemToIssue());
    }

    private void loadCombos() {
        cmbCleaner.removeAllItems();
        cmbCleaner.addItem(null);
        for (Cleaner c : cleanerDAO.getAllCleaners()) {
            cmbCleaner.addItem(c);
        }
        cmbMaterial.removeAllItems();
        cmbMaterial.addItem(null);
        for (Material m : materialDAO.getAllMaterials()) {
            cmbMaterial.addItem(m);
        }
    }

    private void loadIssuanceTable() {
        issuancesTableModel.setRowCount(0);
        for (StockIssueHeader header : stockIssueDAO.getAllIssuances()) {
            List<StockIssueItem> items = stockIssueDAO.getIssueItems(header.getIssueId());
            issuancesTableModel.addRow(new Object[]{
                header.getIssueId(),
                header.getCleanerName(),
                header.getUserName(),
                header.getIssueDate() != null ? header.getIssueDate().toString() : "",
                header.getRemarks() != null ? header.getRemarks() : "",
                items.size()
            });
        }
    }

    private void updateItemTable() {
        itemsTableModel.setRowCount(0);
        for (StockIssueItem item : currentItems) {
            Material material = materialDAO.getMaterialById(item.getMaterialId());
            int availableStock = material != null ? material.getQuantity() : 0;
            itemsTableModel.addRow(new Object[]{
                material != null ? material.getMaterialName() : "Unknown",
                item.getQuantity(),
                availableStock
            });
        }
    }

    private void addItemToIssue() {
        Material selectedMaterial = (Material) cmbMaterial.getSelectedItem();
        String quantityStr = txtQuantity.getText().trim();

        if (selectedMaterial == null) {
            lblStatus.setText("Please select a material!");
            return;
        }
        if (quantityStr.isEmpty()) {
            lblStatus.setText("Please enter quantity!");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                lblStatus.setText("Quantity must be positive!");
                return;
            }
            if (quantity > selectedMaterial.getQuantity()) {
                lblStatus.setText("Insufficient stock! Available: " + selectedMaterial.getQuantity());
                return;
            }
            for (StockIssueItem item : currentItems) {
                if (item.getMaterialId() == selectedMaterial.getMaterialId()) {
                    lblStatus.setText("Material already in list!");
                    return;
                }
            }

            StockIssueItem item = new StockIssueItem();
            item.setMaterialId(selectedMaterial.getMaterialId());
            item.setQuantity(quantity);
            item.setMaterialName(selectedMaterial.getMaterialName());
            currentItems.add(item);
            updateItemTable();
            txtQuantity.setText("");
            lblStatus.setText("Item added successfully!");
        } catch (NumberFormatException e) {
            lblStatus.setText("Quantity must be a number!");
        }
    }

    private void removeItemFromIssue() {
        int selectedRow = tableItems.getSelectedRow();
        if (selectedRow < 0) {
            lblStatus.setText("Please select an item to remove!");
            return;
        }
        currentItems.remove(selectedRow);
        updateItemTable();
        lblStatus.setText("Item removed.");
    }

    private void issueStock() {
        Cleaner selectedCleaner = (Cleaner) cmbCleaner.getSelectedItem();
        String remarks = txtRemarks.getText().trim();

        if (selectedCleaner == null) {
            lblStatus.setText("Please select a cleaner!");
            return;
        }
        if (currentItems.isEmpty()) {
            lblStatus.setText("Please add at least one item to issue!");
            return;
        }

        long userId = 1;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Issue " + currentItems.size() + " item(s) to " + selectedCleaner.getFullName() + "?",
            "Confirm Issuance",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (stockIssueDAO.issueMaterials(selectedCleaner.getCleanerId(), userId, currentItems, remarks)) {
                JOptionPane.showMessageDialog(this, "Stock issued successfully!");
                currentItems.clear();
                updateItemTable();
                txtRemarks.setText("");
                loadCombos();
                loadIssuanceTable();
                lblStatus.setText("Issuance completed successfully!");
            } else {
                lblStatus.setText("Failed to issue stock! Check stock availability.");
                JOptionPane.showMessageDialog(this, "Failed to issue stock!", "Error", JOptionPane.ERROR_MESSAGE);
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
