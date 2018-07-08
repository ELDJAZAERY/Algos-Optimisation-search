package solverSat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;


public class Litteral {
	
	private int var;
	private ArrayList<Clause> clauses = new ArrayList<>();

	
	public ArrayList<Clause> getClause() {return clauses;}
	public void addClause(Clause clause) { clauses.add(clause);}
	
	public void setClause(ArrayList<Clause> clause) {this.clauses = clause;}

	public void setVar(int var) {this.var = var;}

	public Litteral(int v){var = v;}
	
	public int getVar(){return var;}

	
	@Override
	public String toString() {
		return " " + var +" ";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof Litteral) ) return false;
		Litteral litteral = (Litteral) o;
		return var == litteral.var;
	}


	public static Litteral random (Vector<Litteral> v){
		if(v.size()>0){
			Random rand = new Random();
			int littRand = rand.nextInt(v.size());
			Litteral litteral = v.get(littRand);
			return litteral;
		}
		return null;
	}
	
/*	public static Litteral getContraire(Vector<Litteral> v, Litteral x){
		for(Litteral litteral:v) if(litteral.var == -x.var) return litteral;
		return null;
	}*/
	
	public static Litteral getContraire(Vector<Litteral> v,Litteral x){
		return v.get(v.indexOf(new Litteral(-x.var)));
	}
	
	
}
