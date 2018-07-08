
package solverSat;
import java.util.Vector;


public class Clause {
	
	private Vector <Litteral> Litteraux;
	
	public Clause(Vector <Litteral> Litteraux){ this.Litteraux = Litteraux;}

	@SuppressWarnings("unchecked")
	public Clause(Clause c){ this.Litteraux = (Vector<Litteral>) c.Litteraux.clone();}
	
	public Vector <Litteral>  getLitteraux(){ return Litteraux;}
	
	public boolean clauseSat(Litteral l){
		if(this.getLitteraux().contains(l)) return true;
		return false;
	}

	@Override
	public String toString() {
		return " " + Litteraux + " \n ";
	}


	@Override
	public boolean equals(Object obj) {
		if(this==obj) return true;
		if(obj==null || !(obj instanceof Clause) ) return false;
	
		Clause clause =(Clause) obj;
	
		if(clause.Litteraux.size() != Litteraux.size()) return false;
		
		for(Litteral litt:clause.Litteraux)
			if(!Litteraux.contains(litt)) return false;
				
		return true;
	}
	
	public boolean litterauxOppose(Litteral litteral){
		int x = litteral.getVar() ;
		Litteral nonLit = new Litteral(-x);
		
		return Litteraux.contains(litteral) && Litteraux.contains(nonLit) ;
	}
	
}
