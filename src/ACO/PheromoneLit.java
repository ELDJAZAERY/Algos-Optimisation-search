package ACO;

import java.util.Random;


import solverSat.CNF;
import solverSat.Litteral;

public class PheromoneLit {
	
	private float pheromonTrue = (float) 0.1 , pheromonFalse = (float) 0.1 ;
	
	public PheromoneLit(){ }

	public float getPheromonTrue() {
		return pheromonTrue;
	}

	public void setPheromonTrue(float pheromonTrue) {
		this.pheromonTrue = pheromonTrue;
	}

	public float getPheromonFalse() {
		return pheromonFalse;
	}

	public void setPheromonFalse(float pheromonFalse) {
		this.pheromonFalse = pheromonFalse;
	}
	
	
	public Byte getValLit(int lit){
		
		if (pheromonFalse != pheromonTrue) {
			Litteral literalP = ACS.sat.getLitterals().get(ACS.sat.getLitterals().indexOf(new Litteral(lit)));
			Litteral literalN = ACS.sat.getLitterals().get(ACS.sat.getLitterals().indexOf(new Litteral(-lit)));

			int niP = literalP.getClause().size() / CNF.NbClauseTotal ;
			int niN = literalN.getClause().size() / CNF.NbClauseTotal ;
			
			float niP_Beta = (float) Math.pow(niP,ACS.beta);
			float niN_Beta = (float) Math.pow(niN,ACS.beta);
			
			if( ( Math.pow(pheromonTrue,ACS.alpha) * niP_Beta ) > ( Math.pow(pheromonFalse,ACS.alpha) * niN_Beta ) ){
				maj_Local(true);
				return 1;
			}else{
				maj_Local(false);
				return 0;			
			}
		} else {
			Random rnd = new Random();
			boolean random = rnd.nextBoolean();
			if(random) {maj_Local(true); return 1 ; } else { maj_Local(false); return 0; }
		}

		
	}
	
	
	public void maj_Local(boolean pherTrue){
		if(pherTrue)
			pheromonTrue = (float) (((1 - ACS.p) * pheromonTrue ) + (ACS.p * ACS.pheromone0));
		else
			pheromonFalse = (float) (((1 - ACS.p) * pheromonFalse) + (ACS.p * ACS.pheromone0));
	}
	
	
	public void maj(Byte val){
		float delta = 0;
		if(val == 1){
			delta = ( (1 - ACS.p) * pheromonTrue ) - pheromonTrue ;
			pheromonTrue = ((1 - ACS.p) * pheromonTrue ) + (ACS.p*delta);
		}else{
			delta = ( (1 - ACS.p) * pheromonFalse ) - pheromonFalse ;
			pheromonFalse = ((1 - ACS.p) * pheromonFalse ) + (ACS.p*delta);			
		}
		
	}
	
	
}
