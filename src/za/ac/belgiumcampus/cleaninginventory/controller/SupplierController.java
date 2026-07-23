package za.ac.belgiumcampus.cleaninginventory.controller;

import za.ac.belgiumcampus.cleaninginventory.model.Supplier;
import za.ac.belgiumcampus.cleaninginventory.service.InventoryService;
import za.ac.belgiumcampus.cleaninginventory.utils.ActionResult;

import java.util.List;

public class SupplierController {

    private final InventoryService inventoryService;

    public SupplierController() {
        this(new InventoryService());
    }

    public SupplierController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ActionResult<Supplier> addSupplier(Supplier supplier) {
        try {
            inventoryService.addSupplier(supplier);
            return ActionResult.ok("Supplier added successfully", supplier);
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to add supplier: " + e.getMessage());
        }
    }

    public ActionResult<Supplier> updateSupplier(Supplier supplier) {
        try {
            inventoryService.updateSupplier(supplier);
            return ActionResult.ok("Supplier updated successfully", supplier);
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to update supplier: " + e.getMessage());
        }
    }

    public ActionResult<Void> deleteSupplier(long supplierId) {
        try {
            inventoryService.deleteSupplier(supplierId);
            return ActionResult.ok("Supplier deleted successfully");
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to delete supplier: " + e.getMessage());
        }
    }

    public ActionResult<Supplier> getSupplier(long supplierId) {
        try {
            Supplier supplier = inventoryService.getSupplier(supplierId);
            if (supplier == null) {
                return ActionResult.fail("Supplier not found");
            }
            return ActionResult.ok("Supplier loaded", supplier);
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to get supplier: " + e.getMessage());
        }
    }

    public ActionResult<List<Supplier>> getAllSuppliers() {
        try {
            return ActionResult.ok("Suppliers loaded", inventoryService.getAllSuppliers());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to load suppliers: " + e.getMessage());
        }
    }
}
