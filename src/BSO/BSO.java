package BSO;

import solverSat.CNF;
import java.util.*;

import application.Main;


public class BSO {

    private static Solution sRef;
    public static ArrayList<Bee> bees = new ArrayList<>();
    public static ArrayList<Solution> Dances = new ArrayList<>();
    public static LinkedList<String> tabou = new LinkedList<>();
    public static Bee beeInit;

    public static int maxChances=5;
    public static final int maxIter=1000;
    public static  int nbBees=10;
    public static final int flip=5;
    public static  int nbMaxiterationLocal=15;

    static CNF sat;
    public static int maxSat = 0;
    static int NbChances;
    static boolean diversity = true;
    static boolean sRefnear = false; 
    
    public static void AlgorithmeBSO(CNF sat) {

    	BSO.sat = sat;
    	
    	NbChances=maxChances;
    	
        beeInit = new Bee();
        sRef = beeInit.getAssignedSolution();
        maxSat = sRef.getFitness();
        		
        int it = 0;
        while (it < maxIter) {
        	
            tabou.add(sRef.toString());
            
            if(sRef.sat()){ System.out.println(" ----- Sat ------  \n"+sRef.toString()); return;}
/*           
            if(sRef.getFitness()>319 && nbMaxiterationLocal<25 ){ nbMaxiterationLocal+=10; }
            if(sRef.getFitness()>318) { nbMaxiterationLocal+=10; sRefnear = true; }
            if(maxSat>321) diversity = false;
*/           
            
            searchAreaDetermination();

            if(maxSat>318 && nbMaxiterationLocal<200 ) nbMaxiterationLocal+=20;
            if(sRef.getFitness()>322 && nbMaxiterationLocal<4000 ) { nbMaxiterationLocal+=50; sRefnear = true; }
            
            for(Bee bee:bees) bee.search();
            
            selectionOfSref();

            it++;
            maxSat = Math.max(maxSat,sRef.getFitness());
            System.out.println(sRef.getFitness()+" -->  Max-SAt " + maxSat);     

			if( (System.currentTimeMillis() / 1000) - Main.temps_debut  > Main.tempsMax ){return;}
        
        }
        
    }

    
    public static void selectionOfSref() {
        
    	Solution sBest=null;
    	Collections.sort(Dances);
    	do{
            sBest = Dances.remove(Dances.size()-1);  		
    	}while(tabou.contains(sBest.toString()) && !Dances.isEmpty());
    	
    	if(Dances.isEmpty()) return;    	
    	
    	int DeltaFit = sBest.getFitness() - sRef.getFitness();
    	
    	if(DeltaFit<=0 && sRefnear) return;
    	if(DeltaFit > 0){
            sRef = sBest;
            NbChances = maxChances;
        }else if(NbChances>0 || !diversity){
            NbChances--;
        	sRef = sBest;       
        }else{
        	System.out.println("--- Diversity ---");
        	// Diversity Recherche
        	
            String[] tabous = tabou.toArray(new String[tabou.size()]);

            for(Solution sol:Dances) sol.diversity(tabous); 
            
            Collections.sort(Dances, new Comparator<Solution>() {
                @Override
                public int compare(Solution solution, Solution t1) {
                    return ((Integer) solution.getDeversity()).compareTo((Integer) t1.getDeversity());
                }
            });
            
            
            sRef = Dances.remove(Dances.size() - 1);
            List<Solution> bestDiversitySol = new ArrayList<>(); bestDiversitySol.add(sRef);
            for(Solution sol:Dances) if(sol.getDeversity() ==sRef.getDeversity()) bestDiversitySol.add(sol);
            Collections.sort(bestDiversitySol);
            sRef = bestDiversitySol.remove(bestDiversitySol.size()-1);
            
            
            NbChances = maxChances;        
        }
    	
    }

    
    public static boolean wasVisited() {
        return tabou.contains(sRef.toString());
    }

    private static void searchAreaDetermination() {  
      //  if(sRefnear) { searchAreaLoacl(); return; }
      
        bees.clear();
        //Dances.clear();	
        for (int h = 0; h < nbBees; h++) {
            bees.add(new Bee(sRef,h));
        }
    }
    
    
}
