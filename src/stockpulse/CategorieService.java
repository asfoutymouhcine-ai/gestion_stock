package stockpulse;

import java.util.List;

public class CategorieService {
    private final IRepository<Categorie> categorieRepo;
    private final IRepository<Emplacement> emplacementRepo;

    public CategorieService(IRepository<Categorie> categorieRepo, IRepository<Emplacement> emplacementRepo) {
        this.categorieRepo = categorieRepo;
        this.emplacementRepo = emplacementRepo;
    }

    public void ajouterCategorie(Categorie c) {
        categorieRepo.sauvegarder(c.getId(), c);
    }

    public List<Categorie> listerCategories() {
        return categorieRepo.trouverTous();
    }

    public Categorie chercherCategorieParId(String id) throws EntiteIntrouvableException {
        return categorieRepo.trouverParId(id)
                .orElseThrow(() -> new EntiteIntrouvableException("Categorie introuvable avec ID: " + id));
    }

    public void ajouterEmplacement(Emplacement e) {
        emplacementRepo.sauvegarder(e.getId(), e);
    }

    public List<Emplacement> listerEmplacements() {
        return emplacementRepo.trouverTous();
    }

    public Emplacement chercherEmplacementParId(String id) throws EntiteIntrouvableException {
        return emplacementRepo.trouverParId(id)
                .orElseThrow(() -> new EntiteIntrouvableException("Emplacement introuvable avec ID: " + id));
    }
}