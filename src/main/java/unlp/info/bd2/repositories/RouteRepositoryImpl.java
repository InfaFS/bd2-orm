package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

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

    @Override
    public List<Route> findBelowPrice(float price) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Route r WHERE r.price < :price", Route.class)
                .setParameter("price", price)
                .list();
    }

    @Override
    public boolean hasPurchases(Long routeId) {
        Long count = sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(p) FROM Purchase p WHERE p.route.id = :routeId", Long.class)
                .setParameter("routeId", routeId)
                .uniqueResult();
        return count != null && count > 0;
    }

    @Override
    public List<Route> getRoutesWithStop(Stop stop) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT DISTINCT r FROM Route r JOIN r.stops s WHERE s.id = :stopId", Route.class)
                .setParameter("stopId", stop.getId())
                .list();
    }

    @Override
    public int getMaxStopOfRoutes() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT MAX(SIZE(r.stops)) FROM Route r", Integer.class)
                .uniqueResult();
    }

    @Override
    public List<Route> getRoutsNotSell() {
        return sessionFactory.getCurrentSession()
                .createQuery(
                    "FROM Route r WHERE r.id NOT IN " +
                    "(SELECT DISTINCT p.route.id FROM Purchase p)",
                    Route.class)
                .list();
    }

    @Override
    public List<Route> getTop3RoutesWithMaxRating() {
        return sessionFactory.getCurrentSession()
                .createQuery(
                    "SELECT p.route " +
                    "FROM Purchase p " +
                    "JOIN p.review r " +
                    "GROUP BY p.route " +
                    "ORDER BY AVG(r.rating) DESC",
                    Route.class)
                .setMaxResults(3)
                .list();
    }
}
