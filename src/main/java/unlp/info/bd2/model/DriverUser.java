package unlp.info.bd2.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class DriverUser extends User {

    @Column(nullable = false)
    private String expedient;

    @ManyToMany(mappedBy = "driverList")
    private List<Route> routes;

    public String getExpedient() {
        return expedient;
    }

    public void setExpedient(String expedient) {
        this.expedient = expedient;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRouts(List<Route> routs) {
        this.routes = routs;
    }
}
