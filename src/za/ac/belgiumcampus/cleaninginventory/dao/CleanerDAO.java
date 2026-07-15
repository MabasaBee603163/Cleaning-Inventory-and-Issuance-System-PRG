package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.Cleaner;
import java.util.List;

public interface CleanerDAO {

    void addCleaner(Cleaner cleaner);

    void updateCleaner(Cleaner cleaner);

    void deleteCleaner(long cleanerId);

    Cleaner getCleanerById(long cleanerId);

    List<Cleaner> getAllCleaners();
}