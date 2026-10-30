package stockpulse;

public class CritereAvecEcart implements CritereInventaire {

	@Override
	public boolean tester(LigneInventaire ligne) {
		// TODO Auto-generated method stub
		return ligne.getEcart() != 0;
	}

}
