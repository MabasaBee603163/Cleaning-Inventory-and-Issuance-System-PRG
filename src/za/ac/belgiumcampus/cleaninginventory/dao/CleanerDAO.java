package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;
import java.util.List;

public interface CleanerDAO {
    boolean addCleaner(Cleaner cleaner);
    boolean updateCleaner(Cleaner cleaner);
    boolean deleteCleaner(long cleanerId);
    Cleaner getCleanerById(long cleanerId);
    List<Cleaner> getAllCleaners();
    List<Cleaner> searchCleaners(String keyword);
    int getTotalCleaners();
}
