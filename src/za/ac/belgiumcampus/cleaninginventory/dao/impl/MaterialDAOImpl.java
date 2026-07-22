package za.ac.belgiumcampus.cleaninginventory.dao.impl;

import za.ac.belgiumcampus.cleaninginventory.dao.MaterialDAO;
import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAOImpl implements MaterialDAO {

    @Override
    public boolean addMaterial(Material material) {
        String query = "INSERT INTO materials (material_name, description, quantity, unit, reorder_level, supplier_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, material.getMaterialName());
            pstmt.setString(2, material.getDescription());
            pstmt.setInt(3, material.getQuantity());
            pstmt.setString(4, material.getUnit());
            pstmt.setInt(5, material.getReorderLevel());
            pstmt.setLong(6, material.getSupplierId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    material.setMaterialId(generatedKeys.getLong(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateMaterial(Material material) {
        String query = "UPDATE materials SET material_name=?, description=?, quantity=?, unit=?, reorder_level=?, supplier_id=? WHERE material_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, material.getMaterialName());
            pstmt.setString(2, material.getDescription());
            pstmt.setInt(3, material.getQuantity());
            pstmt.setString(4, material.getUnit());
            pstmt.setInt(5, material.getReorderLevel());
            pstmt.setLong(6, material.getSupplierId());
            pstmt.setLong(7, material.getMaterialId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteMaterial(long materialId) {
        String query = "DELETE FROM materials WHERE material_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, materialId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Material getMaterialById(long materialId) {
        String query = "SELECT m.*, s.supplier_name FROM materials m LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id WHERE m.material_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, materialId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Material material = mapResultSetToMaterial(rs);
                material.setSupplierName(rs.getString("supplier_name"));
                return material;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Material> getAllMaterials() {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT m.*, s.supplier_name FROM materials m LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id ORDER BY m.material_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Material material = mapResultSetToMaterial(rs);
                material.setSupplierName(rs.getString("supplier_name"));
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    @Override
    public List<Material> searchMaterials(String keyword) {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT m.*, s.supplier_name FROM materials m LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id WHERE LOWER(m.material_name) LIKE LOWER(?) OR LOWER(m.description) LIKE LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Material material = mapResultSetToMaterial(rs);
                material.setSupplierName(rs.getString("supplier_name"));
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    @Override
    public List<Material> getLowStockMaterials() {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT m.*, s.supplier_name FROM materials m LEFT JOIN suppliers s ON m.supplier_id = s.supplier_id WHERE m.quantity < m.reorder_level ORDER BY m.material_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Material material = mapResultSetToMaterial(rs);
                material.setSupplierName(rs.getString("supplier_name"));
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materials;
    }

    @Override
    public boolean updateStock(long materialId, int newQuantity) {
        String query = "UPDATE materials SET quantity = ? WHERE material_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setLong(2, materialId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getTotalMaterials() {
        String query = "SELECT COUNT(*) FROM materials";
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

    @Override
    public int getLowStockCount() {
        String query = "SELECT COUNT(*) FROM materials WHERE quantity < reorder_level";
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

    private Material mapResultSetToMaterial(ResultSet rs) throws SQLException {
        Material material = new Material();
        material.setMaterialId(rs.getLong("material_id"));
        material.setMaterialName(rs.getString("material_name"));
        material.setDescription(rs.getString("description"));
        material.setQuantity(rs.getInt("quantity"));
        material.setUnit(rs.getString("unit"));
        material.setReorderLevel(rs.getInt("reorder_level"));
        material.setSupplierId(rs.getLong("supplier_id"));
        return material;
    }
}
