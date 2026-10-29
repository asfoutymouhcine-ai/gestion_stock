package ma.emsi.stockpulse.model;

/**
 * Application roles.
 */
public enum Role {
    ADMINISTRATEUR("Administrator"),
    MAGASINIER("Storekeeper");

    private final String libelle;

    Role(String libelle) {
        this.libelle = libelle;
    }

    /** Display name of the role, shown in the user interface. */
    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
