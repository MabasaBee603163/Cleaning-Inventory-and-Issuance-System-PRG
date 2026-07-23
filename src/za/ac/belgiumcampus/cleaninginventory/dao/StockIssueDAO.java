package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.StockIssueHeader;
import za.ac.belgiumcampus.cleaninginventory.model.StockIssueItem;
import java.util.List;

public interface StockIssueDAO {
    boolean issueMaterials(long cleanerId, long userId, List<StockIssueItem> items, String remarks);
    List<StockIssueHeader> getAllIssuances();
    List<StockIssueHeader> getRecentIssuances(int limit);
    StockIssueHeader getIssuanceById(long issueId);
    List<StockIssueItem> getIssueItems(long issueId);
    boolean deleteIssuance(long issueId);
    int getTotalIssuances();
}
