package unlp.info.bd2.repositories;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Review;

import java.util.List;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Review save(Review review) {
        sessionFactory.getCurrentSession().save(review);
        return review;
    }

    @Override
    public Review findById(Long id) {
        return sessionFactory.getCurrentSession().get(Review.class, id);
    }

    @Override
    public List<Review> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Review", Review.class)
                .list();
    }

    @Override
    public void delete(Review review) {
        sessionFactory.getCurrentSession().delete(review);
    }

    @Override
    public Review update(Review review) {
        sessionFactory.getCurrentSession().update(review);
        return review;
    }
}
