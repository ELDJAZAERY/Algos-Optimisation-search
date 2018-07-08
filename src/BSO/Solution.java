package BSO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import solverSat.CNF;
import solverSat.Clause;
import solverSat.Litteral;



public class Solution implements Comparable<Object> {
	
	private Byte[] sol = new Byte[BSO.sat.getNbLitts()];
	private int fitness;
	private int diversity;
	
	private Vector<Clause> clausesNonSat = new Vector<>();
	
	Solution() {
		Random rnd = new Random();
		for(int i=0;i<BSO.sat.getNbLitts();i++){
			boolean rand = rnd.nextBoolean();
			if(rand){sol[i] = 1;}else{sol[i] = 0;}
		}

		fitness();
	}
	
	
	Solution(Solution s,int flip,int h) {
		this.sol = s.sol.clone();

		int i = 0;
		do{
			sol[(flip * i + h)%(BSO.sat.getNbLitts()-1)] = (byte)((s.sol[(flip * i + h)%(BSO.sat.getNbLitts()-1)] + 1) % 2);
			i++;
		}while (flip * i + h < sol.length);

		fitness();
	}

	public Solution(Solution s,int flip) {
		this.sol = s.sol.clone();
		sol[flip%(BSO.sat.getNbLitts()-1)] = (byte) ( (s.sol[flip%(BSO.sat.getNbLitts()-1)]+1) % 2 );
		fitness();
	}
	
	Solution(Solution s) {
		this.sol = s.sol.clone();
	}

	
	public int getDeversity() {
		return diversity;
	}

	public Byte[] getSol() {
		return sol;
	}


	public int getFitness() {
		return fitness;
	}

	
	public boolean sat(){return fitness==CNF.NbClauseTotal;}
	
	
    private void fitness(){        
        fitness=0;
        int lit;
        ArrayList<Integer> clausevisted = new ArrayList<>();
        for (int i = 1; i <= sol.length ; i++) {     
        	lit= i; if(sol[i-1]==0) lit = -i ;
            for (int j = 0; j < CNF.NbClauseTotal ; j++) {
                if ( !clausevisted.contains(j) && BSO.sat.getClauses().get(j).getLitteraux().contains(new Litteral(lit))){
                    fitness++;
                    clausevisted.add(j);
                }
            }
        }
    }

	 
	public void diversity(String[] solutions) {
		String first = this.toString();
		diversity = distance(first, solutions[0]);
		for(String second:solutions){
			diversity = Math.min(diversity,distance(first, second));
		}
	}

	private int distance(String first,String second){
		int dist=0;
		
		char[] fir = first.toCharArray();
		char[] sec = second.toCharArray();
		
		for(int i=0;i<first.length();i++)
			if(fir[i] != sec[i]) dist++;
		
		return dist;
	}
	
	
	
	public Vector<Clause> getClauseNonSat(){
		
		if(clausesNonSat.size()>0) return clausesNonSat;
		
        int lit;
        ArrayList<Integer> clausesat = new ArrayList<>();
        
        for(int i = 1; i <= sol.length ; i++){
        	lit= i; if(sol[i-1]==0) lit = -i ;
            for(int j = 0; j < CNF.NbClauseTotal ; j++){
                if ( !clausesat.contains(j) && BSO.sat.getClauses().get(j).getLitteraux().contains(new Litteral(lit))){
                	clausesat.add(j);
                }
            }
        }
        
        for(int j=0; j < CNF.NbClauseTotal ; j++)
        	if(!clausesat.contains(j)) clausesNonSat.add(BSO.sat.getClauses().get(j));
        
        return clausesNonSat;
	}
	
	
	@Override
	public String toString() {
		return Arrays.toString(sol);
	}

	@Override
	public int compareTo(Object o) {
		return ((Integer) this.fitness).compareTo((Integer) (((Solution) o).fitness));
	}

}
