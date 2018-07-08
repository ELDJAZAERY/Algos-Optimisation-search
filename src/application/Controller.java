package application;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import solverSat.Stratigie;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Controller {

	private String nomFichier = "uf75-01.cnf";
	public static Stratigie stratigie = Stratigie.LargeurDabor;

	
	public static boolean systemBusy = false ;
	
	
	@FXML
	public LineChart<Number, Number> chart;

	static XYChart.Series<Number,Number> series ;

		
	@FXML
	private ChoiceBox<String> fileChoisie , methodeChoisie ;
	
	ObservableList<String> filesDispo = FXCollections.observableArrayList(
			"uf75-01.cnf","uf75-02.cnf","uf75-03.cnf","uf75-04.cnf","uf75-05.cnf","uf75-06.cnf","uf75-07.cnf","uf75-08.cnf"
			,"uf75-09.cnf","uf75-10.cnf"
			,"uuf75-01.cnf","uuf75-02.cnf","uuf75-03.cnf","uuf75-04.cnf","uuf75-05.cnf","uuf75-06.cnf","uuf75-07.cnf","uuf75-08.cnf"
			,"uuf75-09.cnf","uuf75-10.cnf");

	ObservableList<String> Methodes = FXCollections.observableArrayList(
			"DFS","BFS","Cout Uniforme","A*","BSO","ACS");
	
	
	@FXML
	private TextArea fileContenu ;
	
	
	@FXML
	private void initialize() throws Exception{
		series = new XYChart.Series<Number,Number>();
		series.setName("taux de satisfiabilité");
		
		chart.getData().add(series);
		
		fileChoisie.setValue("uf75-01.cnf");
		fileChoisie.setItems(filesDispo);

		methodeChoisie.setValue("DFS");
		methodeChoisie.setItems(Methodes);
		
		fileContenu.setText(readFileContenu("uf75-01.cnf"));
		
		fileChoisie.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){
				try{updateChampText(newValue);}catch(Exception e){System.out.println(e);}
			}
		});
		
		
		methodeChoisie.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){
				upDateStratigie(newValue);
			}
		});
	
	}
	
	private void upDateStratigie(Number newValue) {
		switch ((int)newValue) {
			case 0: stratigie = Stratigie.LargeurDabor; break;
			case 1: stratigie = Stratigie.ProfondeurDabord; break;
			case 2: stratigie = Stratigie.CoutUniforme; break;
			case 3: stratigie = Stratigie.ASTAR; break;
			case 4: stratigie = Stratigie.BSO; break;
			case 5: stratigie = Stratigie.ACS; break;
		}
	}

	@SuppressWarnings("rawtypes")
	public void solve(ActionEvent actionEvent) {
		if(systemBusy) return;
		systemBusy=true;

		series = new XYChart.Series<Number,Number>();
		series.setName("taux de satisfiabilité");
		
		chart.getData().clear();		
		chart.getData().add(series);
						
		Service serv = new Service() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						Main.solve(nomFichier,stratigie);
						
						return null;
					}
				};
			}
		};
		
		serv.start();
	}

		
	public static void updateChart(int x,int y) {
		Platform.runLater(() -> {
				series.getData().add(new XYChart.Data<>(x,y));
		});
	}
	
	
	public void updateChampText (Number newValue) throws Exception{
		String nomFile = "";
		if(newValue.intValue() < 10 ) {
			nomFile="uf75-";
			if(newValue.intValue() == 9 ) nomFile+="010.cnf"; else nomFile+="0"+(newValue.intValue()+1)+".cnf";
		}else{
			nomFile="uuf75-";
			if(newValue.intValue() == 19) nomFile+="010.cnf"; else nomFile+="0"+(newValue.intValue()-9)+".cnf";
		}
		
		nomFichier = nomFile ; 
		fileContenu.setText(readFileContenu(nomFile));
	}
	
	
	private String readFileContenu(String fileNom) throws Exception{
		String fileContenu = "";
		try{
			InputStream flux=new FileInputStream("BCs/"+fileNom); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			while ((ligne=buff.readLine())!=null){
				fileContenu+=ligne+"\n";
			}
			buff.close(); 
			}		
			catch (Exception e){
			  System.out.println(e.toString());
			}
		
		return fileContenu;
	}
	
	
}
