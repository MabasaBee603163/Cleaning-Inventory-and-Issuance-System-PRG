package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.Supplier;
import java.util.List;

public interface SupplierDAO {
    boolean addSupplier(Supplier supplier);
    boolean updateSupplier(Supplier supplier);
    boolean deleteSupplier(long supplierId);
    Supplier getSupplierById(long supplierId);
    List<Supplier> getAllSuppliers();
    List<Supplier> searchSuppliers(String keyword);
    int getTotalSuppliers();
}
