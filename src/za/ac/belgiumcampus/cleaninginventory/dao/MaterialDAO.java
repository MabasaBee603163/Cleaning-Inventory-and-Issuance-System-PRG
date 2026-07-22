package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.Material;
import java.util.List;

public interface MaterialDAO {
    boolean addMaterial(Material material);
    boolean updateMaterial(Material material);
    boolean deleteMaterial(long materialId);
    Material getMaterialById(long materialId);
    List<Material> getAllMaterials();
    List<Material> searchMaterials(String keyword);
    List<Material> getLowStockMaterials();
    boolean updateStock(long materialId, int newQuantity);
    int getTotalMaterials();
    int getLowStockCount();
}
