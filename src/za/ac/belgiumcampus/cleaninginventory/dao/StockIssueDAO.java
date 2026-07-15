package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.StockIssueHeader;
import java.util.List;

public interface StockIssueDAO {

    void createStockIssue(StockIssueHeader stockIssue);

    void updateStockIssue(StockIssueHeader stockIssue);

    void deleteStockIssue(long issueId);

    StockIssueHeader getStockIssueById(long issueId);

    List<StockIssueHeader> getAllStockIssues();
}