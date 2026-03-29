package unlp.info.bd2.repositories;

import unlp.info.bd2.model.Review;
import java.util.List;

public interface ReviewRepository {
    Review save(Review review);
    Review findById(Long id);
    List<Review> findAll();
    void delete(Review review);
    Review update(Review review);
}
