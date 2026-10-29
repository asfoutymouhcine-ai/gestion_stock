package ma.emsi.stockpulse;

import java.util.Locale;

import javafx.application.Application;
import ma.emsi.stockpulse.model.Role;
import ma.emsi.stockpulse.model.Utilisateur;
import ma.emsi.stockpulse.repository.IRepository;
import ma.emsi.stockpulse.repository.InMemoryRepository;
import ma.emsi.stockpulse.service.AuthService;
import ma.emsi.stockpulse.service.UtilisateurService;
import ma.emsi.stockpulse.ui.MenuPrincipal;

/**
 * Common entry point. Main does not extend Application, to avoid the
 * "JavaFX runtime components are missing" error when launching from Eclipse.
 *
 * Each team member writes their own data-initialisation method here.
 */
public class Main {
    public static void main(String[] args) {
        // Keeps JavaFX built-in texts (dialog buttons, table placeholders) in English.
        Locale.setDefault(Locale.ENGLISH);

        IRepository<Utilisateur> repoUtilisateurs = new InMemoryRepository<>(Utilisateur::getId);
        AuthService authService = new AuthService(repoUtilisateurs);
        UtilisateurService utilisateurService = new UtilisateurService(repoUtilisateurs, authService);

        initialiserUtilisateurs(utilisateurService);   // Youssef
        // initialiserCatalogue(...);                  // Saad
        // initialiserStock(...);                      // Abderrahmen
        // initialiserInventaire(...);                 // Mohcine

        MenuPrincipal.configurer(authService, utilisateurService);
        Application.launch(MenuPrincipal.class, args);
    }

    private static void initialiserUtilisateurs(UtilisateurService us) {
        us.creerCompteInitial("admin", "admin123", Role.ADMINISTRATEUR);
        us.creerCompteInitial("magasinier", "mag123", Role.MAGASINIER);
    }
}
