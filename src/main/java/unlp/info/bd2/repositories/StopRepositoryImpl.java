package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Stop;

import java.util.List;

@Repository
public class StopRepositoryImpl implements StopRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Stop save(Stop stop) {
        sessionFactory.getCurrentSession().save(stop);
        return stop;
    }

    @Override
    public Stop findById(Long id) {
        return sessionFactory.getCurrentSession().get(Stop.class, id);
    }

    @Override
    public List<Stop> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Stop", Stop.class)
                .list();
    }

    @Override
    public void delete(Stop stop) {
        sessionFactory.getCurrentSession().delete(stop);
    }

    @Override
    public Stop update(Stop stop) {
        sessionFactory.getCurrentSession().update(stop);
        return stop;
    }

    @Override
    public List<Stop> findByNameStart(String prefix) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Stop s WHERE s.name LIKE :prefix", Stop.class)
                .setParameter("prefix", prefix + "%")
                .list();
    }
}
