package ma.emsi.stockpulse.model;

/**
 * User in charge of the day-to-day stock operations.
 */
public class Magasinier extends Utilisateur {
    public Magasinier(long id, String identifiant, String motDePasse) {
        super(id, identifiant, motDePasse, Role.MAGASINIER);
    }

    @Override
    public boolean peutGererLeSysteme() {
        return false;
    }
}
