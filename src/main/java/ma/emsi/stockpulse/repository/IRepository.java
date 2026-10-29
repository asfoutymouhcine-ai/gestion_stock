package ma.emsi.stockpulse.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD interface shared by all services.
 */
public interface IRepository<T> {
    T save(T entity);                 // creates or updates
    Optional<T> findById(long id);
    List<T> findAll();
    boolean deleteById(long id);
}
