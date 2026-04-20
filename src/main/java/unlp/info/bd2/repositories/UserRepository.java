package unlp.info.bd2.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import unlp.info.bd2.model.TourGuideUser;
import unlp.info.bd2.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT DISTINCT p.user FROM Purchase p WHERE p.totalPrice >= :amount AND p.user.active = true")
    List<User> getUserSpendingMoreThan(@Param("amount") float amount);

    @Query("SELECT DISTINCT g FROM Purchase p JOIN p.review r JOIN p.route route JOIN route.tourGuideList g WHERE r.rating = 1 AND g.active = true")
    List<TourGuideUser> getTourGuidesWithRating1();

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    void physicalDeleteById(@Param("id") Long id);
}
