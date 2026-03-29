package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Supplier;

import java.util.List;

@Repository
public class SupplierRepositoryImpl implements SupplierRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Supplier save(Supplier supplier) {
        sessionFactory.getCurrentSession().save(supplier);
        return supplier;
    }

    @Override
    public Supplier findById(Long id) {
        return sessionFactory.getCurrentSession().get(Supplier.class, id);
    }

    @Override
    public List<Supplier> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Supplier", Supplier.class)
                .list();
    }

    @Override
    public void delete(Supplier supplier) {
        sessionFactory.getCurrentSession().delete(supplier);
    }

    @Override
    public Supplier update(Supplier supplier) {
        sessionFactory.getCurrentSession().update(supplier);
        return supplier;
    }

    @Override
    public Supplier findByAuthorizationNumber(String authorizationNumber) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Supplier s WHERE s.authorizationNumber = :num", Supplier.class)
                .setParameter("num", authorizationNumber)
                .uniqueResult();
    }
}
