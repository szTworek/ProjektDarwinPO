package agh.oop.project.model.app;

import agh.oop.project.model.worlds.Plant;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.animals.Animal;
import agh.oop.project.model.worlds.WorldMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.round;

public class SimulationPresenter implements MapChangeListener{
    //stats:
    @FXML
    private Label dayLabel;
    @FXML
    private Label numberOfAnimalsLabel;
    @FXML
    private Label numberOfPlantsLabel;
    @FXML
    private Label freeAreasLabel;
    @FXML
    private Label genotypesLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label averageLifespanLabel;
    @FXML
    private Label averageNumberOfChildrenLabel;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Button startStop;

    @FXML
    private Label followedAnimalStats;
    @FXML
    private Label animalStatsHeader;
    @FXML
    private Label animalGenome;
    @FXML
    private Label activatedPartOfGenome;
    @FXML
    private Label animalEnergy;
    @FXML
    private Label eatenPlants;
    @FXML
    private Label howManyChildren;
    @FXML
    private Label howManyDescendant;
    @FXML
    private Label animalLifespan;
    @FXML
    private Label animalDeathDay;
    @FXML
    private Label followedLabel;

    private final Image monkeyGreen = new Image(Objects.requireNonNull(getClass().getResource("/images/monkey_green.png")).toExternalForm());
    private final Image monkeyYellow = new Image(Objects.requireNonNull(getClass().getResource("/images/monkey_yellow.png")).toExternalForm(),    30, // Szerokość docelowa
            30, // Wysokość docelowa
            false, // Zachowaj proporcje
            true);
    private final Image monkeyOrange = new Image(Objects.requireNonNull(getClass().getResource("/images/monkey_orange.png")).toExternalForm(),     30, // Szerokość docelowa
            30, // Wysokość docelowa
            false, // Zachowaj proporcje
            true);
    private final Image monkeyRed = new Image(Objects.requireNonNull(getClass().getResource("/images/monkey_red.png")).toExternalForm(),    30, // Szerokość docelowa
            30, // Wysokość docelowa
            false, // Zachowaj proporcje
            true);
    private final Image leaves = new Image(Objects.requireNonNull(getClass().getResource("/images/leaves.png")).toExternalForm(),    30, // Szerokość docelowa
            30, // Wysokość docelowa
            false, // Zachowaj proporcje
            true);
    private final Image cross= new Image(Objects.requireNonNull(getClass().getResource("/images/cross.png")).toExternalForm(),    30, // Szerokość docelowa
            30, // Wysokość docelowa
            false, // Zachowaj proporcje
            true);


    private Animal followedAnimal;
    private ExtendedThread thread;
    private WorldMap map;
    private int height;
    private int width;
    public void setWorldMap(WorldMap map) {
        this.map = map;
        height = map.getHeight();
        width = map.getWidth();
    }

    public void setThread(ExtendedThread thread) {
        this.thread = thread;
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void presentAnimal(Label label, Animal animal) {
        if (animal.getEnergy() >= 15) {
            label.setBackground(new Background(
                    new BackgroundImage(
                            monkeyGreen,
                            BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                            BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                            BackgroundPosition.CENTER,          // Wycentruj obraz
                            new BackgroundSize(100, 100, true, true, true, false)  // Dopasowanie
                    )
            ));
        } else if (animal.getEnergy() >= 10) {
            label.setBackground(new Background(
                    new BackgroundImage(
                            monkeyYellow,
                            BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                            BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                            BackgroundPosition.CENTER,          // Wycentruj obraz
                            new BackgroundSize(100, 100, true, true, true, false)   // Dopasowanie
                    )
            ));

        } else if (animal.getEnergy() >= 5) {
            label.setBackground(new Background(
                    new BackgroundImage(
                            monkeyOrange,
                            BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                            BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                            BackgroundPosition.CENTER,          // Wycentruj obraz
                            new BackgroundSize(100, 100, true, true, true, false)  // Dopasowanie
                    )
            ));
        } else {
            label.setBackground(new Background(
                    new BackgroundImage(
                            monkeyRed,
                            BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                            BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                            BackgroundPosition.CENTER,          // Wycentruj obraz
                            new BackgroundSize(100, 100, true, true, true, false)  // Dopasowanie
                    )
            ));

        }
    }
    public void drawMap(){
        clearGrid();


        int CELL_SIZE = 30;

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
                Object object;
                Vector2d field=new Vector2d(x, y);
                label.setPrefSize(CELL_SIZE, CELL_SIZE);
                label.setText(" ");
                if (followedAnimal!=null && field.equals(followedAnimal.getPosition())) {
                    followedLabel=label;
                    object=followedAnimal;
                    if (followedAnimal.isDead()){
                        label.setBackground(new Background(
                                new BackgroundImage(
                                        cross,
                                        BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                                        BackgroundRepeat.NO_REPEAT,         // Bez powtarzania
                                        BackgroundPosition.CENTER,          // Wycentruj obraz
                                        new BackgroundSize(100, 100, true, true, true, false)  // Dopasowanie
                                )
                        ));
                    }
                    else {
                        presentAnimal(label, followedAnimal);
                    }
                    label.setBorder(new Border(new BorderStroke(
                            Color.RED,
                            BorderStrokeStyle.SOLID,
                            new CornerRadii(0),
                            new BorderWidths(2))));
                }else{
                    object = map.objectAt(new Vector2d(x, y));
                    if (object instanceof Animal) {
                        presentAnimal(label,(Animal)object);
                    }else if(object instanceof Plant) {
                        label.setBackground(new Background(
                                new BackgroundImage(
                                        leaves,
                                        BackgroundRepeat.NO_REPEAT,
                                        BackgroundRepeat.NO_REPEAT,
                                        BackgroundPosition.CENTER,
                                        new BackgroundSize(100, 100, true, true, true, false)
                                )
                        ));
                    }
                }
                if (object instanceof Animal) {
                    label.setOnMouseClicked(event -> onAnimalClicked(event, (Animal) object));
                }
                mapGrid.add(label, x, height - y-1);
                GridPane.setHalignment(label, HPos.CENTER);
            }
        }}

    private void onAnimalClicked(MouseEvent event, Animal animal) {
        if (!thread.isRunning()){
            if (followedLabel != null) {
                followedLabel.setBorder(null);
            }

            if (event.getSource() instanceof Label label) {
                followedLabel = label;
                followedLabel.setBorder(new Border(new BorderStroke(
                        Color.RED,
                        BorderStrokeStyle.SOLID,
                        new CornerRadii(0),
                        new BorderWidths(2))));
        }}


        followedAnimal = animal;
        animalStats();
    }

    public void animalStats(){

        if(followedAnimal!=null){
            animalStatsHeader.setText("Statystyki zwierzęcia:");
            animalGenome.setVisible(true);
            activatedPartOfGenome.setVisible(true);
            animalEnergy.setVisible(true);
            eatenPlants.setVisible(true);
            howManyChildren.setVisible(true);
            howManyDescendant.setVisible(true);
            animalLifespan.setVisible(true);
            animalDeathDay.setVisible(true);
            animalGenome.setText("Genom zwierzęcia: " + followedAnimal.getGenome().toString());
            activatedPartOfGenome.setText("Aktywowana część genomu: " + String.valueOf(followedAnimal.getNextGenome()));
            animalEnergy.setText(String.valueOf("Energia zwierzęcia: " + followedAnimal.getEnergy()));
            eatenPlants.setText(String.valueOf("Zjedzone rośliny: " + followedAnimal.getPlantsEaten()));
            howManyChildren.setText(String.valueOf("Liczba dzieci: " + followedAnimal.getChildAmount()));
            howManyDescendant.setText(String.valueOf("Liczba potomków: " + followedAnimal.getDescendantAmount()));
            animalLifespan.setText("Długość życia: " + String.valueOf(followedAnimal.getAge()));
            if(followedAnimal.isDead()){
                animalDeathDay.setVisible(true);
                animalDeathDay.setText(String.valueOf("Dzień w którym zmarło: " + followedAnimal.getDeathDay())+" ✞");
            }
            else{
                animalDeathDay.setVisible(false);
            }
        }
        else{
            animalStatsHeader.setText("Wybierz zwierzę na mapie");
            animalGenome.setVisible(false);
            activatedPartOfGenome.setVisible(false);
            animalEnergy.setVisible(false);
            eatenPlants.setVisible(false);
            howManyChildren.setVisible(false);
            howManyDescendant.setVisible(false);
            animalLifespan.setVisible(false);
            animalDeathDay.setVisible(false);
        }
    }

    @Override
    public void mapChanges(WorldMap worldMap,int day, int numberOfAnimals, int numberOfPlants, int freeAreas, List<Integer> genotypes, float averageEnergy, float averageLifespan, float averageNumberOfChildren) {

        Platform.runLater(() -> {
            drawMap();
            dayLabel.setText("Dzień: " + day );
            numberOfAnimalsLabel.setText("Ilość zwierząt: "+String.valueOf(numberOfAnimals));
            numberOfPlantsLabel.setText("Ilość roślin: "+String.valueOf(numberOfPlants));
            freeAreasLabel.setText("Wolne pola: "+String.valueOf(freeAreas));
            genotypesLabel.setText("Najpopularniejszy genotyp: "+genotypes.toString());
            averageEnergyLabel.setText("Średnia ilość energii zwierząt: "+String.valueOf((float)round(averageEnergy*100)/100));
            averageLifespanLabel.setText("Średnia długość życia: "+ String.valueOf((float)round(averageLifespan*100)/100));
            averageNumberOfChildrenLabel.setText("Średnia ilość dzieci: "+ String.valueOf((float)round(averageNumberOfChildren*100)/100));

            animalStats();
        });
    }

    public void onStartStopButtonClicked(ActionEvent actionEvent) {
        try{
            if(thread.isRunning()){
                thread.setRunning(false);
                startStop.setText("Start");
            }
            else{
                thread.setRunning(true);
                startStop.setText("Stop");
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
