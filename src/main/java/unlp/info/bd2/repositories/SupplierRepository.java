package unlp.info.bd2.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import unlp.info.bd2.model.Supplier;

import java.util.List;

public interface SupplierRepository extends CrudRepository<Supplier, Long> {

    @Query("FROM Supplier s WHERE s.authorizationNumber = :num")
    Supplier findByAuthorizationNumber(@Param("num") String authorizationNumber);

    @Query("SELECT item.service.supplier FROM ItemService item GROUP BY item.service.supplier ORDER BY COUNT(DISTINCT item.purchase) DESC")
    List<Supplier> findTopNByPurchases(Pageable pageable);
}
