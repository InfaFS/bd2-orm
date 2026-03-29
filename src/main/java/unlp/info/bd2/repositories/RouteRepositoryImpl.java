package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Route;

import java.util.List;

@Repository
public class RouteRepositoryImpl implements RouteRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Route save(Route route) {
        sessionFactory.getCurrentSession().save(route);
        return route;
    }

    @Override
    public Route findById(Long id) {
        return sessionFactory.getCurrentSession().get(Route.class, id);
    }

    @Override
    public List<Route> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Route", Route.class)
                .list();
    }

    @Override
    public void delete(Route route) {
        sessionFactory.getCurrentSession().delete(route);
    }

    @Override
    public Route update(Route route) {
        sessionFactory.getCurrentSession().update(route);
        return route;
    }
}
