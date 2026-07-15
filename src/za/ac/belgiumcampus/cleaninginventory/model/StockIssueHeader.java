package za.ac.belgiumcampus.cleaninginventory.model;
import java.time.LocalDateTime;


public class StockIssue {
    private long issueId;

    private long cleanerId;

    private long userId;

    private LocalDateTime issueDate;

    private String remarks;

    public StockIssue() {}

    public StockIssue(long issueId, long cleanerId, long userId, LocalDateTime issueDate, String remarks) {
        this.issueId = issueId;
        this.cleanerId = cleanerId;
        this.userId = userId;
        this.issueDate = issueDate;
        this.remarks = remarks;
    }

    //Getters

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

    //Setters

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

    //tostring
    @Override
    public String toString() {
        return "StockIssueHeader{" +
            "issueId=" + issueId +
            ", cleanerId=" + cleanerId +
            ", userId=" + userId +
            ", issueDate=" + issueDate +
            ", remarks='" + remarks + '\'' +
            '}';
    }
}
