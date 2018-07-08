package ACO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import solverSat.CNF;


public class Ant {

	private ACO.Solution solution_actuel;
	private ArrayList<Solution> visitedSolution = new ArrayList<>();
	private List<String> tabuList = new ArrayList<>();	

	public Ant() {
		solution_actuel = new Solution();
	}

	
	public void buildeSolutoin(){
		if(solution_actuel.getNbClSat() < ( 0.98 * CNF.NbClauseTotal  ) )
		solution_actuel.generSol();
		search();
	}
	
	public Solution getSolution_actuel() {
		return solution_actuel;
	}

	
	public void maj_local() {
		double pheromone = solution_actuel.getPheromone();
		pheromone = ((1 - ACS.p) * pheromone) + (ACS.p * ACS.pheromone0);
		solution_actuel.setPheromone(pheromone);
	}

	public ArrayList<Solution> getVisitedSolution() {
		return visitedSolution;
	}

	public void selection_solution() {
		Random rand = new Random();
		float q = rand.nextFloat();
		if (q <= ACS.q0) {
			select_par_Intensification();
		} else {
			select_par_diversification();
		}
	}

	private boolean select_par_Intensification() {
		Solution temp = null;
		double prob = 0;
		for (Solution s : solution_actuel.getVoisins()) {
			double deltPher = solution_actuel.getPheromone() - s.getPheromone();
			double nij = 1 / (solution_actuel.getNbClSat() - s.getNbClSat());
			double probTemp = Math.pow(nij, ACS.beta) * deltPher;
			if (probTemp >= prob && !(s.isExplored())) {
				prob = probTemp;
				temp = s;
			}
		}
		if (temp == null) {
			solution_actuel = solution_actuel.getVoisin();

		} else {
			this.solution_actuel = temp;
		}

		ACS.solution_explore.add(this.solution_actuel);
		return true;
	}

	
	private void select_par_diversification(){
		double pherVisib = 0;
		for (Solution s : visitedSolution) {
			double deltPher = solution_actuel.getPheromone() - s.getPheromone();
			double nij = 1 / (solution_actuel.getNbClSat() - s.getNbClSat());
			pherVisib += Math.pow(nij, ACS.beta) * Math.pow(deltPher, ACS.alpha);
		}
		Solution temp = null;
		double prob = 0;
		for (Solution s : ACS.solution_explore) {
			double deltPher = solution_actuel.getPheromone() - s.getPheromone();
			double nij = 1 / (solution_actuel.getNbClSat() - s.getNbClSat());
			double probTemp = (Math.pow(nij, ACS.beta) * Math.pow(deltPher, ACS.alpha)) / pherVisib;
			if (probTemp >= prob && !(s.isExplored())) {
				temp = s;
				prob = probTemp;
			}
		}

		if (temp != null) {
			this.solution_actuel = temp;
			ACS.solution_explore.add(this.solution_actuel);
		}

	}

	
    public void search(){
    	
    	//if(assignedSolution.getFitness() > (CNF.NbClauseTotal * 0.98)-1 ){  serarchLocalPresise(); return; }
    		
        tabuList.add(solution_actuel.toString());
        
        Solution neighborsSol;
        int cpt = 1 ;
        while(cpt<ACS.nbIterRLocla){       		
   
        	neighborsSol = new Solution(solution_actuel,ThreadLocalRandom.current().nextInt(0,ACS.sat.getNbLitts()));
        	if(tabuList.contains(neighborsSol.toString())) continue;
        	
        	if(neighborsSol.getNbClSat()>solution_actuel.getNbClSat()){
        		solution_actuel = neighborsSol;
        		solution_actuel.maj_global();
        	}
        	
        	if(solution_actuel.sat()) { ACS.bestSolution = solution_actuel; break;}
        	cpt++;
        }
        
        if(solution_actuel.getNbClSat() > ACS.bestSolution.getNbClSat()){
        	ACS.bestSolution = solution_actuel;
        	ACS.bestSolution.maj_global();
        }
        
    }


}
