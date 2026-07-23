package za.ac.belgiumcampus.cleaninginventory.dao.impl;

import za.ac.belgiumcampus.cleaninginventory.dao.StockIssueDAO;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueHeader;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueItem;
import za.ac.belgiumcampus.cleaninginventory.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockIssueDAOImpl implements StockIssueDAO {

    @Override
    public boolean issueMaterials(long cleanerId, long userId, List<StockIssueItem> items, String remarks) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String checkStockSQL = "SELECT quantity FROM materials WHERE material_id = ?";
            PreparedStatement checkStockStmt = conn.prepareStatement(checkStockSQL);
            for (StockIssueItem item : items) {
                checkStockStmt.setLong(1, item.getMaterialId());
                ResultSet rs = checkStockStmt.executeQuery();
                if (rs.next()) {
                    int currentStock = rs.getInt("quantity");
                    if (currentStock < item.getQuantity()) {
                        throw new SQLException("Insufficient stock for material ID: " + item.getMaterialId());
                    }
                } else {
                    throw new SQLException("Material not found: " + item.getMaterialId());
                }
                rs.close();
            }
            checkStockStmt.close();

            String headerSQL = "INSERT INTO stock_issue_headers (cleaner_id, user_id, remarks) VALUES (?, ?, ?)";
            PreparedStatement headerStmt = conn.prepareStatement(headerSQL, Statement.RETURN_GENERATED_KEYS);
            headerStmt.setLong(1, cleanerId);
            headerStmt.setLong(2, userId);
            headerStmt.setString(3, remarks);
            headerStmt.executeUpdate();

            ResultSet headerKeys = headerStmt.getGeneratedKeys();
            long issueId;
            if (headerKeys.next()) {
                issueId = headerKeys.getLong(1);
            } else {
                throw new SQLException("Failed to get issue ID");
            }
            headerStmt.close();

            String itemSQL = "INSERT INTO stock_issue_items (issue_id, material_id, quantity) VALUES (?, ?, ?)";
            PreparedStatement itemStmt = conn.prepareStatement(itemSQL);

            String updateStockSQL = "UPDATE materials SET quantity = quantity - ? WHERE material_id = ?";
            PreparedStatement updateStockStmt = conn.prepareStatement(updateStockSQL);

            for (StockIssueItem item : items) {
                updateStockStmt.setInt(1, item.getQuantity());
                updateStockStmt.setLong(2, item.getMaterialId());
                updateStockStmt.executeUpdate();

                itemStmt.setLong(1, issueId);
                itemStmt.setLong(2, item.getMaterialId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.addBatch();
            }

            itemStmt.executeBatch();
            itemStmt.close();
            updateStockStmt.close();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<StockIssueHeader> getAllIssuances() {
        List<StockIssueHeader> headers = new ArrayList<>();
        String query = "SELECT h.*, c.first_name || ' ' || c.last_name as cleaner_name, u.full_name as user_name " +
                      "FROM stock_issue_headers h " +
                      "LEFT JOIN cleaners c ON h.cleaner_id = c.cleaner_id " +
                      "LEFT JOIN users u ON h.user_id = u.user_id " +
                      "ORDER BY h.issue_date DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                StockIssueHeader header = mapResultSetToHeader(rs);
                header.setCleanerName(rs.getString("cleaner_name"));
                header.setUserName(rs.getString("user_name"));
                headers.add(header);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return headers;
    }

    @Override
    public List<StockIssueHeader> getRecentIssuances(int limit) {
        List<StockIssueHeader> headers = new ArrayList<>();
        String query = "SELECT h.*, c.first_name || ' ' || c.last_name as cleaner_name, u.full_name as user_name " +
                      "FROM stock_issue_headers h " +
                      "LEFT JOIN cleaners c ON h.cleaner_id = c.cleaner_id " +
                      "LEFT JOIN users u ON h.user_id = u.user_id " +
                      "ORDER BY h.issue_date DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                StockIssueHeader header = mapResultSetToHeader(rs);
                header.setCleanerName(rs.getString("cleaner_name"));
                header.setUserName(rs.getString("user_name"));
                headers.add(header);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return headers;
    }

    @Override
    public StockIssueHeader getIssuanceById(long issueId) {
        String query = "SELECT h.*, c.first_name || ' ' || c.last_name as cleaner_name, u.full_name as user_name " +
                      "FROM stock_issue_headers h " +
                      "LEFT JOIN cleaners c ON h.cleaner_id = c.cleaner_id " +
                      "LEFT JOIN users u ON h.user_id = u.user_id " +
                      "WHERE h.issue_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, issueId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                StockIssueHeader header = mapResultSetToHeader(rs);
                header.setCleanerName(rs.getString("cleaner_name"));
                header.setUserName(rs.getString("user_name"));
                return header;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<StockIssueItem> getIssueItems(long issueId) {
        List<StockIssueItem> items = new ArrayList<>();
        String query = "SELECT i.*, m.material_name FROM stock_issue_items i " +
                      "LEFT JOIN materials m ON i.material_id = m.material_id " +
                      "WHERE i.issue_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, issueId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                StockIssueItem item = new StockIssueItem();
                item.setIssueItemId(rs.getLong("issue_item_id"));
                item.setIssueId(rs.getLong("issue_id"));
                item.setMaterialId(rs.getLong("material_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setMaterialName(rs.getString("material_name"));
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    item.setCreatedAt(timestamp.toLocalDateTime());
                }
                items.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public boolean deleteIssuance(long issueId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String deleteItemsSQL = "DELETE FROM stock_issue_items WHERE issue_id = ?";
            PreparedStatement deleteItemsStmt = conn.prepareStatement(deleteItemsSQL);
            deleteItemsStmt.setLong(1, issueId);
            deleteItemsStmt.executeUpdate();
            deleteItemsStmt.close();

            String deleteHeaderSQL = "DELETE FROM stock_issue_headers WHERE issue_id = ?";
            PreparedStatement deleteHeaderStmt = conn.prepareStatement(deleteHeaderSQL);
            deleteHeaderStmt.setLong(1, issueId);
            int deleted = deleteHeaderStmt.executeUpdate();
            deleteHeaderStmt.close();

            conn.commit();
            return deleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getTotalIssuances() {
        String query = "SELECT COUNT(*) FROM stock_issue_headers";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private StockIssueHeader mapResultSetToHeader(ResultSet rs) throws SQLException {
        StockIssueHeader header = new StockIssueHeader();
        header.setIssueId(rs.getLong("issue_id"));
        header.setCleanerId(rs.getLong("cleaner_id"));
        header.setUserId(rs.getLong("user_id"));
        header.setRemarks(rs.getString("remarks"));
        Timestamp timestamp = rs.getTimestamp("issue_date");
        if (timestamp != null) {
            header.setIssueDate(timestamp.toLocalDateTime());
        }
        return header;
    }
}
