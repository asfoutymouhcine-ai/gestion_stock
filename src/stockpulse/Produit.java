package stockpulse;

public class Produit {
    private String reference;
    private String nom;
    private double prix;
    private int quantiteStock;
    private int seuilAlerte;
    private UniteMesure uniteMesure;
    private Categorie categorie;
    private Emplacement emplacement;

    public Produit(String reference, String nom, double prix, int quantiteStock, int seuilAlerte, 
                   UniteMesure uniteMesure, Categorie categorie, Emplacement emplacement) {
        if (prix <= 0) {
            throw new IllegalArgumentException("Le prix doit être strictement positif.");
        }
        if (seuilAlerte < 0) {
            throw new IllegalArgumentException("Le seuil d'alerte doit être positif ou nul.");
        }
        this.reference = reference;
        this.nom = nom;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.seuilAlerte = seuilAlerte;
        this.uniteMesure = uniteMesure;
        this.categorie = categorie;
        this.emplacement = emplacement;
    }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; }

    public int getSeuilAlerte() { return seuilAlerte; }
    public void setSeuilAlerte(int seuilAlerte) { this.seuilAlerte = seuilAlerte; }

    public UniteMesure getUniteMesure() { return uniteMesure; }
    public void setUniteMesure(UniteMesure uniteMesure) { this.uniteMesure = uniteMesure; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public Emplacement getEmplacement() { return emplacement; }
    public void setEmplacement(Emplacement emplacement) { this.emplacement = emplacement; }

    @Override
    public String toString() {
        return nom + " (Ref: " + reference + ", Stock: " + quantiteStock + ")";
    }
}