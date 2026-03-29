package unlp.info.bd2.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("TOUR_GUIDE")
public class TourGuideUser extends User {

    @Column(nullable = false)
    private String education;

    @ManyToMany(mappedBy = "tourGuideList")
    private List<Route> routes;

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

}
