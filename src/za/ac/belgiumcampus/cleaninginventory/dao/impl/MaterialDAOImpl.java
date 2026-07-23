package za.ac.belgiumcampus.cleaninginventory.dao.impl;

import za.ac.belgiumcampus.cleaninginventory.dao.MaterialDAO;
import za.ac.belgiumcampus.cleaninginventory.database.DBConnection;
import za.ac.belgiumcampus.cleaninginventory.model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAOImpl implements MaterialDAO {

    private static final String INSERT_SQL =
            "INSERT INTO materials (material_name, description, quantity, unit, reorder_level, supplier_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE materials SET material_name = ?, description = ?, quantity = ?, unit = ?, "
                    + "reorder_level = ?, supplier_id = ? WHERE material_id = ?";
    private static final String DELETE_SQL = "DELETE FROM materials WHERE material_id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM materials WHERE material_id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM materials ORDER BY material_id";
    private static final String SELECT_BELOW_REORDER_SQL =
            "SELECT * FROM materials WHERE quantity <= reorder_level ORDER BY material_id";
    private static final String REDUCE_QUANTITY_SQL =
            "UPDATE materials SET quantity = quantity - ? WHERE material_id = ? AND quantity >= ?";

    @Override
    public void addMaterial(Material material) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            bindMaterial(stmt, material);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    material.setMaterialId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add material: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateMaterial(Material material) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            bindMaterial(stmt, material);
            stmt.setLong(7, material.getMaterialId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update material: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteMaterial(long materialId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, materialId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete material: " + e.getMessage(), e);
        }
    }

    @Override
    public Material getMaterialById(long materialId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1, materialId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get material by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Material> getAllMaterials() {
        List<Material> materials = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                materials.add(mapRow(rs));
            }
            return materials;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all materials: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Material> getMaterialsBelowReorderLevel() {
        List<Material> materials = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BELOW_REORDER_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                materials.add(mapRow(rs));
            }
            return materials;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get materials below reorder level: " + e.getMessage(), e);
        }
    }

    @Override
    public void reduceQuantity(long materialId, int amount) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(REDUCE_QUANTITY_SQL)) {
            stmt.setInt(1, amount);
            stmt.setLong(2, materialId);
            stmt.setInt(3, amount);
            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new RuntimeException("Insufficient stock or material not found for id " + materialId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reduce material quantity: " + e.getMessage(), e);
        }
    }

    /**
     * Reduces quantity using an existing connection (for transactional stock issues).
     */
    public void reduceQuantity(Connection conn, long materialId, int amount) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(REDUCE_QUANTITY_SQL)) {
            stmt.setInt(1, amount);
            stmt.setLong(2, materialId);
            stmt.setInt(3, amount);
            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new SQLException("Insufficient stock or material not found for id " + materialId);
            }
        }
    }

    private void bindMaterial(PreparedStatement stmt, Material material) throws SQLException {
        stmt.setString(1, material.getMaterialName());
        stmt.setString(2, material.getDescription());
        stmt.setInt(3, material.getQuantity());
        stmt.setString(4, material.getUnit());
        stmt.setInt(5, material.getReorderLevel());
        if (material.getSupplierId() > 0) {
            stmt.setLong(6, material.getSupplierId());
        } else {
            stmt.setObject(6, null);
        }
    }

    private Material mapRow(ResultSet rs) throws SQLException {
        Material material = new Material();
        material.setMaterialId(rs.getLong("material_id"));
        material.setMaterialName(rs.getString("material_name"));
        material.setDescription(rs.getString("description"));
        material.setQuantity(rs.getInt("quantity"));
        material.setUnit(rs.getString("unit"));
        material.setReorderLevel(rs.getInt("reorder_level"));
        long supplierId = rs.getLong("supplier_id");
        if (!rs.wasNull()) {
            material.setSupplierId(supplierId);
        }
        return material;
    }
}
