package solverSat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;




public class CNF {

	private Vector<Clause>   clauses   = new Vector<>();
	private Vector<Litteral> litterals = new Vector<>();
	private int profond;
	// p cnf nblitterals nbClauses
	private int nbLitterals;
	
	public static int NbClauseTotal = 0;
	
	// getters & setters & initialization
	private void initialiserLitteraux(){
		// -nbMax --> +nbMax
		for(int i=-nbLitterals;i<=nbLitterals;i++){ if(i!=0) litterals.add(new Litteral(i)); }
	}

	private Litteral getLitt(int var){
		for(Litteral litt:litterals) if(litt.getVar()==var) return litt;
		return null;
	}
	
	private void chainageLiteralClauses(){
		for(Litteral litteral: litterals)
			for(Clause clause:clauses)
				if(clause.getLitteraux().contains(litteral)) litteral.addClause(clause);
	}

	public Vector<Litteral> getLitterals(){ return litterals;}
	
	public void setLitterals(Vector<Litteral> l){ litterals = l;}
	
	public int getProfond(){ return profond;}
	
	public int getNbLitts(){ return nbLitterals;}
	
	public void setProfond(int p){profond=p;}

	public Vector<Clause> getClauses(){ return this.clauses;}
		
	private void config(String ligne){
		int i=0; String[] configs = ligne.split(" ");
		for(String cnf:configs)
			if(!cnf.isEmpty()) if(i!=2) i++; else { nbLitterals = Integer.parseInt(cnf); break;}
	}

	public int getMaxVar(){ return nbLitterals; }
		
	
	public CNF (String nomF) throws Exception {

		String ligne;
		Vector <Clause>   clauses   = new Vector<>();
		
		BufferedReader bufR = new BufferedReader(new FileReader(nomF));	
			
		while ((ligne=bufR.readLine())!=null){
			if(ligne.startsWith("c")||ligne.startsWith("%")||ligne.startsWith("0")||ligne.startsWith("p")|| ligne.startsWith("P")|| ligne.isEmpty()){
				if(ligne.startsWith("P") || ligne.startsWith("p") ) { config(ligne); initialiserLitteraux();}
			}
			else{
				clauses.add(new Clause(getLitteralsOfClause(ligne)));
			}
		}
		bufR.close();
		
		this.clauses = clauses;	
		chainageLiteralClauses();
		
		NbClauseTotal = clauses.size();
	}

	
	public Vector<Litteral> getLitteralsOfClause(String ligne){
		Vector<Litteral> litters = new Vector<>();

		String[] Litterals = ligne.split(" ");
		
		for(String litt:Litterals)
			if(!litt.isEmpty() && Integer.parseInt(litt)!=0 ) litters.add(getLitt(Integer.parseInt(litt)));
				
		return litters;
	}

		
	@Override
	public String toString() {
		return "Sat [ " + clauses + " ]";
	}
	
}
