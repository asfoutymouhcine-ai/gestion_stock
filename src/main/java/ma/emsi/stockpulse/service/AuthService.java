package ma.emsi.stockpulse.service;

import ma.emsi.stockpulse.exception.AuthentificationException;
import ma.emsi.stockpulse.model.Utilisateur;
import ma.emsi.stockpulse.repository.IRepository;

/**
 * UC1: authentication, counting of failed logins and account blocking.
 */
public class AuthService {
    public static final int MAX_ECHECS = 3;

    private final IRepository<Utilisateur> repository;
    private Utilisateur utilisateurCourant;

    public AuthService(IRepository<Utilisateur> repository) {
        this.repository = repository;
    }

    public Utilisateur login(String identifiant, String motDePasse) throws AuthentificationException {
        if (identifiant == null || identifiant.isBlank() || motDePasse == null || motDePasse.isEmpty()) {
            throw new AuthentificationException("Please enter your username and password.");
        }

        Utilisateur u = repository.findAll().stream()
                .filter(x -> x.getIdentifiant().equalsIgnoreCase(identifiant.trim()))
                .findFirst()
                .orElseThrow(() -> new AuthentificationException("Incorrect username or password."));

        if (u.isBloque()) {
            throw new AuthentificationException("Account blocked. Please contact an administrator.");
        }

        if (!u.verifierMotDePasse(motDePasse)) {
            u.incrementerEchecs();
            if (u.getEchecsConnexion() >= MAX_ECHECS) {
                u.bloquer();
                repository.save(u);
                throw new AuthentificationException(
                        "Account blocked after " + MAX_ECHECS + " failed login attempts.");
            }
            repository.save(u);
            throw new AuthentificationException("Incorrect username or password. Remaining attempts: "
                    + (MAX_ECHECS - u.getEchecsConnexion()));
        }

        u.reinitialiserEchecs();
        repository.save(u);
        utilisateurCourant = u;
        return u;
    }

    public void deconnecter() {
        utilisateurCourant = null;
    }

    public Utilisateur getUtilisateurCourant() {
        return utilisateurCourant;
    }

    public boolean estConnecte() {
        return utilisateurCourant != null;
    }

    /** Business rule: a Storekeeper cannot access Administrator functions. */
    public void exigerAdministrateur() throws AuthentificationException {
        if (!estConnecte() || !utilisateurCourant.peutGererLeSysteme()) {
            throw new AuthentificationException("Access denied: Administrator only.");
        }
    }
}
