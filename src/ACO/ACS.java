package ACO;


import java.util.ArrayList;
import java.util.LinkedList;

import application.Main;
import solverSat.CNF;

public class ACS {
	
	public static int nbIter=100;
	public static int nbIterRLocla=100;
	public static int nbAnts=10;
	public static double pheromone0 = 0.1;
	public static float alpha=(float) 0.1;
    public static float beta=(float) 2;
    public static float q0=(float) 0.9;
	public static float p=(float) 0.1;

	public static Solution bestSolution;
	
	protected static LinkedList<Solution> solution_explore = new LinkedList<>();
	private static ArrayList<Ant> Ants = new ArrayList<>();
	
	public static CNF sat;
	
	
	public static void ACS_Algorithme(CNF sat) {

		 // Initialisation
	     ACS.sat = sat;
	     bestSolution = new Solution();
	     init_Ants();
	     Solution.initialPheromonLits();
	    //--------
	     
	    for(int i = 0; i < nbIter; i++) {
			for (Ant a : Ants) {
				// builde une solution et Maj Local
				a.buildeSolutoin();
				
				System.out.println("bestSolution ---> "+bestSolution.getNbClSat());
				if( (System.currentTimeMillis() / 1000) - Main.temps_debut  > Main.tempsMax ){return;}
				
			}
			maj_global();

	    }
		
	}
	

	private static void init_Ants(){
		Ant ant;
		for(int i=0;i<nbAnts;i++){
			ant = new Ant();
			Ants.add(ant);
		}
	}
	
	private static void maj_global() {
		bestSolution.maj_global();
	}
	
}
