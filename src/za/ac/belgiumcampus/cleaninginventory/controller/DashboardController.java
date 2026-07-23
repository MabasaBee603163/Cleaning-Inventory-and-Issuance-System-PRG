package za.ac.belgiumcampus.cleaninginventory.controller;

import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.service.ReportService;
import za.ac.belgiumcampus.cleaninginventory.utils.ActionResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController {

    private final ReportService reportService;

    public DashboardController() {
        this(new ReportService());
    }

    public DashboardController(ReportService reportService) {
        this.reportService = reportService;
    }

    public ActionResult<Map<String, Integer>> getSummary() {
        try {
            Map<String, Integer> summary = new HashMap<>();
            summary.put("materials", reportService.getMaterialCount());
            summary.put("suppliers", reportService.getSupplierCount());
            summary.put("lowStock", reportService.getLowStockCount());
            summary.put("issues", reportService.getIssueCount());
            return ActionResult.ok("Dashboard summary loaded", summary);
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to load dashboard summary: " + e.getMessage());
        }
    }

    public ActionResult<List<Material>> getLowStockMaterials() {
        try {
            return ActionResult.ok("Low stock materials loaded", reportService.getLowStockMaterials());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to load low stock materials: " + e.getMessage());
        }
    }
}
