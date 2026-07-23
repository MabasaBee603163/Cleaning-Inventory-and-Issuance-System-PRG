package za.ac.belgiumcampus.cleaninginventory.ui;

import za.ac.belgiumcampus.cleaninginventory.service.AuthenticationService;
import za.ac.belgiumcampus.cleaninginventory.service.InventoryService;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.CleanerDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.StockIssueDAOImpl;
import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private AuthenticationService authService;
    private InventoryService inventoryService;
    private CleanerDAOImpl cleanerDAO;
    private StockIssueDAOImpl stockIssueDAO;
    private JLabel lblTotalMaterials, lblLowStock, lblTotalCleaners, lblTotalIssuances;

    public DashboardFrame(AuthenticationService authService) {
        this.authService = authService;
        this.inventoryService = new InventoryService();
        this.cleanerDAO = new CleanerDAOImpl();
        this.stockIssueDAO = new StockIssueDAOImpl();
        initComponents();
        loadDashboardData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Cleaning Inventory System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(900, 80));

        JLabel lblHeader = new JLabel("Cleaning Inventory System");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 28));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);

        JLabel lblUser = new JLabel("Welcome, " + authService.getCurrentUser().getFullName());
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUser.setForeground(new Color(200, 220, 255));
        headerPanel.add(lblUser);

        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        statsPanel.setBackground(new Color(240, 248, 255));

        statsPanel.add(createStatPanel("Total Materials", "0", new Color(52, 152, 219)));
        statsPanel.add(createStatPanel("Low Stock Items", "0", new Color(231, 76, 60)));
        statsPanel.add(createStatPanel("Total Cleaners", "0", new Color(46, 204, 113)));
        statsPanel.add(createStatPanel("Total Issuances", "0", new Color(155, 89, 182)));

        mainPanel.add(statsPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton btnMaterials = createNavButton("Manage Materials", new Color(52, 152, 219));
        JButton btnSuppliers = createNavButton("Manage Suppliers", new Color(155, 89, 182));
        JButton btnCleaners = createNavButton("Manage Cleaners", new Color(46, 204, 113));
        JButton btnIssuance = createNavButton("Stock Issuance", new Color(243, 156, 18));
        JButton btnReports = createNavButton("Reports", new Color(231, 76, 60));
        JButton btnLogout = createNavButton("Logout", new Color(149, 165, 166));

        buttonPanel.add(btnMaterials);
        buttonPanel.add(btnSuppliers);
        buttonPanel.add(btnCleaners);
        buttonPanel.add(btnIssuance);
        buttonPanel.add(btnReports);
        buttonPanel.add(btnLogout);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        btnMaterials.addActionListener(e -> {
            MaterialFrame materialFrame = new MaterialFrame();
            materialFrame.setVisible(true);
        });

        btnSuppliers.addActionListener(e -> {
            SupplierFrame supplierFrame = new SupplierFrame();
            supplierFrame.setVisible(true);
        });

        btnCleaners.addActionListener(e -> {
            CleanerFrame cleanerFrame = new CleanerFrame();
            cleanerFrame.setVisible(true);
        });

        btnIssuance.addActionListener(e -> {
            StockIssueFrame stockIssueFrame = new StockIssueFrame();
            stockIssueFrame.setVisible(true);
        });

        btnReports.addActionListener(e -> {
            ReportFrame reportFrame = new ReportFrame();
            reportFrame.setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                authService.logout();
                new LoginFrame().setVisible(true);
                this.dispose();
            }
        });
    }

    private JPanel createStatPanel(String title, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);
        panel.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);
        panel.add(valueLabel, BorderLayout.CENTER);

        if (title.equals("Total Materials")) {
            lblTotalMaterials = valueLabel;
        } else if (title.equals("Low Stock Items")) {
            lblLowStock = valueLabel;
        } else if (title.equals("Total Cleaners")) {
            lblTotalCleaners = valueLabel;
        } else if (title.equals("Total Issuances")) {
            lblTotalIssuances = valueLabel;
        }

        return panel;
    }

    private JButton createNavButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadDashboardData() {
        lblTotalMaterials.setText(String.valueOf(inventoryService.getTotalMaterials()));
        lblLowStock.setText(String.valueOf(inventoryService.getLowStockCount()));
        lblTotalCleaners.setText(String.valueOf(cleanerDAO.getTotalCleaners()));
        lblTotalIssuances.setText(String.valueOf(stockIssueDAO.getTotalIssuances()));
    }
}
