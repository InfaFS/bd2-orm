package unlp.info.bd2.repositories;

public interface ToursRepository {
    UserRepository getUserRepository();

    RouteRepository getRouteRepository();

    PurchaseRepository getPurchaseRepository();

    ServiceRepository getServiceRepository();

    SupplierRepository getSupplierRepository();

    ReviewRepository getReviewRepository();

    StopRepository getStopRepository();
}
