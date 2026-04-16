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

    @Query("FROM Purchase p WHERE p.user.username = :username")
    List<Purchase> findByUsername(@Param("username") String username);

    @Query("SELECT COUNT(p) FROM Purchase p WHERE p.date BETWEEN :start AND :end")
    long getCountOfPurchasesBetweenDates(@Param("start") Date start, @Param("end") Date end);

    @Query("SELECT COUNT(p) FROM Purchase p WHERE p.route.id = :routeId")
    long countByRoute(@Param("routeId") Long routeId);
}
