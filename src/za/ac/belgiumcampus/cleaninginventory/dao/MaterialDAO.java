package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.Material;
import java.util.List;

public interface MaterialDAO {

    void addMaterial(Material material);

    void updateMaterial(Material material);

    void deleteMaterial(long materialId);

    Material getMaterialById(long materialId);

    List<Material> getAllMaterials();

    List<Material> getMaterialsBelowReorderLevel();
}

