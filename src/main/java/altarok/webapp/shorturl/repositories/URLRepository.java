package altarok.webapp.shorturl.repositories;

import altarok.webapp.shorturl.models.URL;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface URLRepository extends CrudRepository<URL, Long> {
    Optional<URL> findById(Long id);

    Optional<URL> findByKey(String key);
}
