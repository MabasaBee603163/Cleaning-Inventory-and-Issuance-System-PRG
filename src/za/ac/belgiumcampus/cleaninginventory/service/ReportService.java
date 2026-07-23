package za.ac.belgiumcampus.cleaninginventory.service;

import za.ac.belgiumcampus.cleaninginventory.dao.MaterialDAO;
import za.ac.belgiumcampus.cleaninginventory.dao.StockIssueDAO;
import za.ac.belgiumcampus.cleaninginventory.dao.SupplierDAO;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.MaterialDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.StockIssueDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.dao.impl.SupplierDAOImpl;
import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueHeader;
import za.ac.belgiumcampus.cleaninginventory.model.Supplier;

import java.util.List;

/**
 * Read-only reporting queries for the dashboard and inventory overview.
 */
public class ReportService {

    private final MaterialDAO materialDAO;
    private final StockIssueDAO stockIssueDAO;
    private final SupplierDAO supplierDAO;

    public ReportService() {
        this(new MaterialDAOImpl(), new StockIssueDAOImpl(), new SupplierDAOImpl());
    }

    public ReportService(MaterialDAO materialDAO, StockIssueDAO stockIssueDAO, SupplierDAO supplierDAO) {
        this.materialDAO = materialDAO;
        this.stockIssueDAO = stockIssueDAO;
        this.supplierDAO = supplierDAO;
    }

    public List<Material> getLowStockMaterials() {
        return materialDAO.getMaterialsBelowReorderLevel();
    }

    public List<StockIssueHeader> getIssuedMaterials() {
        return stockIssueDAO.getAllStockIssues();
    }

    public List<Supplier> getSupplierInformation() {
        return supplierDAO.getAllSuppliers();
    }

    public int getMaterialCount() {
        return materialDAO.getAllMaterials().size();
    }

    public int getSupplierCount() {
        return supplierDAO.getAllSuppliers().size();
    }

    public int getLowStockCount() {
        return materialDAO.getMaterialsBelowReorderLevel().size();
    }

    public int getIssueCount() {
        return stockIssueDAO.getAllStockIssues().size();
    }
}
