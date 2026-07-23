package za.ac.belgiumcampus.cleaninginventory.dao.impl;

import za.ac.belgiumcampus.cleaninginventory.dao.CleanerDAO;
import za.ac.belgiumcampus.cleaninginventory.database.DBConnection;
import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CleanerDAOImpl implements CleanerDAO {

    private static final String INSERT_SQL =
            "INSERT INTO cleaners (first_name, last_name, phone, email, department) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL =
            "UPDATE cleaners SET first_name = ?, last_name = ?, phone = ?, email = ?, department = ? WHERE cleaner_id = ?";
    private static final String DELETE_SQL = "DELETE FROM cleaners WHERE cleaner_id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM cleaners WHERE cleaner_id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM cleaners ORDER BY cleaner_id";

    @Override
    public void addCleaner(Cleaner cleaner) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cleaner.getFirstName());
            stmt.setString(2, cleaner.getLastName());
            stmt.setString(3, cleaner.getPhone());
            stmt.setString(4, cleaner.getEmail());
            stmt.setString(5, cleaner.getDepartment());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    cleaner.setCleanerId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add cleaner: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateCleaner(Cleaner cleaner) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, cleaner.getFirstName());
            stmt.setString(2, cleaner.getLastName());
            stmt.setString(3, cleaner.getPhone());
            stmt.setString(4, cleaner.getEmail());
            stmt.setString(5, cleaner.getDepartment());
            stmt.setLong(6, cleaner.getCleanerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update cleaner: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCleaner(long cleanerId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, cleanerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete cleaner: " + e.getMessage(), e);
        }
    }

    @Override
    public Cleaner getCleanerById(long cleanerId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1, cleanerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get cleaner by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cleaner> getAllCleaners() {
        List<Cleaner> cleaners = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                cleaners.add(mapRow(rs));
            }
            return cleaners;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all cleaners: " + e.getMessage(), e);
        }
    }

    private Cleaner mapRow(ResultSet rs) throws SQLException {
        Cleaner cleaner = new Cleaner();
        cleaner.setCleanerId(rs.getLong("cleaner_id"));
        cleaner.setFirstName(rs.getString("first_name"));
        cleaner.setLastName(rs.getString("last_name"));
        cleaner.setPhone(rs.getString("phone"));
        cleaner.setEmail(rs.getString("email"));
        cleaner.setDepartment(rs.getString("department"));
        return cleaner;
    }
}
