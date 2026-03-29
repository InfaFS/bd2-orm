package unlp.info.bd2.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ToursRepositoryImpl implements ToursRepository {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StopRepository stopRepository;

    @Override
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public RouteRepository getRouteRepository() {
        return routeRepository;
    }

    @Override
    public PurchaseRepository getPurchaseRepository() {
        return purchaseRepository;
    }

    @Override
    public ServiceRepository getServiceRepository() {
        return serviceRepository;
    }

    @Override
    public SupplierRepository getSupplierRepository() {
        return supplierRepository;
    }

    @Override
    public ReviewRepository getReviewRepository() {
        return reviewRepository;
    }

    @Override
    public StopRepository getStopRepository() {
        return stopRepository;
    }
}
