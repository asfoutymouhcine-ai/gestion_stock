package ma.emsi.stockpulse.service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import ma.emsi.stockpulse.exception.AuthentificationException;
import ma.emsi.stockpulse.model.Administrateur;
import ma.emsi.stockpulse.model.Magasinier;
import ma.emsi.stockpulse.model.Role;
import ma.emsi.stockpulse.model.Utilisateur;
import ma.emsi.stockpulse.repository.IRepository;

/**
 * UC2: user account management; UC3: password change.
 */
public class UtilisateurService {
    private final IRepository<Utilisateur> repository;
    private final AuthService authService;
    private final AtomicLong compteur = new AtomicLong(0);

    public UtilisateurService(IRepository<Utilisateur> repository, AuthService authService) {
        this.repository = repository;
        this.authService = authService;
    }

    /** Used only by Main to create the initial data. */
    public Utilisateur creerCompteInitial(String identifiant, String motDePasse, Role role) {
        return creerInterne(identifiant, motDePasse, role);
    }

    public Utilisateur creer(String identifiant, String motDePasse, Role role)
            throws AuthentificationException {
        authService.exigerAdministrateur();
        return creerInterne(identifiant, motDePasse, role);
    }

    private Utilisateur creerInterne(String identifiant, String motDePasse, Role role) {
        if (identifiant == null || identifiant.isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (motDePasse == null || motDePasse.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role is required.");
        }

        boolean existe = repository.findAll().stream()
                .anyMatch(u -> u.getIdentifiant().equalsIgnoreCase(identifiant.trim()));
        if (existe) {
            throw new IllegalArgumentException("This username already exists.");
        }

        long id = compteur.incrementAndGet();
        Utilisateur u = (role == Role.ADMINISTRATEUR)
                ? new Administrateur(id, identifiant.trim(), motDePasse)
                : new Magasinier(id, identifiant.trim(), motDePasse);
        return repository.save(u);
    }

    public List<Utilisateur> lister() throws AuthentificationException {
        authService.exigerAdministrateur();
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Utilisateur::getIdentifiant, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    public void supprimer(long id) throws AuthentificationException {
        authService.exigerAdministrateur();
        if (authService.getUtilisateurCourant().getId() == id) {
            throw new IllegalArgumentException("You cannot delete your own account.");
        }
        if (!repository.deleteById(id)) {
            throw new IllegalArgumentException("User not found.");
        }
    }

    public void debloquer(long id) throws AuthentificationException {
        authService.exigerAdministrateur();
        Utilisateur u = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        u.debloquer();
        repository.save(u);
    }

    /** UC3: the logged-in user changes their own password. */
    public void changerMotDePasse(String ancien, String nouveau, String confirmation)
            throws AuthentificationException {
        Utilisateur u = authService.getUtilisateurCourant();
        if (u == null) {
            throw new AuthentificationException("You must be logged in.");
        }
        if (!u.verifierMotDePasse(ancien)) {
            throw new AuthentificationException("The old password is incorrect.");
        }
        if (nouveau == null || nouveau.length() < 6) {
            throw new IllegalArgumentException("The new password must be at least 6 characters long.");
        }
        if (!nouveau.equals(confirmation)) {
            throw new IllegalArgumentException("The confirmation does not match.");
        }
        u.changerMotDePasse(nouveau);
        repository.save(u);
    }
}
