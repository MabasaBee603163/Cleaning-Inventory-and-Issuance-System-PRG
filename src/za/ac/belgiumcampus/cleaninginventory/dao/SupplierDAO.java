package za.ac.belgiumcampus.cleaninginventory.dao;

import za.ac.belgiumcampus.cleaninginventory.model.Supplier;
import java.util.List;

public interface SupplierDAO {

    void addSupplier(Supplier supplier);

    void updateSupplier(Supplier supplier);

    void deleteSupplier(long supplierId);

    Supplier getSupplierById(long supplierId);

    List<Supplier> getAllSuppliers();
}