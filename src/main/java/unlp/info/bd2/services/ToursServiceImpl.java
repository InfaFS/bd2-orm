package unlp.info.bd2.services;

import org.springframework.transaction.annotation.Transactional;
import unlp.info.bd2.model.*;
import unlp.info.bd2.repositories.ToursRepository;
import unlp.info.bd2.utils.ToursException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
public class ToursServiceImpl implements ToursService {

    private final ToursRepository repository;

    public ToursServiceImpl(ToursRepository repository) {
        this.repository = repository;
    }

    // -------------------------------------------------------------------------
    // USER
    // -------------------------------------------------------------------------

    @Override
    public User createUser(String username, String password, String fullName,
                           String email, Date birthdate, String phoneNumber) throws ToursException {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(fullName);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setPhoneNumber(phoneNumber);
        user.setActive(true);
        return repository.getUserRepository().save(user);
    }

    @Override
    public DriverUser createDriverUser(String username, String password, String fullName,
                                       String email, Date birthdate, String phoneNumber,
                                       String expedient) throws ToursException {
        DriverUser driver = new DriverUser();
        driver.setUsername(username);
        driver.setPassword(password);
        driver.setName(fullName);
        driver.setEmail(email);
        driver.setBirthdate(birthdate);
        driver.setPhoneNumber(phoneNumber);
        driver.setExpedient(expedient);
        driver.setActive(true);
        return (DriverUser) repository.getUserRepository().save(driver);
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName,
                                             String email, Date birthdate, String phoneNumber,
                                             String education) throws ToursException {
        TourGuideUser guide = new TourGuideUser();
        guide.setUsername(username);
        guide.setPassword(password);
        guide.setName(fullName);
        guide.setEmail(email);
        guide.setBirthdate(birthdate);
        guide.setPhoneNumber(phoneNumber);
        guide.setEducation(education);
        guide.setActive(true);
        return (TourGuideUser) repository.getUserRepository().save(guide);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException {
        return Optional.ofNullable(repository.getUserRepository().findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return Optional.ofNullable(repository.getUserRepository().findByUsername(username));
    }

    @Override
    public User updateUser(User user) throws ToursException {
        return repository.getUserRepository().update(user);
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        if (user.getPurchaseList() != null && !user.getPurchaseList().isEmpty()) {
            user.setActive(false);
            repository.getUserRepository().update(user);
        } else {
            repository.getUserRepository().delete(user);
        }
    }

    // -------------------------------------------------------------------------
    // STOP
    // -------------------------------------------------------------------------

    @Override
    public Stop createStop(String name, String description) throws ToursException {
        Stop stop = new Stop();
        stop.setName(name);
        stop.setDescription(description);
        return repository.getStopRepository().save(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return repository.getStopRepository().findByNameStart(name);
    }

    // -------------------------------------------------------------------------
    // ROUTE
    // -------------------------------------------------------------------------

    @Override
    public Route createRoute(String name, float price, float totalKm,
                             int maxNumberOfUsers, List<Stop> stops) throws ToursException {
        Route route = new Route();
        route.setName(name);
        route.setPrice(price);
        route.setTotalKm(totalKm);
        route.setMaxNumberUsers(maxNumberOfUsers);
        route.setStops(stops);
        return repository.getRouteRepository().save(route);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return Optional.ofNullable(repository.getRouteRepository().findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price) {
        return repository.getRouteRepository().findBelowPrice(price);
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        User user = repository.getUserRepository().findByUsername(username);
        if (user == null || !(user instanceof DriverUser)) {
            throw new ToursException("Driver not found: " + username);
        }
        Route route = repository.getRouteRepository().findById(idRoute);
        if (route == null) {
            throw new ToursException("Route not found: " + idRoute);
        }
        route.getDriverList().add((DriverUser) user);
        repository.getRouteRepository().update(route);
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        User user = repository.getUserRepository().findByUsername(username);
        if (user == null || !(user instanceof TourGuideUser)) {
            throw new ToursException("Tour guide not found: " + username);
        }
        Route route = repository.getRouteRepository().findById(idRoute);
        if (route == null) {
            throw new ToursException("Route not found: " + idRoute);
        }
        route.getTourGuideList().add((TourGuideUser) user);
        repository.getRouteRepository().update(route);
    }

    @Override
    public void deleteRoute(Route route) throws ToursException {
        if (repository.getRouteRepository().hasPurchases(route.getId())) {
            throw new ToursException("Cannot delete route with associated purchases");
        }
        repository.getRouteRepository().delete(route);
    }

    // -------------------------------------------------------------------------
    // SUPPLIER
    // -------------------------------------------------------------------------

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        Supplier supplier = new Supplier();
        supplier.setBusinessName(businessName);
        supplier.setAuthorizationNumber(authorizationNumber);
        return repository.getSupplierRepository().save(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) {
        return Optional.ofNullable(repository.getSupplierRepository().findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return Optional.ofNullable(
                repository.getSupplierRepository().findByAuthorizationNumber(authorizationNumber));
    }

    // -------------------------------------------------------------------------
    // SERVICE
    // -------------------------------------------------------------------------

    @Override
    public Service addServiceToSupplier(String name, float price, String description,
                                        Supplier supplier) throws ToursException {
        Service service = new Service();
        service.setName(name);
        service.setPrice(price);
        service.setDescription(description);
        service.setSupplier(supplier);
        return repository.getServiceRepository().save(service);
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        Service service = repository.getServiceRepository().findById(id);
        if (service == null) {
            throw new ToursException("Service not found: " + id);
        }
        service.setPrice(newPrice);
        return repository.getServiceRepository().update(service);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return Optional.ofNullable(
                repository.getServiceRepository().findByNameAndSupplierId(name, id));
    }

    // -------------------------------------------------------------------------
    // PURCHASE
    // -------------------------------------------------------------------------

    @Override
    public Purchase createPurchase(String code, Route route, User user) throws ToursException {
        return createPurchase(code, new Date(), route, user);
    }

    @Override
    public Purchase createPurchase(String code, Date date, Route route, User user) throws ToursException {
        Purchase purchase = new Purchase();
        purchase.setCode(code);
        purchase.setDate(date);
        purchase.setRoute(route);
        purchase.setUser(user);
        purchase.setTotalPrice(route.getPrice());
        return repository.getPurchaseRepository().save(purchase);
    }

    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService item = new ItemService();
        item.setService(service);
        item.setQuantity(quantity);
        item.setPurchase(purchase);
        purchase.setTotalPrice(purchase.getTotalPrice() + service.getPrice() * quantity);
        repository.getPurchaseRepository().update(purchase);
        // ItemService se persiste via cascada desde Purchase (CascadeType.ALL)
        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code) {
        return Optional.ofNullable(repository.getPurchaseRepository().findByCode(code));
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        repository.getPurchaseRepository().delete(purchase);
    }

    // -------------------------------------------------------------------------
    // REVIEW
    // -------------------------------------------------------------------------

    @Override
    public Review addReviewToPurchase(int rating, String comment, Purchase purchase) throws ToursException {
        if (purchase.getReview() != null) {
            throw new ToursException("Purchase already has a review");
        }
        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setPurchase(purchase);
        purchase.setReview(review);
        repository.getPurchaseRepository().update(purchase);
        // Review se persiste via cascada desde Purchase (CascadeType.ALL)
        return review;
    }

    // -------------------------------------------------------------------------
    // HQL QUERIES (a implementar en etapas posteriores)
    // -------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxRating() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public Service getMostDemandedService() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
