package unlp.info.bd2.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private float totalKm;

    @Column(nullable = false)
    private int maxNumberUsers;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private List<Stop> stops;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "route_driver", joinColumns = @JoinColumn(name = "route_id"), inverseJoinColumns = @JoinColumn(name = "driver_id"))
    private List<DriverUser> driverList;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "route_tour_guide", joinColumns = @JoinColumn(name = "route_id"), inverseJoinColumns = @JoinColumn(name = "tour_guide_id"))
    private List<TourGuideUser> tourGuideList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(float totalKm) {
        this.totalKm = totalKm;
    }

    public int getMaxNumberUsers() {
        return maxNumberUsers;
    }

    public void setMaxNumberUsers(int maxNumberUsers) {
        this.maxNumberUsers = maxNumberUsers;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<DriverUser> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<DriverUser> driverList) {
        this.driverList = driverList;
    }

    public List<TourGuideUser> getTourGuideList() {
        return tourGuideList;
    }

    public void setTourGuideList(List<TourGuideUser> tourGuideList) {
        this.tourGuideList = tourGuideList;
    }

    public void addDriver(DriverUser driver) {
        if (this.driverList == null) {
            this.driverList = new ArrayList<>();
        }
        this.driverList.add(driver);
    }

    public void addTourGuide(TourGuideUser guide) {
        if (this.tourGuideList == null) {
            this.tourGuideList = new ArrayList<>();
        }
        this.tourGuideList.add(guide);
    }
}
