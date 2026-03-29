package unlp.info.bd2.repositories;

import unlp.info.bd2.model.Service;
import java.util.List;

public interface ServiceRepository {
    Service save(Service service);
    Service findById(Long id);
    List<Service> findAll();
    void delete(Service service);
    Service update(Service service);
}
