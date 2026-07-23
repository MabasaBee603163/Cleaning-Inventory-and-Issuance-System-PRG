package za.ac.belgiumcampus.cleaninginventory.model;

import java.time.LocalDateTime;

public class StockIssueItem {
    private long issueItemId;
    private long issueId;
    private long materialId;
    private int quantity;

    /** Display-only fields from JOIN queries. */
    private String materialName;
    private LocalDateTime createdAt;

    public StockIssueItem() {}

    public StockIssueItem(long issueItemId, long issueId, long materialId, int quantity) {
        this.issueItemId = issueItemId;
        this.issueId = issueId;
        this.materialId = materialId;
        this.quantity = quantity;
    }

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

    public String getMaterialName() {
        return materialName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

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

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "StockIssueItem{" +
                "issueItemId=" + issueItemId +
                ", issueId=" + issueId +
                ", materialId=" + materialId +
                ", quantity=" + quantity +
                ", materialName='" + materialName + '\'' +
                '}';
    }
}
