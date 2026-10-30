package stockpulse;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    void sauvegarder(String id, T entite);
    Optional<T> trouverParId(String id);
    List<T> trouverTous();
    void supprimer(String id);
}