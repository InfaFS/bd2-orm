package unlp.info.bd2.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import unlp.info.bd2.model.Stop;

import java.util.List;

public interface StopRepository extends CrudRepository<Stop, Long> {

    @Query("SELECT s FROM Stop s WHERE s.name LIKE :prefix%")
    List<Stop> findByNameStart(@Param("prefix") String prefix);
}
