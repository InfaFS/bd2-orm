package unlp.info.bd2.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import unlp.info.bd2.dto.RouteSummaryDTO;
import unlp.info.bd2.model.Route;
import unlp.info.bd2.model.Stop;

import java.util.List;

public interface RouteRepository extends CrudRepository<Route, Long> {

    @Query("FROM Route r WHERE r.price < :price")
    List<Route> findBelowPrice(@Param("price") float price);

    @Query("SELECT COUNT(r) > 0 FROM Route r JOIN r.tourGuideList g WHERE g.id = :userId AND g.active = true")
    boolean isTourGuideAssignedToAnyRoute(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) > 0 FROM Purchase p WHERE p.route.id = :routeId")
    boolean hasPurchases(@Param("routeId") Long routeId);

    List<Route> findDistinctByStops(Stop stop);

    @Query("SELECT MAX(SIZE(r.stops)) FROM Route r")
    int getMaxStopOfRoutes();

    @Query("FROM Route r WHERE r.id NOT IN (SELECT DISTINCT p.route.id FROM Purchase p)")
    List<Route> getRoutsNotSell();

    @Query("SELECT p.route FROM Purchase p JOIN p.review r GROUP BY p.route ORDER BY AVG(r.rating) DESC")
    List<Route> getTop3RoutesWithMaxRating(Pageable pageable);

    @Query("SELECT new unlp.info.bd2.dto.RouteSummaryDTO(r.name, COUNT(p.id), COALESCE(AVG(p.totalPrice), 0.0)) " +
           "FROM Route r LEFT JOIN Purchase p ON p.route = r " +
           "GROUP BY r.id, r.name")
    List<RouteSummaryDTO> getRouteSummaries();
}
