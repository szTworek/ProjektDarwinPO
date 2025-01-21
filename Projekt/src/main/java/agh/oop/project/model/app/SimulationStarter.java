package agh.oop.project.model.app;

import agh.oop.project.Simulation;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.worlds.WorldMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationStarter {
    @FXML
    private Spinner<Integer> height;
    @FXML
    private Spinner<Integer> width;
    @FXML
    private Spinner<Integer> startingAmountOfPlants;
    @FXML
    private Spinner<Integer> amountOfEnergyPerPlant;
    @FXML
    private Spinner<Integer> dailyPlantGrowth;
    @FXML
    private ComboBox<String> growthType;
    @FXML
    private Spinner<Integer> startingAmountOfAnimals;
    @FXML
    private Spinner<Integer> startingEnergyForAnimals;
    @FXML
    private Spinner<Integer> healthyLimit;
    @FXML
    private Spinner<Integer> energyUsageForReproduction;
    @FXML
    private Spinner<Integer> minimalAmountOfMutations;
    @FXML
    private Spinner<Integer> maximalAmountOfMutations;
    @FXML
    private ComboBox<String> genomeType;
    @FXML
    private Spinner<Integer> genomeLength;

    @FXML
    private Button startButton;

    @FXML
    private Button smallSimulation;
    @FXML
    private Button mediumSimulation;
    @FXML
    private Button largeSimulation;

    public void onSimulationStartClicked(ActionEvent e) throws IOException {
        boolean normalGrowth=growthType.getValue().equals("Zalesione równiki");
        boolean normalGenome=genomeType.getValue().equals("Pełna losowość");

        Specifications specifications=new Specifications(
                height.getValue(),
                width.getValue(),
                startingAmountOfPlants.getValue(),
                amountOfEnergyPerPlant.getValue(),
                dailyPlantGrowth.getValue(),
                normalGrowth,
                startingAmountOfAnimals.getValue(),
                startingEnergyForAnimals.getValue(),
                healthyLimit.getValue(),
                energyUsageForReproduction.getValue(),
                minimalAmountOfMutations.getValue(),
                maximalAmountOfMutations.getValue(),
                genomeLength.getValue(),
                normalGenome);

        newWindow(specifications);
    }

    public void newWindow(Specifications specifications) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        GridPane viewRoot = loader.load();

        SimulationPresenter presenter = loader.<SimulationPresenter>getController();
        Simulation simulation = new Simulation(specifications, presenter);
        WorldMap map = simulation.getWorldMap();
        presenter.setWorldMap(map);
        presenter.setThread(simulation.getThread());
        configureStage(stage, viewRoot);

        stage.setOnCloseRequest(event -> {
            System.out.println("Okno zostało zamknięte!");
            simulation.endSimulation();
        });

        stage.show();
    }
    private void configureStage(Stage stage, GridPane viewRoot) {
        var scene = new Scene(viewRoot,0.7*1792,0.7*1024 );
        scene.getStylesheets().add("simulationPageStyle.css");
        stage.setScene(scene);
        stage.setTitle("Simulation");
        stage.setResizable(false);

    }

    public void setDefaultSpecifications(ActionEvent e){
        Object source=e.getSource();
        if(source==smallSimulation){
            height.getValueFactory().setValue(5);
            width.getValueFactory().setValue(5);
            startingAmountOfPlants.getValueFactory().setValue(3);
            amountOfEnergyPerPlant.getValueFactory().setValue(5);
            dailyPlantGrowth.getValueFactory().setValue(3);
            startingAmountOfAnimals.getValueFactory().setValue(3);
            startingEnergyForAnimals.getValueFactory().setValue(12);
            healthyLimit.getValueFactory().setValue(7);
            energyUsageForReproduction.getValueFactory().setValue(5);
            minimalAmountOfMutations.getValueFactory().setValue(5);
            maximalAmountOfMutations.getValueFactory().setValue(5);
            genomeLength.getValueFactory().setValue(5);
        }
        else if(source==mediumSimulation){
            height.getValueFactory().setValue(10);
            width.getValueFactory().setValue(10);
            startingAmountOfPlants.getValueFactory().setValue(5);
            amountOfEnergyPerPlant.getValueFactory().setValue(5);
            dailyPlantGrowth.getValueFactory().setValue(5);
            startingAmountOfAnimals.getValueFactory().setValue(5);
            startingEnergyForAnimals.getValueFactory().setValue(5);
            healthyLimit.getValueFactory().setValue(5);
            energyUsageForReproduction.getValueFactory().setValue(5);
            minimalAmountOfMutations.getValueFactory().setValue(5);
            maximalAmountOfMutations.getValueFactory().setValue(5);
            genomeLength.getValueFactory().setValue(5);
        }
        else{
            height.getValueFactory().setValue(10);
            width.getValueFactory().setValue(10);
            startingAmountOfPlants.getValueFactory().setValue(5);
            amountOfEnergyPerPlant.getValueFactory().setValue(5);
            dailyPlantGrowth.getValueFactory().setValue(5);
            startingAmountOfAnimals.getValueFactory().setValue(5);
            startingEnergyForAnimals.getValueFactory().setValue(5);
            healthyLimit.getValueFactory().setValue(5);
            energyUsageForReproduction.getValueFactory().setValue(5);
            minimalAmountOfMutations.getValueFactory().setValue(5);
            maximalAmountOfMutations.getValueFactory().setValue(5);
            genomeLength.getValueFactory().setValue(5);
        }
    }

}
