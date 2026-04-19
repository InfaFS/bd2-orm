package unlp.info.bd2.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import unlp.info.bd2.model.Purchase;

import java.util.Date;
import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase, Long> {

    @Query("FROM Purchase p WHERE p.code = :code")
    Purchase findByCode(@Param("code") String code);

    List<Purchase> findByUserUsername(String username);

    long countByDateBetween(Date from, Date to);

    @Query("SELECT COUNT(p) FROM Purchase p WHERE p.route.id = :routeId")
    long countByRoute(@Param("routeId") Long routeId);
}
