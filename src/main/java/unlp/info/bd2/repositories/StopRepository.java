package unlp.info.bd2.repositories;

import unlp.info.bd2.model.Stop;
import java.util.List;

public interface StopRepository {
    Stop save(Stop stop);
    Stop findById(Long id);
    List<Stop> findAll();
    void delete(Stop stop);
    Stop update(Stop stop);
    List<Stop> findByNameStart(String prefix);
}
