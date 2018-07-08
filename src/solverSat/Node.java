package solverSat;

import java.util.ArrayList;
import java.util.Vector;

public class Node implements Comparable<Object> {
	
	private Vector<Clause>   clauses   = new Vector<>();
	private Vector<Litteral> litterals = new Vector<>();
	
	private Litteral lit;
	private int frequenceLit = 0;

	private int heuristique =0 ;
	
	
	public int getHeuristique() {return heuristique;}

	private boolean contradiction = false;
	public boolean contradiction(){return contradiction;}

	public void tauxDeSat(){
		int tauxSat = ( 100 * (CNF.NbClauseTotal - clauses.size()) ) /  CNF.NbClauseTotal ;
		MetdsRechGraph.tauxDeSat = Math.max(tauxSat, MetdsRechGraph.tauxDeSat) ;
	}
	
	@SuppressWarnings("unchecked")
	public Node(Vector<Clause> clauses, Vector<Litteral> litterals) {
		this.clauses = (Vector<Clause>) clauses.clone();
		this.litterals = (Vector<Litteral>) litterals.clone();
	}

	@SuppressWarnings("unchecked")
	public Node(Node node , Litteral litteral) {
		this.clauses = (Vector<Clause>) node.clauses.clone();
		this.litterals = (Vector<Litteral>) node.litterals.clone();
		
		clauses.removeAll(litteral.getClause());
		
		litterals.remove(new Litteral(-litteral.getVar()));
		litterals.remove(litteral);
		
		this.lit = litteral;
		tauxDeSat();
		setHeuristique();
	}
	
	
	// For BSO && ACO
	public Node(Node node , int litteralvals) {
		this.clauses = node.clauses;
		this.litterals = node.litterals;

		Litteral litteral = litterals.get(litterals.indexOf(new Litteral(litteralvals)));
		if(litteral==null) return;
		
		
		clauses.removeAll(litteral.getClause());
		
		litterals.remove(new Litteral(-litteral.getVar()));
		litterals.remove(litteral);
				
	}

	
	private void frequenceLit(){
		int nb =0 ;
		for(Clause clause:clauses)
			if(lit.getClause().contains(clause))
				nb++;
		
		frequenceLit = nb ;
	}
	
	private void setHeuristique() {
        frequenceLit();
        int k = CNF.NbClauseTotal - this.clauses.size();
        this.heuristique =k + frequenceLit;
    }

	
	
	public Vector<Clause> getClauses() {
		return clauses;
	}


	public void setClauses(Vector<Clause> clauses) {
		this.clauses = clauses;
	}

	public Vector<Litteral> getLitterals() {
		return litterals;
	}


	public void setLitterals(Vector<Litteral> litterals) {
		this.litterals = litterals;
	}

	
	public boolean Sat(){
		
		if(clauses.size() == 0 ) return true;
		
		return false;
	}
	
	public ArrayList<Node> devlopper(){
		//while(optimiser()){}
		ArrayList<Node> fils = new ArrayList<>();
		if(litterals.size()>0){
			Litteral lit = Litteral.random(litterals) , nonLit = Litteral.getContraire(litterals, lit);
			
			Node fg = new Node(this,lit);
			Node fd = new Node(this,nonLit);	
			fils.add(fd); fils.add(fg);
		}

		return fils;
    }
	
	
	@SuppressWarnings("unused")
	private boolean optimiser(){
		return suppClauseContienLittOppose() ||
			   suppClauseVarUnePolarite()	 ||
			   suppClausesUnitaire() ;
	}
	
	
	private boolean suppClauseContienLittOppose(){
		for(int i=0;i<clauses.size();i++)
			for(Litteral litteral:litterals)
				if(clauses.get(i).getLitteraux().contains(litteral) && clauses.get(i).getLitteraux().contains(new Litteral(-litteral.getVar()))){
					clauses.remove(i); i--; return true;
			    }
		
		return false;		
	}
	
	
	private boolean suppClauseVarUnePolarite(){
		ArrayList<Litteral> NonVarPolarite = new ArrayList<>();
		ArrayList<Litteral> varPolarite = new ArrayList<>();
		
		for(Litteral litt:litterals){
			for(Clause clause:clauses){
				if(clause.getLitteraux().contains(new Litteral(-litt.getVar()))) NonVarPolarite.add(litt);
			}
		}
		
		for(Litteral litt:litterals){
			if(!NonVarPolarite.contains(litt))
				varPolarite.add(litt);
		}
		
		for(Litteral litt:varPolarite){
			clauses.removeAll(litt.getClause());
			litterals.remove(new Litteral(-litt.getVar()));
		}
		
		litterals.removeAll(varPolarite);
				
		return varPolarite.size() > 0;		
	}
	
	
	
	private boolean suppClausesUnitaire(){
		
		ArrayList<Litteral> varUnitaire = new ArrayList<>();
		for(Clause clause:clauses)
			if(clause.getLitteraux().size() == 1) varUnitaire.add(clause.getLitteraux().get(0)); // seul element
		
		for(Litteral litteral:varUnitaire)
			if(varUnitaire.contains(new Litteral(-litteral.getVar()))){contradiction=true; return false;}

		// supprimer tt les clause qui contienne ce varUnitaire et 
		// supprimer sa negation de tt les clause qui le contien
 		for(Litteral litteral:varUnitaire){
			
  			clauses.removeAll(litteral.getClause());
 			
			Litteral nonlitt = Litteral.getContraire(litterals, litteral);
			for(Clause clause:nonlitt.getClause()){
				if(clause.getLitteraux().size() == 1 ) {contradiction = true ; return false;}
				clause.getLitteraux().remove(nonlitt);
			}
			
			litterals.remove(nonlitt);
		}
 		
 		if(varUnitaire.size()>0) litterals.removeAll(varUnitaire);
 	
 		
		return varUnitaire.size() > 0;			
	}

	
	@Override
	public int compareTo(Object o) {
		if(o==this) return 0;
		if((o instanceof Node)){
			Node node = (Node) o ;
			if(MetdsRechGraph.strategie.equals(Stratigie.ASTAR)) return heuristique < node.heuristique ? 1 : -1 ;
			else return clauses.size() > node.clauses.size() ? 1 : -1 ;
		}
		return 0;
	}
	
}
