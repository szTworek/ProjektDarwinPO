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

import java.io.BufferedWriter;
import java.io.FileWriter;
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
    private CheckBox csvCheck;

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
        boolean normalGenome=genomeType.getValue().equals("Pełna predestynacja");

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
        Simulation simulation = new Simulation(specifications, presenter, csvCheck.isSelected());
        WorldMap map = simulation.getWorldMap();
        presenter.setWorldMap(map);
        presenter.setThread(simulation.getThread());
        configureStage(stage, viewRoot);

        stage.setOnCloseRequest(event -> {
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
            height.getValueFactory().setValue(7);
            width.getValueFactory().setValue(7);
            startingAmountOfPlants.getValueFactory().setValue(6);
            amountOfEnergyPerPlant.getValueFactory().setValue(2);
            dailyPlantGrowth.getValueFactory().setValue(2);
            startingAmountOfAnimals.getValueFactory().setValue(7);
            startingEnergyForAnimals.getValueFactory().setValue(12);
            healthyLimit.getValueFactory().setValue(4);
            energyUsageForReproduction.getValueFactory().setValue(5);
            minimalAmountOfMutations.getValueFactory().setValue(1);
            maximalAmountOfMutations.getValueFactory().setValue(5);
            genomeLength.getValueFactory().setValue(5);
        }
        else if(source==mediumSimulation){
            height.getValueFactory().setValue(12);
            width.getValueFactory().setValue(12);
            startingAmountOfPlants.getValueFactory().setValue(30);
            amountOfEnergyPerPlant.getValueFactory().setValue(2);
            dailyPlantGrowth.getValueFactory().setValue(4);
            startingAmountOfAnimals.getValueFactory().setValue(30);
            startingEnergyForAnimals.getValueFactory().setValue(15);
            healthyLimit.getValueFactory().setValue(8);
            energyUsageForReproduction.getValueFactory().setValue(5);
            minimalAmountOfMutations.getValueFactory().setValue(1);
            maximalAmountOfMutations.getValueFactory().setValue(5);
            genomeLength.getValueFactory().setValue(5);
        }
        else{
            height.getValueFactory().setValue(17);
            width.getValueFactory().setValue(17);
            startingAmountOfPlants.getValueFactory().setValue(40);
            amountOfEnergyPerPlant.getValueFactory().setValue(4);
            dailyPlantGrowth.getValueFactory().setValue(3);
            startingAmountOfAnimals.getValueFactory().setValue(40);
            startingEnergyForAnimals.getValueFactory().setValue(20);
            healthyLimit.getValueFactory().setValue(10);
            energyUsageForReproduction.getValueFactory().setValue(5);
            minimalAmountOfMutations.getValueFactory().setValue(0);
            maximalAmountOfMutations.getValueFactory().setValue(5);
            genomeLength.getValueFactory().setValue(5);
        }
    }


    @FXML
    public void initialize() {
        SpinnerValueFactory.IntegerSpinnerValueFactory minValueFactory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) minimalAmountOfMutations.getValueFactory();
        SpinnerValueFactory.IntegerSpinnerValueFactory maxValueFactory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) maximalAmountOfMutations.getValueFactory();
        SpinnerValueFactory.IntegerSpinnerValueFactory genomeLengthValidator =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) genomeLength.getValueFactory();


        minimalAmountOfMutations.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue > maximalAmountOfMutations.getValue()) {
                minimalAmountOfMutations.getValueFactory().setValue(oldValue);
            }
        });


        maximalAmountOfMutations.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue < minimalAmountOfMutations.getValue()) {
                maximalAmountOfMutations.getValueFactory().setValue(oldValue);
            }
        });
        maximalAmountOfMutations.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue > genomeLength.getValue()) {
                maximalAmountOfMutations.getValueFactory().setValue(oldValue);
            }
        });
        genomeLength.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue < maximalAmountOfMutations.getValue()) {
                genomeLength.getValueFactory().setValue(oldValue);
            }
        });
    }


}
