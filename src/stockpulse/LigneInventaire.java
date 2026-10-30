package stockpulse;

public class LigneInventaire {

    private int quantiteTheorique;
    private int quantiteReelle;

    public LigneInventaire(int quantiteTheorique, int quantiteReelle) {
        this.quantiteTheorique = quantiteTheorique;
        this.quantiteReelle = quantiteReelle;
    }

  

   



	public int getQuantiteTheorique() {
		return quantiteTheorique;
	}



	public void setQuantiteTheorique(int quantiteTheorique) {
		this.quantiteTheorique = quantiteTheorique;
	}



	public int getQuantiteReelle() {
		return quantiteReelle;
	}



	public void setQuantiteReelle(int quantiteReelle) {
		this.quantiteReelle = quantiteReelle;
	}
	
	
	
	 public int getEcart() {
	        return quantiteReelle - quantiteTheorique;
	    }
}
