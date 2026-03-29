package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Purchase;

import java.util.List;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Purchase save(Purchase purchase) {
        sessionFactory.getCurrentSession().save(purchase);
        return purchase;
    }

    @Override
    public Purchase findById(Long id) {
        return sessionFactory.getCurrentSession().get(Purchase.class, id);
    }

    @Override
    public List<Purchase> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Purchase", Purchase.class)
                .list();
    }

    @Override
    public void delete(Purchase purchase) {
        sessionFactory.getCurrentSession().delete(purchase);
    }

    @Override
    public Purchase update(Purchase purchase) {
        sessionFactory.getCurrentSession().update(purchase);
        return purchase;
    }

    @Override
    public Purchase findByCode(String code) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Purchase p WHERE p.code = :code", Purchase.class)
                .setParameter("code", code)
                .uniqueResult();
    }

    @Override
    public List<Purchase> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Purchase p WHERE p.user.username = :username", Purchase.class)
                .setParameter("username", username)
                .list();
    }
}
