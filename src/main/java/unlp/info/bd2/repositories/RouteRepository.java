package unlp.info.bd2.repositories;

import unlp.info.bd2.model.Route;
import java.util.List;

public interface RouteRepository {
    Route save(Route route);
    Route findById(Long id);
    List<Route> findAll();
    void delete(Route route);
    Route update(Route route);
    List<Route> findBelowPrice(float price);
    boolean hasPurchases(Long routeId);
}
