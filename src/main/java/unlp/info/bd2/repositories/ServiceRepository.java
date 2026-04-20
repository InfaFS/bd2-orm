package unlp.info.bd2.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import unlp.info.bd2.model.Service;

public interface ServiceRepository extends CrudRepository<Service, Long> {

    @Query("FROM Service s WHERE s.name = :name AND s.supplier.id = :supplierId")
    Service findByNameAndSupplierId(@Param("name") String name, @Param("supplierId") Long supplierId);

    @Query("SELECT item.service FROM ItemService item GROUP BY item.service ORDER BY COUNT(DISTINCT item.purchase) DESC, item.service.id DESC LIMIT 1")
    Service getMostDemandedService();
}
