package za.ac.belgiumcampus.cleaninginventory.ui

import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.CleanerDAOImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CleanerFrame extends JFrame {
    private JTextField txtSearch;
    private JTable tableCleaners;
    private DefaultTableModel tableModel;
    private CleanerDAOImpl cleanerDAO;

    public CleanerFrame() {
        cleanerDAO = new CleanerDAOImpl();
        initComponents();
        loadTableData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Manage Cleaners");
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

        String[] columns = {"ID", "First Name", "Last Name", "Full Name", "Phone", "Email", "Department"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableCleaners = new JTable(tableModel);
        tableCleaners.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableCleaners);
        scrollPane.setPreferredSize(new Dimension(950, 350));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Add Cleaner");
        JButton btnEdit = new JButton("Edit Cleaner");
        JButton btnDelete = new JButton("Delete Cleaner");
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

        btnAdd.addActionListener(e -> openCleanerDialog(null));
        btnEdit.addActionListener(e -> editCleaner());
        btnDelete.addActionListener(e -> deleteCleaner());
        btnSearch.addActionListener(e -> searchCleaners());
        btnRefresh.addActionListener(e -> loadTableData());
        btnBack.addActionListener(e -> dispose());
        txtSearch.addActionListener(e -> searchCleaners());
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        for (Cleaner c : cleanerDAO.getAllCleaners()) {
            tableModel.addRow(new Object[]{
                c.getCleanerId(),
                c.getFirstName(),
                c.getLastName(),
                c.getFullName(),
                c.getPhone(),
                c.getEmail(),
                c.getDepartment()
            });
        }
    }

    private void searchCleaners() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadTableData();
            return;
        }
        tableModel.setRowCount(0);
        for (Cleaner c : cleanerDAO.searchCleaners(keyword)) {
            tableModel.addRow(new Object[]{
                c.getCleanerId(),
                c.getFirstName(),
                c.getLastName(),
                c.getFullName(),
                c.getPhone(),
                c.getEmail(),
                c.getDepartment()
            });
        }
    }

    private void openCleanerDialog(Cleaner cleaner) {
        CleanerDialog dialog = new CleanerDialog(this, cleaner);
        dialog.setVisible(true);
        loadTableData();
    }

    private void editCleaner() {
        int selectedRow = tableCleaners.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a cleaner to edit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        long cleanerId = (long) tableModel.getValueAt(selectedRow, 0);
        Cleaner cleaner = cleanerDAO.getCleanerById(cleanerId);
        if (cleaner != null) {
            openCleanerDialog(cleaner);
        }
    }

    private void deleteCleaner() {
        int selectedRow = tableCleaners.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a cleaner to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long cleanerId = (long) tableModel.getValueAt(selectedRow, 0);
        String fullName = (String) tableModel.getValueAt(selectedRow, 3);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete cleaner: " + fullName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (cleanerDAO.deleteCleaner(cleanerId)) {
                JOptionPane.showMessageDialog(this, "Cleaner deleted successfully!");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete cleaner!", "Error", JOptionPane.ERROR_MESSAGE);
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
