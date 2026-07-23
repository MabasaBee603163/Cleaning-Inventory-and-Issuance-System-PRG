package za.ac.belgiumcampus.cleaninginventory.controller;

import za.ac.belgiumcampus.cleaninginventory.model.Material;
import za.ac.belgiumcampus.cleaninginventory.service.InventoryService;
import za.ac.belgiumcampus.cleaninginventory.utils.ActionResult;

import java.util.List;

public class MaterialController {

    private final InventoryService inventoryService;

    public MaterialController() {
        this(new InventoryService());
    }

    public MaterialController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ActionResult<Material> addMaterial(Material material) {
        try {
            inventoryService.addMaterial(material);
            return ActionResult.ok("Material added successfully", material);
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to add material: " + e.getMessage());
        }
    }

    public ActionResult<Material> updateMaterial(Material material) {
        try {
            inventoryService.updateMaterial(material);
            return ActionResult.ok("Material updated successfully", material);
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to update material: " + e.getMessage());
        }
    }

    public ActionResult<Void> deleteMaterial(long materialId) {
        try {
            inventoryService.deleteMaterial(materialId);
            return ActionResult.ok("Material deleted successfully");
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to delete material: " + e.getMessage());
        }
    }

    public ActionResult<Material> getMaterial(long materialId) {
        try {
            Material material = inventoryService.getMaterial(materialId);
            if (material == null) {
                return ActionResult.fail("Material not found");
            }
            return ActionResult.ok("Material loaded", material);
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to get material: " + e.getMessage());
        }
    }

    public ActionResult<List<Material>> getAllMaterials() {
        try {
            return ActionResult.ok("Materials loaded", inventoryService.getAllMaterials());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to load materials: " + e.getMessage());
        }
    }

    public ActionResult<List<Material>> getLowStockMaterials() {
        try {
            return ActionResult.ok("Low stock materials loaded", inventoryService.getLowStockMaterials());
        } catch (RuntimeException e) {
            return ActionResult.fail("Failed to load low stock materials: " + e.getMessage());
        }
    }
}
