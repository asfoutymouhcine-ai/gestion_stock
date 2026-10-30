package stockpulse;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProduitService {
    private final IRepository<Produit> produitRepo;

    public ProduitService(IRepository<Produit> produitRepo) {
        this.produitRepo = produitRepo;
    }

    public void ajouterProduit(Produit p) {
        produitRepo.sauvegarder(p.getReference(), p);
    }

    public List<Produit> listerTous() {
        return produitRepo.trouverTous();
    }

    public Optional<Produit> chercherParRef(String ref) {
        return produitRepo.trouverParId(ref);
    }

    public List<Produit> chercherParNom(String motCle) {
        return produitRepo.trouverTous().stream()
                .filter(p -> p.getNom().toLowerCase().contains(motCle.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Produit> trierParNom() {
        return produitRepo.trouverTous().stream()
                .sorted(Comparator.comparing(Produit::getNom))
                .collect(Collectors.toList());
    }

    public List<Produit> trierParPrix() {
        return produitRepo.trouverTous().stream()
                .sorted(Comparator.comparingDouble(Produit::getPrix))
                .collect(Collectors.toList());
    }

    public List<Produit> trierParStock() {
        return produitRepo.trouverTous().stream()
                .sorted(Comparator.comparingInt(Produit::getQuantiteStock))
                .collect(Collectors.toList());
    }

    public void supprimerProduit(String ref) throws EntiteIntrouvableException {
        if (!produitRepo.trouverParId(ref).isPresent()) {
            throw new EntiteIntrouvableException("Produit introuvable avec la référence: " + ref);
        }
        produitRepo.supprimer(ref);
    }
}