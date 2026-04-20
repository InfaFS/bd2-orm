package unlp.info.bd2.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import unlp.info.bd2.model.ItemService;
import unlp.info.bd2.model.Purchase;

public interface ItemServiceRepository extends CrudRepository<ItemService, Long> {

    @Modifying
    @Query("DELETE FROM ItemService i WHERE i.purchase = :purchase")
    void deleteByPurchase(@Param("purchase") Purchase purchase);
}
