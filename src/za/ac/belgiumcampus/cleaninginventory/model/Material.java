package za.ac.belgiumcampus.cleaninginventory.model;

public class Material {

    private long materialId;

    private String materialName;

    private String description;

    private int quantity;

    private String unit;

    private int reorderLevel;

    private long supplierId;

    public Material() {}

    public Material(long materialId, String materialName, String description, int quantity, String unit, int reorderLevel, long supplierId) {
        this.materialId = materialId;
        this.materialName = materialName;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.reorderLevel = reorderLevel;
        this.supplierId = supplierId;
    }

    //Getters
    public long getMaterialId() {
        return materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public long getSupplierId() {
        return supplierId;
    }

    //Setters
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }   

    //tostring
    @Override
    public String toString() {
        return "Material{" +
                "materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", reorderLevel=" + reorderLevel +
                ", supplierId=" + supplierId +
                '}';
    }
}