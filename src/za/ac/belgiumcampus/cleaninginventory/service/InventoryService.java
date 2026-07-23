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
 * Business helpers for inventory operations used by controllers and the dashboard.
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

    public int getTotalMaterials() {
        return materialDAO.getTotalMaterials();
    }

    public int getLowStockCount() {
        return materialDAO.getLowStockCount();
    }

    public boolean addSupplier(Supplier supplier) {
        Validator.requireNotNull(supplier, "Supplier");
        Validator.requireNonBlank(supplier.getSupplierName(), "Supplier name");
        return supplierDAO.addSupplier(supplier);
    }

    public boolean updateSupplier(Supplier supplier) {
        Validator.requireNotNull(supplier, "Supplier");
        Validator.requireId(supplier.getSupplierId(), "Supplier id");
        return supplierDAO.updateSupplier(supplier);
    }

    public boolean deleteSupplier(long supplierId) {
        Validator.requireId(supplierId, "Supplier id");
        return supplierDAO.deleteSupplier(supplierId);
    }

    public Supplier getSupplier(long supplierId) {
        return supplierDAO.getSupplierById(supplierId);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

    public boolean addMaterial(Material material) {
        Validator.requireNotNull(material, "Material");
        Validator.requireNonBlank(material.getMaterialName(), "Material name");
        Validator.requireNonNegative(material.getQuantity(), "Quantity");
        Validator.requireNonNegative(material.getReorderLevel(), "Reorder level");
        return materialDAO.addMaterial(material);
    }

    public boolean updateMaterial(Material material) {
        Validator.requireNotNull(material, "Material");
        Validator.requireId(material.getMaterialId(), "Material id");
        return materialDAO.updateMaterial(material);
    }

    public boolean deleteMaterial(long materialId) {
        Validator.requireId(materialId, "Material id");
        return materialDAO.deleteMaterial(materialId);
    }

    public Material getMaterial(long materialId) {
        return materialDAO.getMaterialById(materialId);
    }

    public List<Material> getAllMaterials() {
        return materialDAO.getAllMaterials();
    }

    public List<Material> getLowStockMaterials() {
        return materialDAO.getLowStockMaterials();
    }

    public boolean addCleaner(Cleaner cleaner) {
        Validator.requireNotNull(cleaner, "Cleaner");
        Validator.requireNonBlank(cleaner.getFirstName(), "First name");
        Validator.requireNonBlank(cleaner.getLastName(), "Last name");
        return cleanerDAO.addCleaner(cleaner);
    }

    public boolean updateCleaner(Cleaner cleaner) {
        Validator.requireNotNull(cleaner, "Cleaner");
        Validator.requireId(cleaner.getCleanerId(), "Cleaner id");
        return cleanerDAO.updateCleaner(cleaner);
    }

    public boolean deleteCleaner(long cleanerId) {
        Validator.requireId(cleanerId, "Cleaner id");
        return cleanerDAO.deleteCleaner(cleanerId);
    }

    public Cleaner getCleaner(long cleanerId) {
        return cleanerDAO.getCleanerById(cleanerId);
    }

    public List<Cleaner> getAllCleaners() {
        return cleanerDAO.getAllCleaners();
    }

    public List<Material> issueStock(StockIssueHeader stockIssue) {
        Validator.requireNotNull(stockIssue, "Stock issue");
        Validator.requireId(stockIssue.getCleanerId(), "Cleaner id");
        Validator.requireId(stockIssue.getUserId(), "User id");
        Validator.requireNotNull(stockIssue.getItems(), "Issue items");
        if (stockIssue.getItems().isEmpty()) {
            throw new IllegalArgumentException("At least one issue item is required");
        }

        boolean ok = stockIssueDAO.issueMaterials(
                stockIssue.getCleanerId(),
                stockIssue.getUserId(),
                stockIssue.getItems(),
                stockIssue.getRemarks());
        if (!ok) {
            throw new IllegalArgumentException("Failed to issue stock (check quantities and stock levels)");
        }

        List<Material> lowStockWarnings = new ArrayList<>();
        for (StockIssueItem item : stockIssue.getItems()) {
            Material updated = materialDAO.getMaterialById(item.getMaterialId());
            if (updated != null && updated.isLowStock()) {
                lowStockWarnings.add(updated);
            }
        }
        return lowStockWarnings;
    }

    public StockIssueHeader getStockIssue(long issueId) {
        return stockIssueDAO.getIssuanceById(issueId);
    }

    public List<StockIssueHeader> getAllStockIssues() {
        return stockIssueDAO.getAllIssuances();
    }
}
