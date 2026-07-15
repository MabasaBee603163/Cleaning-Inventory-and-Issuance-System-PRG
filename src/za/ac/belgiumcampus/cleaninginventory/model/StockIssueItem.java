package za.ac.belgiumcampus.cleaninginventory.model;

public class StockIssueItem {
    private long issueItemId;

    private long issueId;

    private long materialId;

    private int quantity;

    public StockIssueItem() {}

    public StockIssueItem(long issueItemId, long issueId, long materialId, int quantity) {
        this.issueItemId = issueItemId;
        this.issueId = issueId;
        this.materialId = materialId;
        this.quantity = quantity;
    }

    //Getters

    public long getIssueItemId() {
        return issueItemId;
    }

    public long getIssueId() {
        return issueId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public int getQuantity() {
        return quantity;
    }

    //Setters

    public void setIssueItemId(long issueItemId) {
        this.issueItemId = issueItemId;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //tostring
    @Override
    public String toString() {      
        return "StockIssueItem{" +
            "issueItemId=" + issueItemId +
            ", issueId=" + issueId +
            ", materialId=" + materialId +
            ", quantity=" + quantity +
            '}';
    }
}