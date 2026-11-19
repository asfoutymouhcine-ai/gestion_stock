package stockpulse;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        //collection declare list avec arraylist 
        List<LigneInventaire> lignes= new ArrayList<>();

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
            	
            	//instancie avec la methode class anonyme 
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
            	
            	
            	//instancie avec la methode lambda 
            	CritereInventaire critereManque = ligne -> ligne.getEcart() < 0;
            	
            	
                 
            	LigneInventaire ligneTest =
            	        new LigneInventaire(theorique, reelle);
            	
            	//ajouter a la collection 
            	lignes.add(ligneTest);
            	
            	// Afficher le nombre d'éléments dans la collection
            	System.out.println("Nombre de lignes : " + lignes.size());
            	
            	//stream pour compter le nombre d'ecart 
            	long nbecart = lignes.stream().filter(ligne -> ligne.getEcart() != 0).count();
            	System.out.println("nombre d'ecart " +nbecart);
            	
            	//une stream pour calculer s'il exesiste un manquant
            	long nbmanquant = lignes.stream().filter(ligne -> ligne.getEcart() < 0).count();
            	System.out.println("nbr de manquants" +nbmanquant);
            	
            	//une stream qui clacule le nbr de ecart 
            	int totalmanquant= lignes.stream().filter(ligne -> ligne.getEcart()<0).mapToInt(ligne->ligne.getEcart()).sum();
                System.out.println("total de valeur manquant" +totalmanquant);
            	
            	
            	
            	
               //revenr boolen si l'ecart execicte true avec la class criter est la methode tester 
            	System.out.println("Presence d'un ecart : " + critere.tester(ligneTest));
            	
            	
            	RapportService r = new RapportService();
            	r.exportRapport();
            	r.LireRapport();
            	r.sauvegarderInventaire(inventaire);
            	r.chargerInventaire();
            }

        } catch (NumberFormatException e) {
            System.out.println("Erreur : entrez des nombres entiers !");
        }

        clavier.close();
    }
}