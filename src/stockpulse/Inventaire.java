package stockpulse;

import java.time.LocalDate;
import java.util.ArrayList;

public class Inventaire {

    private long id;
    private LocalDate date;
    private ArrayList<LigneInventaire> lignes = new ArrayList<>();

    public Inventaire(long id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void ajouterLigne(int quantiteTheorique, int quantiteReelle) {
        LigneInventaire ligne =
                new LigneInventaire(quantiteTheorique, quantiteReelle);

        lignes.add(ligne);
    }

    public void afficherEcarts() {
        for (LigneInventaire ligne : lignes) {
            System.out.println("Ecart : " + ligne.getEcart());
        }
    }
}