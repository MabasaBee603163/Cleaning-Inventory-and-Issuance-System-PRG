package za.ac.belgiumcampus.cleaninginventory.dao.impl;

import za.ac.belgiumcampus.cleaninginventory.dao.CleanerDAO;
import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;
import za.ac.belgiumcampus.cleaninginventory.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CleanerDAOImpl implements CleanerDAO {

    @Override
    public boolean addCleaner(Cleaner cleaner) {
        String query = "INSERT INTO cleaners (first_name, last_name, phone, email, department) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cleaner.getFirstName());
            pstmt.setString(2, cleaner.getLastName());
            pstmt.setString(3, cleaner.getPhone());
            pstmt.setString(4, cleaner.getEmail());
            pstmt.setString(5, cleaner.getDepartment());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    cleaner.setCleanerId(generatedKeys.getLong(1));
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
    public boolean updateCleaner(Cleaner cleaner) {
        String query = "UPDATE cleaners SET first_name=?, last_name=?, phone=?, email=?, department=? WHERE cleaner_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, cleaner.getFirstName());
            pstmt.setString(2, cleaner.getLastName());
            pstmt.setString(3, cleaner.getPhone());
            pstmt.setString(4, cleaner.getEmail());
            pstmt.setString(5, cleaner.getDepartment());
            pstmt.setLong(6, cleaner.getCleanerId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCleaner(long cleanerId) {
        String query = "DELETE FROM cleaners WHERE cleaner_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, cleanerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Cleaner getCleanerById(long cleanerId) {
        String query = "SELECT * FROM cleaners WHERE cleaner_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, cleanerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Cleaner cleaner = new Cleaner();
                cleaner.setCleanerId(rs.getLong("cleaner_id"));
                cleaner.setFirstName(rs.getString("first_name"));
                cleaner.setLastName(rs.getString("last_name"));
                cleaner.setPhone(rs.getString("phone"));
                cleaner.setEmail(rs.getString("email"));
                cleaner.setDepartment(rs.getString("department"));
                return cleaner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cleaner> getAllCleaners() {
        List<Cleaner> cleaners = new ArrayList<>();
        String query = "SELECT * FROM cleaners ORDER BY cleaner_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Cleaner cleaner = new Cleaner();
                cleaner.setCleanerId(rs.getLong("cleaner_id"));
                cleaner.setFirstName(rs.getString("first_name"));
                cleaner.setLastName(rs.getString("last_name"));
                cleaner.setPhone(rs.getString("phone"));
                cleaner.setEmail(rs.getString("email"));
                cleaner.setDepartment(rs.getString("department"));
                cleaners.add(cleaner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cleaners;
    }

    @Override
    public List<Cleaner> searchCleaners(String keyword) {
        List<Cleaner> cleaners = new ArrayList<>();
        String query = "SELECT * FROM cleaners WHERE LOWER(first_name) LIKE LOWER(?) OR LOWER(last_name) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) OR LOWER(department) LIKE LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Cleaner cleaner = new Cleaner();
                cleaner.setCleanerId(rs.getLong("cleaner_id"));
                cleaner.setFirstName(rs.getString("first_name"));
                cleaner.setLastName(rs.getString("last_name"));
                cleaner.setPhone(rs.getString("phone"));
                cleaner.setEmail(rs.getString("email"));
                cleaner.setDepartment(rs.getString("department"));
                cleaners.add(cleaner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cleaners;
    }

    @Override
    public int getTotalCleaners() {
        String query = "SELECT COUNT(*) FROM cleaners";
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
}
