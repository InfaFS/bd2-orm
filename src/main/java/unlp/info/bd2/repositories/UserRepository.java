package unlp.info.bd2.repositories;

import unlp.info.bd2.model.User;
import java.util.List;

public interface UserRepository {
    User save(User user);

    User findById(Long id);

    List<User> findAll();

    void delete(User user);

    User update(User user);

    User findByUsername(String username);

    List<User> getUserSpendingMoreThan(float amount);
}
