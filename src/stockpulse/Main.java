package stockpulse;

import java.util.Scanner;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);

        System.out.println("=== StockPulse : Inventaire ===");

        try {
            System.out.print("Quantite theorique : ");
            int theorique = Integer.parseInt(clavier.nextLine());

            System.out.print("Quantite reelle : ");
            int reelle = Integer.parseInt(clavier.nextLine());

            if (theorique < 0 || reelle < 0) {
                System.out.println("Erreur : les quantites doivent etre positives ou nulles.");
            } else {
            	Inventaire inventaire = new Inventaire(1, LocalDate.now());

            	inventaire.ajouterLigne(theorique, reelle);

            	inventaire.afficherEcarts();
            	
            	CritereInventaire critere = new CritereInventaire() {

            	    @Override
            	    public boolean tester(LigneInventaire ligne) {
            	        return ligne.getEcart() != 0;
            	    }
            	};
            	//recupere l'ecarr negative s'il exesiste on va met true  
            	//Predicate<LigneInventaire> critereManque =
            	        //ligne -> ligne.getEcart() < 0;

            	//System.out.println("Presence d'un manque : "
            	        //+ critereManque.test(ligneTest));
            	
            	CritereInventaire critereManque = ligne -> ligne.getEcart() < 0;

            	LigneInventaire ligneTest =
            	        new LigneInventaire(theorique, reelle);

            	System.out.println("Presence d'un ecart : " + critere.tester(ligneTest));
            }

        } catch (NumberFormatException e) {
            System.out.println("Erreur : entrez des nombres entiers !");
        }

        clavier.close();
    }
}