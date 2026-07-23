package za.ac.belgiumcampus.cleaninginventory.dao.impl;

import za.ac.belgiumcampus.cleaninginventory.dao.StockIssueDAO;
import za.ac.belgiumcampus.cleaninginventory.database.DBConnection;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueHeader;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StockIssueDAOImpl implements StockIssueDAO {

    private static final String INSERT_HEADER_SQL =
            "INSERT INTO stock_issue_headers (cleaner_id, user_id, remarks) VALUES (?, ?, ?)";
    private static final String INSERT_ITEM_SQL =
            "INSERT INTO stock_issue_items (issue_id, material_id, quantity) VALUES (?, ?, ?)";
    private static final String UPDATE_HEADER_SQL =
            "UPDATE stock_issue_headers SET cleaner_id = ?, user_id = ?, remarks = ? WHERE issue_id = ?";
    private static final String DELETE_ITEMS_SQL = "DELETE FROM stock_issue_items WHERE issue_id = ?";
    private static final String DELETE_HEADER_SQL = "DELETE FROM stock_issue_headers WHERE issue_id = ?";
    private static final String SELECT_HEADER_BY_ID_SQL = "SELECT * FROM stock_issue_headers WHERE issue_id = ?";
    private static final String SELECT_ITEMS_BY_ISSUE_SQL =
            "SELECT * FROM stock_issue_items WHERE issue_id = ? ORDER BY issue_item_id";
    private static final String SELECT_ALL_HEADERS_SQL =
            "SELECT * FROM stock_issue_headers ORDER BY issue_id DESC";
    private static final String REDUCE_QUANTITY_SQL =
            "UPDATE materials SET quantity = quantity - ? WHERE material_id = ? AND quantity >= ?";

    @Override
    public void createStockIssue(StockIssueHeader stockIssue) {
        if (stockIssue.getItems() == null || stockIssue.getItems().isEmpty()) {
            throw new IllegalArgumentException("Stock issue must contain at least one item");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            long issueId;
            try (PreparedStatement headerStmt =
                         conn.prepareStatement(INSERT_HEADER_SQL, Statement.RETURN_GENERATED_KEYS)) {
                headerStmt.setLong(1, stockIssue.getCleanerId());
                headerStmt.setLong(2, stockIssue.getUserId());
                headerStmt.setString(3, stockIssue.getRemarks());
                headerStmt.executeUpdate();

                try (ResultSet keys = headerStmt.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("Failed to retrieve generated issue_id");
                    }
                    issueId = keys.getLong(1);
                    stockIssue.setIssueId(issueId);
                }
            }

            try (PreparedStatement itemStmt =
                         conn.prepareStatement(INSERT_ITEM_SQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement reduceStmt = conn.prepareStatement(REDUCE_QUANTITY_SQL)) {
                for (StockIssueItem item : stockIssue.getItems()) {
                    itemStmt.setLong(1, issueId);
                    itemStmt.setLong(2, item.getMaterialId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.executeUpdate();

                    try (ResultSet keys = itemStmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            item.setIssueItemId(keys.getLong(1));
                        }
                    }
                    item.setIssueId(issueId);

                    reduceStmt.setInt(1, item.getQuantity());
                    reduceStmt.setLong(2, item.getMaterialId());
                    reduceStmt.setInt(3, item.getQuantity());
                    int updated = reduceStmt.executeUpdate();
                    if (updated == 0) {
                        throw new SQLException(
                                "Insufficient stock for material id " + item.getMaterialId());
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
            }
            throw new RuntimeException("Failed to create stock issue: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    // ignore close errors after commit/rollback
                }
            }
        }
    }

    @Override
    public void updateStockIssue(StockIssueHeader stockIssue) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_HEADER_SQL)) {
            stmt.setLong(1, stockIssue.getCleanerId());
            stmt.setLong(2, stockIssue.getUserId());
            stmt.setString(3, stockIssue.getRemarks());
            stmt.setLong(4, stockIssue.getIssueId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update stock issue: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteStockIssue(long issueId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement deleteItems = conn.prepareStatement(DELETE_ITEMS_SQL)) {
                deleteItems.setLong(1, issueId);
                deleteItems.executeUpdate();
            }
            try (PreparedStatement deleteHeader = conn.prepareStatement(DELETE_HEADER_SQL)) {
                deleteHeader.setLong(1, issueId);
                deleteHeader.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
            }
            throw new RuntimeException("Failed to delete stock issue: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    // ignore
                }
            }
        }
    }

    @Override
    public StockIssueHeader getStockIssueById(long issueId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement headerStmt = conn.prepareStatement(SELECT_HEADER_BY_ID_SQL)) {
            headerStmt.setLong(1, issueId);
            try (ResultSet rs = headerStmt.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                StockIssueHeader header = mapHeader(rs);
                header.setItems(loadItems(conn, issueId));
                return header;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get stock issue by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<StockIssueHeader> getAllStockIssues() {
        List<StockIssueHeader> issues = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_HEADERS_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                StockIssueHeader header = mapHeader(rs);
                header.setItems(loadItems(conn, header.getIssueId()));
                issues.add(header);
            }
            return issues;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all stock issues: " + e.getMessage(), e);
        }
    }

    private List<StockIssueItem> loadItems(Connection conn, long issueId) throws SQLException {
        List<StockIssueItem> items = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SELECT_ITEMS_BY_ISSUE_SQL)) {
            stmt.setLong(1, issueId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapItem(rs));
                }
            }
        }
        return items;
    }

    private StockIssueHeader mapHeader(ResultSet rs) throws SQLException {
        StockIssueHeader header = new StockIssueHeader();
        header.setIssueId(rs.getLong("issue_id"));
        header.setCleanerId(rs.getLong("cleaner_id"));
        header.setUserId(rs.getLong("user_id"));
        Timestamp issueDate = rs.getTimestamp("issue_date");
        if (issueDate != null) {
            header.setIssueDate(issueDate.toLocalDateTime());
        }
        header.setRemarks(rs.getString("remarks"));
        return header;
    }

    private StockIssueItem mapItem(ResultSet rs) throws SQLException {
        StockIssueItem item = new StockIssueItem();
        item.setIssueItemId(rs.getLong("issue_item_id"));
        item.setIssueId(rs.getLong("issue_id"));
        item.setMaterialId(rs.getLong("material_id"));
        item.setQuantity(rs.getInt("quantity"));
        return item;
    }
}
