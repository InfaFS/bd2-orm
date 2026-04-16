package unlp.info.bd2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import unlp.info.bd2.model.*;
import unlp.info.bd2.model.Service;
import unlp.info.bd2.repositories.*;
import unlp.info.bd2.utils.ToursException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

// (1) @Service reemplaza la instanciación manual en AppConfig.
// Spring detecta esta clase y la registra como bean automáticamente.
@org.springframework.stereotype.Service
@Transactional
public class ToursServiceImpl implements ToursService {

    // (2) Cada repositorio se inyecta individualmente.
    // Ya no existe la fachada ToursRepository que agrupaba todos los repos.
    // Spring Data genera la implementación de cada interfaz en tiempo de ejecución
    // y Spring la inyecta aquí vía @Autowired.
    @Autowired private UserRepository userRepository;
    @Autowired private RouteRepository routeRepository;
    @Autowired private StopRepository stopRepository;
    @Autowired private PurchaseRepository purchaseRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private SupplierRepository supplierRepository;
    @Autowired private ItemServiceRepository itemServiceRepository;

    // -------------------------------------------------------------------------
    // USER
    // -------------------------------------------------------------------------

    private void checkUsernameUnique(String username) throws ToursException {
        // (3) Llamada directa al repositorio sin pasar por la fachada
        if (userRepository.findByUsername(username) != null) {
            throw new ToursException("Username already exists: " + username);
        }
    }

    @Override
    public User createUser(String username, String password, String fullName,
            String email, Date birthdate, String phoneNumber) throws ToursException {
        checkUsernameUnique(username);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(fullName);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setPhoneNumber(phoneNumber);
        user.setActive(true);
        // (4) save() de CrudRepository reemplaza session.save().
        // Funciona para insert y update según si el ID ya existe.
        return userRepository.save(user);
    }

    @Override
    public DriverUser createDriverUser(String username, String password, String fullName,
            String email, Date birthdate, String phoneNumber,
            String expedient) throws ToursException {
        checkUsernameUnique(username);
        DriverUser driver = new DriverUser();
        driver.setUsername(username);
        driver.setPassword(password);
        driver.setName(fullName);
        driver.setEmail(email);
        driver.setBirthdate(birthdate);
        driver.setPhoneNumber(phoneNumber);
        driver.setExpedient(expedient);
        driver.setActive(true);
        return (DriverUser) userRepository.save(driver);
    }

    @Override
    public TourGuideUser createTourGuideUser(String username, String password, String fullName,
            String email, Date birthdate, String phoneNumber,
            String education) throws ToursException {
        checkUsernameUnique(username);
        TourGuideUser guide = new TourGuideUser();
        guide.setUsername(username);
        guide.setPassword(password);
        guide.setName(fullName);
        guide.setEmail(email);
        guide.setBirthdate(birthdate);
        guide.setPhoneNumber(phoneNumber);
        guide.setEducation(education);
        guide.setActive(true);
        return (TourGuideUser) userRepository.save(guide);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) throws ToursException {
        // (5) findById() de CrudRepository devuelve Optional<T>, no null.
        // Antes: Optional.ofNullable(repo.findById(id)) para envolver el null.
        // Ahora: findById ya devuelve Optional directamente.
        return userRepository.findById(id).map(u -> (User) u);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) throws ToursException {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    public User updateUser(User user) throws ToursException {
        // (6) update() no existe en CrudRepository.
        // save() detecta que el ID ya existe y ejecuta UPDATE en lugar de INSERT.
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) throws ToursException {
        if (!user.isActive()) {
            throw new ToursException("User is already inactive");
        }
        if (user instanceof TourGuideUser) {
            if (routeRepository.isTourGuideAssignedToAnyRoute(user.getId())) {
                throw new ToursException("TourGuide is assigned to routes and cannot be deleted");
            }
        }
        if (user.getPurchaseList() != null && !user.getPurchaseList().isEmpty()) {
            user.setActive(false);
            userRepository.save(user);
        } else {
            userRepository.delete(user);
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
        return stopRepository.save(stop);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stop> getStopByNameStart(String name) {
        return stopRepository.findByNameStart(name);
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
        return routeRepository.save(route);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Route> getRouteById(Long id) {
        return routeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesBelowPrice(float price) {
        return routeRepository.findBelowPrice(price);
    }

    @Override
    public void assignDriverByUsername(String username, Long idRoute) throws ToursException {
        User user = userRepository.findByUsername(username);
        if (user == null || !(user instanceof DriverUser)) {
            throw new ToursException("Driver not found: " + username);
        }
        // (7) findById devuelve Optional — se usa orElseThrow para lanzar excepción
        // si no existe, en lugar de chequear null manualmente.
        Route route = routeRepository.findById(idRoute)
                .orElseThrow(() -> new ToursException("Route not found: " + idRoute));
        route.getDriverList().add((DriverUser) user);
        routeRepository.save(route);
    }

    @Override
    public void assignTourGuideByUsername(String username, Long idRoute) throws ToursException {
        User user = userRepository.findByUsername(username);
        if (user == null || !(user instanceof TourGuideUser)) {
            throw new ToursException("Tour guide not found: " + username);
        }
        Route route = routeRepository.findById(idRoute)
                .orElseThrow(() -> new ToursException("Route not found: " + idRoute));
        route.getTourGuideList().add((TourGuideUser) user);
        routeRepository.save(route);
    }

    @Override
    public void deleteRoute(Route route) throws ToursException {
        if (routeRepository.hasPurchases(route.getId())) {
            throw new ToursException("Cannot delete route with associated purchases");
        }
        routeRepository.delete(route);
    }

    // -------------------------------------------------------------------------
    // SUPPLIER
    // -------------------------------------------------------------------------

    @Override
    public Supplier createSupplier(String businessName, String authorizationNumber) throws ToursException {
        if (supplierRepository.findByAuthorizationNumber(authorizationNumber) != null) {
            throw new ToursException("Authorization number already exists: " + authorizationNumber);
        }
        Supplier supplier = new Supplier();
        supplier.setBusinessName(businessName);
        supplier.setAuthorizationNumber(authorizationNumber);
        return supplierRepository.save(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> getSupplierByAuthorizationNumber(String authorizationNumber) {
        return Optional.ofNullable(supplierRepository.findByAuthorizationNumber(authorizationNumber));
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
        Service saved = serviceRepository.save(service);
        supplier.getServices().add(saved);
        return saved;
    }

    @Override
    public Service updateServicePriceById(Long id, float newPrice) throws ToursException {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ToursException("Service not found: " + id));
        service.setPrice(newPrice);
        return serviceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Service> getServiceByNameAndSupplierId(String name, Long id) throws ToursException {
        return Optional.ofNullable(serviceRepository.findByNameAndSupplierId(name, id));
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
        long count = purchaseRepository.countByRoute(route.getId());
        if (count >= route.getMaxNumberUsers()) {
            throw new ToursException("Route is at capacity");
        }
        Purchase purchase = new Purchase();
        purchase.setCode(code);
        purchase.setDate(date);
        purchase.setRoute(route);
        purchase.setUser(user);
        purchase.setTotalPrice(route.getPrice());
        Purchase saved = purchaseRepository.save(purchase);
        user.getPurchaseList().add(saved);
        return saved;
    }

    @Override
    public ItemService addItemToPurchase(Service service, int quantity, Purchase purchase) throws ToursException {
        ItemService item = new ItemService();
        item.setService(service);
        item.setQuantity(quantity);
        item.setPurchase(purchase);
        purchase.getItemServiceList().add(item);
        service.getItemServiceList().add(item);
        purchase.setTotalPrice(purchase.getTotalPrice() + service.getPrice() * quantity);
        // (8) saveItem() desapareció de PurchaseRepository.
        // ItemService es su propia entidad y tiene su propio repositorio.
        return itemServiceRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Purchase> getPurchaseByCode(String code) {
        return Optional.ofNullable(purchaseRepository.findByCode(code));
    }

    @Override
    public void deletePurchase(Purchase purchase) throws ToursException {
        purchaseRepository.delete(purchase);
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
        // (9) update() reemplazado por save(). La Review se persiste via cascada
        // desde Purchase, por lo que alcanza con guardar el Purchase.
        purchaseRepository.save(purchase);
        return review;
    }

    // -------------------------------------------------------------------------
    // QUERIES
    // -------------------------------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchasesOfUsername(String username) {
        return purchaseRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserSpendingMoreThan(float mount) {
        return userRepository.getUserSpendingMoreThan(mount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supplier> getTopNSuppliersInPurchases(int n) {
        // (10) findTopNByPurchases recibe Pageable en lugar de un int.
        // PageRequest.of(0, n) significa: página 0, tamaño n — equivale al LIMIT n anterior.
        return supplierRepository.findTopNByPurchases(PageRequest.of(0, n));
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountOfPurchasesBetweenDates(Date start, Date end) {
        return purchaseRepository.getCountOfPurchasesBetweenDates(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutesWithStop(Stop stop) {
        // (11) El repositorio recibe Long en lugar de Stop para evitar
        // dependencia del objeto entero en la query JPQL.
        return routeRepository.getRoutesWithStop(stop.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMaxStopOfRoutes() {
        return (long) routeRepository.getMaxStopOfRoutes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getRoutsNotSell() {
        return routeRepository.getRoutsNotSell();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Route> getTop3RoutesWithMaxRating() {
        // (12) Mismo patrón que findTopNByPurchases: Pageable reemplaza setMaxResults(3).
        return routeRepository.getTop3RoutesWithMaxRating(PageRequest.of(0, 3));
    }

    @Override
    @Transactional(readOnly = true)
    public Service getMostDemandedService() {
        return serviceRepository.getMostDemandedService();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourGuideUser> getTourGuidesWithRating1() {
        return userRepository.getTourGuidesWithRating1();
    }
}
