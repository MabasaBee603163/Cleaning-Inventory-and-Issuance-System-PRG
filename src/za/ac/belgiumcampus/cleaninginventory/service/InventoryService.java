package za.ac.belgiumcampus.cleaninginventory.service;

import za.ac.belgiumcampus.cleaninginventory.dao.CleanerDAO;
import za.ac.belgiumcampus.cleaninginventory.dao.MaterialDAO;
import za.ac.belgiumcampus.cleaninginventory.dao.StockIssueDAO;
import za.ac.belgiumcampus.cleaninginventory.dao.SupplierDAO;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.CleanerDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.MaterialDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.StockIssueDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.SupplierDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;
import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueHeader;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueItem;
import za.ac.belgiumcampus.cleaninginventory.model.Supplier;
import za.ac.belgiumcampus.cleaninginventory.utils.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Business rules for suppliers, materials, cleaners, and stock issuing.
 */
public class InventoryService {

    private final SupplierDAO supplierDAO;
    private final MaterialDAO materialDAO;
    private final CleanerDAO cleanerDAO;
    private final StockIssueDAO stockIssueDAO;

    public InventoryService() {
        this(new SupplierDAOImpl(), new MaterialDAOImpl(), new CleanerDAOImpl(), new StockIssueDAOImpl());
    }

    public InventoryService(SupplierDAO supplierDAO, MaterialDAO materialDAO,
                            CleanerDAO cleanerDAO, StockIssueDAO stockIssueDAO) {
        this.supplierDAO = supplierDAO;
        this.materialDAO = materialDAO;
        this.cleanerDAO = cleanerDAO;
        this.stockIssueDAO = stockIssueDAO;
    }

    // --- Suppliers ---

    public void addSupplier(Supplier supplier) {
        Validator.requireNotNull(supplier, "Supplier");
        Validator.requireNonBlank(supplier.getSupplierName(), "Supplier name");
        supplierDAO.addSupplier(supplier);
    }

    public void updateSupplier(Supplier supplier) {
        Validator.requireNotNull(supplier, "Supplier");
        Validator.requireId(supplier.getSupplierId(), "Supplier id");
        Validator.requireNonBlank(supplier.getSupplierName(), "Supplier name");
        supplierDAO.updateSupplier(supplier);
    }

    public void deleteSupplier(long supplierId) {
        Validator.requireId(supplierId, "Supplier id");
        supplierDAO.deleteSupplier(supplierId);
    }

    public Supplier getSupplier(long supplierId) {
        return supplierDAO.getSupplierById(supplierId);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    // --- Materials ---

    public void addMaterial(Material material) {
        Validator.requireNotNull(material, "Material");
        Validator.requireNonBlank(material.getMaterialName(), "Material name");
        Validator.requireNonNegative(material.getQuantity(), "Quantity");
        Validator.requireNonNegative(material.getReorderLevel(), "Reorder level");
        materialDAO.addMaterial(material);
    }

    public void updateMaterial(Material material) {
        Validator.requireNotNull(material, "Material");
        Validator.requireId(material.getMaterialId(), "Material id");
        Validator.requireNonBlank(material.getMaterialName(), "Material name");
        Validator.requireNonNegative(material.getQuantity(), "Quantity");
        Validator.requireNonNegative(material.getReorderLevel(), "Reorder level");
        materialDAO.updateMaterial(material);
    }

    public void deleteMaterial(long materialId) {
        Validator.requireId(materialId, "Material id");
        materialDAO.deleteMaterial(materialId);
    }

    public Material getMaterial(long materialId) {
        return materialDAO.getMaterialById(materialId);
    }

    public List<Material> getAllMaterials() {
        return materialDAO.getAllMaterials();
    }

    public List<Material> getLowStockMaterials() {
        return materialDAO.getMaterialsBelowReorderLevel();
    }

    // --- Cleaners ---

    public void addCleaner(Cleaner cleaner) {
        Validator.requireNotNull(cleaner, "Cleaner");
        Validator.requireNonBlank(cleaner.getFirstName(), "First name");
        Validator.requireNonBlank(cleaner.getLastName(), "Last name");
        cleanerDAO.addCleaner(cleaner);
    }

    public void updateCleaner(Cleaner cleaner) {
        Validator.requireNotNull(cleaner, "Cleaner");
        Validator.requireId(cleaner.getCleanerId(), "Cleaner id");
        Validator.requireNonBlank(cleaner.getFirstName(), "First name");
        Validator.requireNonBlank(cleaner.getLastName(), "Last name");
        cleanerDAO.updateCleaner(cleaner);
    }

    public void deleteCleaner(long cleanerId) {
        Validator.requireId(cleanerId, "Cleaner id");
        cleanerDAO.deleteCleaner(cleanerId);
    }

    public Cleaner getCleaner(long cleanerId) {
        return cleanerDAO.getCleanerById(cleanerId);
    }

    public List<Cleaner> getAllCleaners() {
        return cleanerDAO.getAllCleaners();
    }

    // --- Stock issue ---

    /**
     * Issues stock to a cleaner: validates availability, creates the issue record,
     * and reduces material quantities in one transaction (handled by DAO).
     *
     * @return materials that are at or below reorder level after the issue
     */
    public List<Material> issueStock(StockIssueHeader stockIssue) {
        Validator.requireNotNull(stockIssue, "Stock issue");
        Validator.requireId(stockIssue.getCleanerId(), "Cleaner id");
        Validator.requireId(stockIssue.getUserId(), "User id");
        Validator.requireNotNull(stockIssue.getItems(), "Issue items");

        if (stockIssue.getItems().isEmpty()) {
            throw new IllegalArgumentException("At least one issue item is required");
        }

        if (cleanerDAO.getCleanerById(stockIssue.getCleanerId()) == null) {
            throw new IllegalArgumentException("Cleaner not found");
        }

        for (StockIssueItem item : stockIssue.getItems()) {
            Validator.requireId(item.getMaterialId(), "Material id");
            Validator.requirePositive(item.getQuantity(), "Item quantity");

            Material material = materialDAO.getMaterialById(item.getMaterialId());
            if (material == null) {
                throw new IllegalArgumentException("Material not found: " + item.getMaterialId());
            }
            if (material.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for " + material.getMaterialName()
                                + " (available: " + material.getQuantity()
                                + ", requested: " + item.getQuantity() + ")");
            }
        }

        stockIssueDAO.createStockIssue(stockIssue);

        List<Material> lowStockWarnings = new ArrayList<>();
        for (StockIssueItem item : stockIssue.getItems()) {
            Material updated = materialDAO.getMaterialById(item.getMaterialId());
            if (updated != null && updated.getQuantity() <= updated.getReorderLevel()) {
                lowStockWarnings.add(updated);
            }
        }
        return lowStockWarnings;
    }

    public StockIssueHeader getStockIssue(long issueId) {
        return stockIssueDAO.getStockIssueById(issueId);
    }

    public List<StockIssueHeader> getAllStockIssues() {
        return stockIssueDAO.getAllStockIssues();
    }
}
