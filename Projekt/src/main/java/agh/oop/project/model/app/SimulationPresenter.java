package agh.oop.project.model.app;

import agh.oop.project.model.Vector2d;
import agh.oop.project.model.worlds.WorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;
import java.util.Map;

public class SimulationPresenter implements MapChangeListener{
    //stats:
//    @FXML
//    private Label numberOfAnimalsLabel;
//    private Label numberOfPlantsLabel;
//    private Label freeAreasLabel;
//    private Label genotypesLabel;
//    private Label averageEnergyLabel;
//    private Label averageLifespanLabel;
//    private Label averageNumberOfChildrenLabel;
    @FXML
    private GridPane mapGrid;
    private WorldMap map;
    private int height;
    private int width;
    public void setWorldMap(WorldMap map) {
        this.map = map;
        height = map.getHeight();
        width = map.getWidth();
    }
    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
    public void drawMap(){
        clearGrid();


        int CELL_SIZE = 60;

        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();

        for (int i = 0; i < width; i++) {
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));

        }
        for (int i = 0; i < height; i++) {
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        }
        for (int y = 0; y <height; y++) {
            for (int x = 0; x < width; x++) {
                Label label = new Label();
                Object object = map.objectAt(new Vector2d(x, y));
                if (object != null) {
                    label.setText(object.toString());
                }
                mapGrid.add(label, x, height - y-1);
                GridPane.setHalignment(label, HPos.CENTER);

            }
        }}

    @Override
    public void mapChanges(WorldMap worldMap, int numberOfAnimals, int numberOfPlants, int freeAreas, Map<List<Integer>, Integer> genotypes, float averageEnergy, float averageLifespan, float averageNumberOfChildren) {
        Platform.runLater(() -> {
            drawMap();
//            numberOfAnimalsLabel.setText(String.valueOf(numberOfAnimals));
//            numberOfPlantsLabel.setText(String.valueOf(numberOfPlants));
//            freeAreasLabel.setText(String.valueOf(freeAreas));
//            genotypesLabel.setText(genotypes);
//            averageEnergyLabel.setText(String.valueOf(averageEnergy));
//            averageLifespanLabel.setText(String.valueOf(averageLifespan));
//            averageNumberOfChildrenLabel.setText(String.valueOf(averageNumberOfChildren));
        });
    }
}
