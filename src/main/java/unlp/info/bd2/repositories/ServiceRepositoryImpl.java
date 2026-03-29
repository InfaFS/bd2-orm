package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Service;

import java.util.List;

@Repository
public class ServiceRepositoryImpl implements ServiceRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Service save(Service service) {
        sessionFactory.getCurrentSession().save(service);
        return service;
    }

    @Override
    public Service findById(Long id) {
        return sessionFactory.getCurrentSession().get(Service.class, id);
    }

    @Override
    public List<Service> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Service", Service.class)
                .list();
    }

    @Override
    public void delete(Service service) {
        sessionFactory.getCurrentSession().delete(service);
    }

    @Override
    public Service update(Service service) {
        sessionFactory.getCurrentSession().update(service);
        return service;
    }

    @Override
    public Service findByNameAndSupplierId(String name, Long supplierId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Service s WHERE s.name = :name AND s.supplier.id = :supplierId", Service.class)
                .setParameter("name", name)
                .setParameter("supplierId", supplierId)
                .uniqueResult();
    }
}
