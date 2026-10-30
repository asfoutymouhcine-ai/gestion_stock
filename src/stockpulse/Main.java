package stockpulse;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("=======================================");
        System.out.println("   STOCKPULSE - CATALOGUE PRODUITS     ");
        System.out.println("=======================================");

        // Initialisation des repositories
        IRepository<Produit> produitRepo = new InMemoryRepository<>();
        IRepository<Categorie> categorieRepo = new InMemoryRepository<>();
        IRepository<Emplacement> emplacementRepo = new InMemoryRepository<>();

        // Initialisation des services
        CategorieService categorieService = new CategorieService(categorieRepo, emplacementRepo);
        ProduitService produitService = new ProduitService(produitRepo);

        // Données de démonstration
        Categorie cat1 = new Categorie("C1", "Informatique");
        Categorie cat2 = new Categorie("C2", "Bureautique");
        Emplacement emp1 = new Emplacement("E1", "Rayon-A1");
        Emplacement emp2 = new Emplacement("E2", "Stock-Principal");

        categorieService.ajouterCategorie(cat1);
        categorieService.ajouterCategorie(cat2);
        categorieService.ajouterEmplacement(emp1);
        categorieService.ajouterEmplacement(emp2);

        Produit p1 = new Produit("P001", "PC Portable ROG", 12000.0, 5, 2, UniteMesure.PIECE, cat1, emp1);
        Produit p2 = new Produit("P002", "Souris Sans Fil", 250.0, 20, 5, UniteMesure.PIECE, cat1, emp1);
        Produit p3 = new Produit("P003", "Rame Papier A4", 45.0, 50, 10, UniteMesure.CARTON, cat2, emp2);

        produitService.ajouterProduit(p1);
        produitService.ajouterProduit(p2);
        produitService.ajouterProduit(p3);

        // Lancement du menu Catalogue
        MenuCatalogue menuCatalogue = new MenuCatalogue(produitService, categorieService);
        menuCatalogue.afficherMenu();
    }
}