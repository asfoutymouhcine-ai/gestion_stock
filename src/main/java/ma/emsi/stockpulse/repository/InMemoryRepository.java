package ma.emsi.stockpulse.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * In-memory implementation of IRepository, backed by a HashMap.
 * (Temporary version by Youssef: can be replaced by Saad's if needed,
 * as long as the InMemoryRepository(Function&lt;T, Long&gt;) constructor is kept.)
 */
public class InMemoryRepository<T> implements IRepository<T> {
    private final Map<Long, T> data = new HashMap<>();
    private final Function<T, Long> idExtractor;

    public InMemoryRepository(Function<T, Long> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public T save(T entity) {
        data.put(idExtractor.apply(entity), entity);
        return entity;
    }

    @Override
    public Optional<T> findById(long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean deleteById(long id) {
        return data.remove(id) != null;
    }
}
