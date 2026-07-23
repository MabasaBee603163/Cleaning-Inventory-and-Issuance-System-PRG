package za.ac.belgiumcampus.cleaninginventory.controller;

import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueHeader;
import za.ac.belgiumcampus.cleaninginventory.service.InventoryService;
import za.ac.belgiumcampus.cleaninginventory.utils.ActionResult;

import java.util.List;

public class StockIssueController {

    private final InventoryService inventoryService;

    public StockIssueController() {
        this(new InventoryService());
    }

    public StockIssueController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ActionResult<List<Material>> issueStock(StockIssueHeader stockIssue) {
        try {
            List<Material> lowStock = inventoryService.issueStock(stockIssue);
            String message = "Stock issued successfully";
            if (!lowStock.isEmpty()) {
                message += " — warning: " + lowStock.size() + " material(s) at or below reorder level";
            }
            return ActionResult.ok(message, lowStock);
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to issue stock: " + e.getMessage());
        }
    }

    public ActionResult<StockIssueHeader> getStockIssue(long issueId) {
        try {
            StockIssueHeader issue = inventoryService.getStockIssue(issueId);
            if (issue == null) {
                return ActionResult.fail("Stock issue not found");
            }
            return ActionResult.ok("Stock issue loaded", issue);
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to get stock issue: " + e.getMessage());
        }
    }

    public ActionResult<List<StockIssueHeader>> getAllStockIssues() {
        try {
            return ActionResult.ok("Stock issues loaded", inventoryService.getAllStockIssues());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to load stock issues: " + e.getMessage());
        }
    }
}
