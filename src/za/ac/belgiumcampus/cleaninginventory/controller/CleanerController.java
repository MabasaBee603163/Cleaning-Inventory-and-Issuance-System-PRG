package za.ac.belgiumcampus.cleaninginventory.controller;

import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;
import za.ac.belgiumcampus.cleaninginventory.service.InventoryService;
import za.ac.belgiumcampus.cleaninginventory.utils.ActionResult;

import java.util.List;

public class CleanerController {

    private final InventoryService inventoryService;

    public CleanerController() {
        this(new InventoryService());
    }

    public CleanerController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ActionResult<Cleaner> addCleaner(Cleaner cleaner) {
        try {
            inventoryService.addCleaner(cleaner);
            return ActionResult.ok("Cleaner added successfully", cleaner);
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to add cleaner: " + e.getMessage());
        }
    }

    public ActionResult<Cleaner> updateCleaner(Cleaner cleaner) {
        try {
            inventoryService.updateCleaner(cleaner);
            return ActionResult.ok("Cleaner updated successfully", cleaner);
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to update cleaner: " + e.getMessage());
        }
    }

    public ActionResult<Void> deleteCleaner(long cleanerId) {
        try {
            inventoryService.deleteCleaner(cleanerId);
            return ActionResult.ok("Cleaner deleted successfully");
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to delete cleaner: " + e.getMessage());
        }
    }

    public ActionResult<Cleaner> getCleaner(long cleanerId) {
        try {
            Cleaner cleaner = inventoryService.getCleaner(cleanerId);
            if (cleaner == null) {
                return ActionResult.fail("Cleaner not found");
            }
            return ActionResult.ok("Cleaner loaded", cleaner);
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to get cleaner: " + e.getMessage());
        }
    }

    public ActionResult<List<Cleaner>> getAllCleaners() {
        try {
            return ActionResult.ok("Cleaners loaded", inventoryService.getAllCleaners());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to load cleaners: " + e.getMessage());
        }
    }
}
