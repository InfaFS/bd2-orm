package unlp.info.bd2.repositories;

import unlp.info.bd2.model.Supplier;
import java.util.List;

public interface SupplierRepository {
    Supplier save(Supplier supplier);
    Supplier findById(Long id);
    List<Supplier> findAll();
    void delete(Supplier supplier);
    Supplier update(Supplier supplier);
    Supplier findByAuthorizationNumber(String authorizationNumber);
}
