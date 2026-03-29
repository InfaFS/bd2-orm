package unlp.info.bd2.repositories;

import unlp.info.bd2.model.Purchase;
import java.util.List;
import java.util.Date;

public interface PurchaseRepository {
    Purchase save(Purchase purchase);

    Purchase findById(Long id);

    List<Purchase> findAll();

    void delete(Purchase purchase);

    Purchase update(Purchase purchase);

    Purchase findByCode(String code);

    List<Purchase> findByUsername(String username);

    int getCountOfPurchasesBetweenDates(Date start, Date end);
}
