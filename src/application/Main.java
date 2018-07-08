package application;

import ACO.ACS;
import BSO.BSO;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import solverSat.CNF;
import solverSat.MetdsRechGraph;
import solverSat.Stratigie;

public class Main extends Application {

  static boolean end = false;
  public static final int tempsMax = 30 ;
  public static double temps_debut = 0;
  private static int timecurrent=0;
  
  
  
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        Parent root = FXMLLoader.load(getClass().getResource("/application/Demo.fxml"));
        		
        primaryStage.setTitle("Metha Project");
        primaryStage.setScene(new Scene(root));
        primaryStage.setFullScreen(true);
        primaryStage.show();

    }


    public static void main(String[] args) { launch(args);}
    
    
	@SuppressWarnings("rawtypes")
	public static void solve(String nomFichier,Stratigie stratigie) throws Exception {
		Service serv = new Service() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						temps_debut = System.currentTimeMillis() / 1000;
						CNF sat  = new CNF("BCs/"+nomFichier);
						if(stratigie.equals(Stratigie.BSO))
							BSO.AlgorithmeBSO(sat);
						else if(stratigie.equals(Stratigie.ACS))
							ACS.ACS_Algorithme(sat);
						else
							MetdsRechGraph.RechGraph(sat);
						
						System.out.println("-- Fin --");
						return null;
					}
				};
			}
		};
		serv.start();
		
		Controller.updateChart(0,0); end=false; timecurrent=0;
		while(!end){
			Thread.sleep(1000);
			Controller.updateChart(++timecurrent,tauxDeSat(stratigie));
			if(timecurrent>tempsMax || tauxDeSat(stratigie)==100) { end = true ; Controller.systemBusy = false; }
		}
			
	}
	
	public static int tauxDeSat(Stratigie strategie){
		if(strategie.equals(Stratigie.BSO))
			return (int) ((float) (BSO.maxSat * ( 1.0 / CNF.NbClauseTotal )) *100) ;
		else if(strategie.equals(Stratigie.ACS))
			return (int) ((float) (ACS.bestSolution.getNbClSat() * ( 1.0 / CNF.NbClauseTotal )) *100) ;
		else return (int) MetdsRechGraph.tauxDeSat;
	}

    
}
