package BSO;

import solverSat.CNF;

public class BSO_Demo {

	public static void main(String[] args)throws Exception {
		CNF sat  = new CNF("BCs/uf75-01.cnf");
		BSO.AlgorithmeBSO(sat);
		
		/*for(int flip = 3;flip <= 15;flip+=2)
		            for (int maxIterations = 100; maxIterations <= 1000; maxIterations += 100)
		                for (int maxChances = 2; maxChances <= 14; maxChances += 2)
		                    for (int maxLocalSearch = 10; maxLocalSearch <= 100; maxLocalSearch += 5) {
		                    	
		  }
		 */
	}

}
