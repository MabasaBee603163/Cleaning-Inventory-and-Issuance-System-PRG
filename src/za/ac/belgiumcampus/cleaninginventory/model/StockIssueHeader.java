package za.ac.belgiumcampus.cleaninginventory.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StockIssueHeader {
    private long issueId;
    private long cleanerId;
    private long userId;
    private LocalDateTime issueDate;
    private String remarks;
    private List<StockIssueItem> items = new ArrayList<>();

    /** Display-only fields from JOIN queries. */
    private String cleanerName;
    private String userName;

    public StockIssueHeader() {}

    public StockIssueHeader(long issueId, long cleanerId, long userId, LocalDateTime issueDate, String remarks) {
        this.issueId = issueId;
        this.cleanerId = cleanerId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.remarks = remarks;
    }

    public long getIssueId() {
        return issueId;
    }

    public long getCleanerId() {
        return cleanerId;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public List<StockIssueItem> getItems() {
        return items;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public void setCleanerId(long cleanerId) {
        this.cleanerId = cleanerId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setItems(List<StockIssueItem> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public String getCleanerName() {
        return cleanerName;
    }

    public void setCleanerName(String cleanerName) {
        this.cleanerName = cleanerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "StockIssueHeader{" +
                "issueId=" + issueId +
                ", cleanerId=" + cleanerId +
                ", userId=" + userId +
                ", issueDate=" + issueDate +
                ", remarks='" + remarks + '\'' +
                ", items=" + items +
                '}';
    }
}
