package solverSat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import application.Controller;
import application.Main;


public class MetdsRechGraph {

	public static float tauxDeSat = 0 ;
	
	private static LinkedList<Node> open= new LinkedList<>();
	
	
	static Stratigie strategie;
	
	public static void RechGraph(CNF sat){
		
		strategie = Controller.stratigie;
		
		Node node = new Node(sat.getClauses(),sat.getLitterals());
		ArrayList<Node> fils = new ArrayList<>();
		Node fg , fd ;
		
		
		open = new LinkedList<>(); tauxDeSat = 0 ; float tauxsatlocal = 0;
		open.add(node);

		while(!open.isEmpty()){

			node=choisiNode(strategie);
			
			if(node.Sat()){System.out.println("SAT"); System.out.println((System.currentTimeMillis() / 1000) - Main.temps_debut); return;}
			else{
				
				if(node.contradiction()) break;
				
				fils = node.devlopper();
				
				if( fils.size()>0 && (fg = fils.get(0)) != null && (fd = fils.get(1)) !=null ) {
					addNode(fg);
					addNode(fd);
				}
			}
			
			fils.clear();
			
			if( (System.currentTimeMillis() / 1000) - Main.temps_debut  > Main.tempsMax ){
				return;
			}
			
			if(tauxsatlocal < tauxDeSat ){
				tauxsatlocal = tauxDeSat;
			}
			
		}
		
		System.out.println((System.currentTimeMillis() / 1000) - Main.temps_debut);
		System.out.println("not SAT");
		return;
	}
	
	
	private static Node choisiNode(Stratigie stratigie){
		Node node = null;
		
		 // FIFO retire premier element
		if(Stratigie.LargeurDabor.equals(stratigie) || Stratigie.BFS.equals(stratigie)){
			node = open.getFirst();
			open.removeFirst();
		}
		
		// LIFO retire le dernier element
		if(Stratigie.ProfondeurDabord.equals(stratigie) || Stratigie.DFS.equals(stratigie)){
			node = open.getLast();
			open.removeLast();
		}

		
		// ASTAR or Coutuniform basé sur le trie de la liste Open
		if(Stratigie.ASTAR.equals(stratigie) || Stratigie.CoutUniforme.equals(stratigie)){
			Collections.sort(open, Node::compareTo);
			node = open.getFirst();
			open.removeFirst();
		}
		
		return node;	
	}
	
	
	private static void addNode(Node node){		
		// LIFO && FIFO ajout a la fin
			open.add(node);
	}

}
