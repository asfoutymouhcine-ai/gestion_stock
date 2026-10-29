package ma.emsi.stockpulse.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Abstract parent class of all users; holds the login information and the
 * role. The password is stored hashed (SHA-256).
 */
public abstract class Utilisateur {
    private final long id;
    private final String identifiant;
    private String motDePasseHache;
    private final Role role;
    private int echecsConnexion;
    private boolean bloque;

    protected Utilisateur(long id, String identifiant, String motDePasse, Role role) {
        this.id = id;
        this.identifiant = identifiant;
        this.motDePasseHache = hasher(motDePasse);
        this.role = role;
    }

    /** Business rule: only an Administrator can access the management menus. */
    public abstract boolean peutGererLeSysteme();

    public boolean verifierMotDePasse(String motDePasse) {
        return motDePasse != null && motDePasseHache.equals(hasher(motDePasse));
    }

    public void changerMotDePasse(String nouveau) {
        this.motDePasseHache = hasher(nouveau);
    }

    public void incrementerEchecs() {
        echecsConnexion++;
    }

    public void reinitialiserEchecs() {
        echecsConnexion = 0;
    }

    public void bloquer() {
        bloque = true;
    }

    public void debloquer() {
        bloque = false;
        echecsConnexion = 0;
    }

    public long getId() {
        return id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public Role getRole() {
        return role;
    }

    public int getEchecsConnexion() {
        return echecsConnexion;
    }

    public boolean isBloque() {
        return bloque;
    }

    private static String hasher(String texte) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(texte.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {
        return identifiant + " (" + role + ")";
    }
}
