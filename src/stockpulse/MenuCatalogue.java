package stockpulse;

import java.util.List;
import java.util.Scanner;

public class MenuCatalogue {
    private final ProduitService produitService;
    private final CategorieService categorieService;
    private final Scanner scanner;

    public MenuCatalogue(ProduitService produitService, CategorieService categorieService) {
        this.produitService = produitService;
        this.categorieService = categorieService;
        this.scanner = new Scanner(System.in);
    }

    public void afficherMenu() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n=== MENU CATALOGUE PRODUITS ===");
            System.out.println("1. Lister tous les produits");
            System.out.println("2. Rechercher un produit par nom");
            System.out.println("3. Trier les produits par prix");
            System.out.println("4. Trier les produits par nom");
            System.out.println("5. Supprimer un produit");
            System.out.println("0. Quitter");
            System.out.print("Choix: ");
            try {
                choix = Integer.parseInt(scanner.nextLine());
                switch (choix) {
                    case 1:
                        listerProduits();
                        break;
                    case 2:
                        rechercherProduit();
                        break;
                    case 3:
                        trierParPrix();
                        break;
                    case 4:
                        trierParNom();
                        break;
                    case 5:
                        supprimerProduit();
                        break;
                    case 0:
                        System.out.println("Retour au menu principal.");
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
    }

    private void listerProduits() {
        List<Produit> produits = produitService.listerTous();
        if (produits.isEmpty()) {
            System.out.println("Aucun produit dans le catalogue.");
        } else {
            produits.forEach(System.out::println);
        }
    }

    private void rechercherProduit() {
        System.out.print("Entrez le mot cle du nom: ");
        String motCle = scanner.nextLine();
        List<Produit> res = produitService.chercherParNom(motCle);
        if (res.isEmpty()) {
            System.out.println("Aucun produit trouve.");
        } else {
            res.forEach(System.out::println);
        }
    }

    private void trierParPrix() {
        produitService.trierParPrix().forEach(System.out::println);
    }

    private void trierParNom() {
        produitService.trierParNom().forEach(System.out::println);
    }

    private void supprimerProduit() {
        System.out.print("Entrez la reference du produit a supprimer: ");
        String ref = scanner.nextLine();
        try {
            produitService.supprimerProduit(ref);
            System.out.println("Produit supprime avec succes.");
        } catch (EntiteIntrouvableException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}