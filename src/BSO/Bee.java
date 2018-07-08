package BSO;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import solverSat.Clause;
import solverSat.Litteral;


public class Bee {

	private Solution assignedSolution;
	private List<String> tabuList = new ArrayList<>();	
	
	public Bee() {
		assignedSolution = new Solution();
	}

	Bee(Solution sol ,int h) {
		assignedSolution = new Solution(sol, BSO.flip, h);
	}

	Bee(Solution sol) {
		assignedSolution = new Solution(sol);
	}

	public Solution getAssignedSolution() {
		return assignedSolution;
	}

	public void setAssignedSolution(Solution assignedSolution) {
		this.assignedSolution = assignedSolution;
	}

    public void search(){
    	
    	//if(assignedSolution.getFitness() > (CNF.NbClauseTotal * 0.98)-1 ){  serarchLocalPresise(); return; }
    		
        tabuList.add(assignedSolution.toString());
        
        Solution neighborsSol;
        int cpt = 1 ;
        while(cpt<BSO.nbMaxiterationLocal){       		
   
        	neighborsSol = new Solution(assignedSolution,ThreadLocalRandom.current().nextInt(0,BSO.sat.getNbLitts()));
        	if(tabuList.contains(neighborsSol.toString())) continue;
        	
        	if(neighborsSol.getFitness()>assignedSolution.getFitness())
        		assignedSolution = neighborsSol;
        	
        	if(assignedSolution.sat()) break;
        	cpt++;
        }
        
        BSO.Dances.add(assignedSolution);
    }

    
    private void serarchLocalPresise(){
    	int cpt = 0;
    	while(cpt++<BSO.nbMaxiterationLocal){    		
        	if(assignedSolution.sat()) return ;
        	Vector<Clause> clausesNonSat = assignedSolution.getClauseNonSat();
        	Vector<Integer> indexOfLitteralsDeNonSat = new Vector<>();
        	
        	for(Clause clause:clausesNonSat)
        		for(Litteral litt:clause.getLitteraux())
        			  indexOfLitteralsDeNonSat.add(Math.abs(litt.getVar()));
        	
        	if(indexOfLitteralsDeNonSat.size()==0) continue;
        	
        	Solution sol;
        	for(Integer index:indexOfLitteralsDeNonSat){
        		sol = new Solution(assignedSolution,index);
        		if(sol.getFitness()>assignedSolution.getFitness())
        			assignedSolution = sol;        		
        	}
        		
       	       	
    	}
    	BSO.Dances.add(assignedSolution);
    }
    
    
	@Override
	public String toString() {
		return assignedSolution + "\n";
	}
}
