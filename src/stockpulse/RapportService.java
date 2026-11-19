package stockpulse;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RapportService {
	
	public void exportRapport() {
		
		try(FileOutputStream fos = new FileOutputStream("rapport_stock.txt")){
			
			String rapport="===Rapport Stockplus===";
			
			fos.write(rapport.getBytes());
			
			System.out.println("le rapport a ete exporte avec sucees ");
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public void LireRapport() {
		
		try(FileInputStream fis = new FileInputStream("D:\\gestion_stock\\rapport_stock.txt")){
			int caractere;
			 while((caractere = fis.read()) != -1) {
				 System.out.println((char) caractere);
			 }
			 
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		
		
	}
	//serialisation 
	public void sauvegarderInventaire(Inventaire inventaire) {

	    try (ObjectOutputStream oos =
	            new ObjectOutputStream(
	                new FileOutputStream("inventaire.ser"))) {

	        oos.writeObject(inventaire);

	        System.out.println("Inventaire sauvegardé !");

	    } catch (IOException e) {

	        System.out.println("Erreur : " + e.getMessage());
	    }
	}
	
	public Inventaire chargerInventaire() {

	    try (ObjectInputStream ois =
	            new ObjectInputStream(
	                new FileInputStream("inventaire.ser"))) {

	        Inventaire inventaire =
	                (Inventaire) ois.readObject();

	        System.out.println("Inventaire chargé avec succès !");

	        return inventaire;

	    } catch (IOException | ClassNotFoundException e) {

	        System.out.println("Erreur : " + e.getMessage());
	        return null;
	    }
	}

}
