package stockpulse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<T> implements IRepository<T> {
    private final Map<String, T> stockage = new HashMap<>();

    @Override
    public void sauvegarder(String id, T entite) {
        stockage.put(id, entite);
    }

    @Override
    public Optional<T> trouverParId(String id) {
        return Optional.ofNullable(stockage.get(id));
    }

    @Override
    public List<T> trouverTous() {
        return new ArrayList<>(stockage.values());
    }

    @Override
    public void supprimer(String id) {
        stockage.remove(id);
    }
}