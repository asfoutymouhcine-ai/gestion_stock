package ma.emsi.stockpulse.model;

/**
 * User with management rights (users, catalog, suppliers, reports).
 */
public class Administrateur extends Utilisateur {
    public Administrateur(long id, String identifiant, String motDePasse) {
        super(id, identifiant, motDePasse, Role.ADMINISTRATEUR);
    }

    @Override
    public boolean peutGererLeSysteme() {
        return true;
    }
}
