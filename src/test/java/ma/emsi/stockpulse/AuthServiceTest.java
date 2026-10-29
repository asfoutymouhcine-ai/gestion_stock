package ma.emsi.stockpulse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ma.emsi.stockpulse.exception.AuthentificationException;
import ma.emsi.stockpulse.model.Role;
import ma.emsi.stockpulse.model.Utilisateur;
import ma.emsi.stockpulse.repository.IRepository;
import ma.emsi.stockpulse.repository.InMemoryRepository;
import ma.emsi.stockpulse.service.AuthService;
import ma.emsi.stockpulse.service.UtilisateurService;

class AuthServiceTest {
    private AuthService auth;
    private UtilisateurService us;

    @BeforeEach
    void init() {
        IRepository<Utilisateur> repo = new InMemoryRepository<>(Utilisateur::getId);
        auth = new AuthService(repo);
        us = new UtilisateurService(repo, auth);
        us.creerCompteInitial("admin", "admin123", Role.ADMINISTRATEUR);
        us.creerCompteInitial("mag", "mag123456", Role.MAGASINIER);
    }

    @Test
    void connexionValide() throws Exception {
        assertTrue(auth.login("admin", "admin123").peutGererLeSysteme());
    }

    @Test
    void mauvaisMotDePasseRefuse() {
        assertThrows(AuthentificationException.class, () -> auth.login("admin", "faux"));
        assertFalse(auth.estConnecte());
    }

    @Test
    void troisEchecsBloquentLeCompte() {
        for (int i = 0; i < 3; i++) {
            assertThrows(AuthentificationException.class, () -> auth.login("admin", "faux"));
        }
        // même avec le bon mot de passe, le compte est bloqué
        assertThrows(AuthentificationException.class, () -> auth.login("admin", "admin123"));
    }

    @Test
    void administrateurPeutDebloquer() throws Exception {
        for (int i = 0; i < 3; i++) {
            assertThrows(AuthentificationException.class, () -> auth.login("mag", "faux"));
        }
        auth.login("admin", "admin123");
        long idMag = us.lister().stream()
                .filter(u -> u.getIdentifiant().equals("mag")).findFirst().get().getId();
        us.debloquer(idMag);
        auth.deconnecter();
        assertEquals("mag", auth.login("mag", "mag123456").getIdentifiant());
    }

    @Test
    void magasinierRefuseCommeAdmin() throws Exception {
        auth.login("mag", "mag123456");
        assertThrows(AuthentificationException.class, () -> auth.exigerAdministrateur());
        assertThrows(AuthentificationException.class, () -> us.lister());
    }

    @Test
    void changementMotDePasse() throws Exception {
        auth.login("mag", "mag123456");
        us.changerMotDePasse("mag123456", "nouveau123", "nouveau123");
        auth.deconnecter();
        assertThrows(AuthentificationException.class, () -> auth.login("mag", "mag123456"));
        assertEquals("mag", auth.login("mag", "nouveau123").getIdentifiant());
    }

    @Test
    void identifiantDejaUtilise() throws Exception {
        auth.login("admin", "admin123");
        assertThrows(IllegalArgumentException.class, () -> us.creer("MAG", "abcdef", Role.MAGASINIER));
    }
}
