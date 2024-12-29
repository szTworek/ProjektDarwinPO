package agh.oop.project;

import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.animals.Animal;
import agh.oop.project.model.animals.CrazyAnimal;
import agh.oop.project.model.animals.NormalAnimal;
import agh.oop.project.model.worlds.ForestedEquator;
import agh.oop.project.model.worlds.LiveGivingCorpse;
import agh.oop.project.model.worlds.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private final Specifications specifications;
    private final WorldMap worldMap;

    public Simulation(Specifications specifications, List<Vector2d> animalPositions, List<ArrayList<Integer>> animalGenomes) {
        this.specifications = specifications;
        if (specifications.normalGrowth()) worldMap = new ForestedEquator(specifications);
        else worldMap = new LiveGivingCorpse(specifications);
        initSimulation(animalPositions, animalGenomes);
    }

    public void initSimulation(List<Vector2d> animalPositions, List<ArrayList<Integer>> animalGenomes) {
        // zakładamy że spec.startingAnimalAmount jest tyle samo so positions.size i genomes.size'

        if (specifications.normalGenome())
            for (int i = 0; i < animalGenomes.size(); i++) {
                worldMap.placeAnimal(new NormalAnimal(animalPositions.get(i), animalGenomes.get(i), specifications.startingEnergyForAnimals()));
            }
        else
            for (int i = 0; i < animalGenomes.size(); i++) {
                worldMap.placeAnimal(new CrazyAnimal(animalPositions.get(i), animalGenomes.get(i), specifications.startingEnergyForAnimals()));
            }

        // dodanie roslin pierwszych
        worldMap.generatePlants(specifications.startingAmountOfPlants());

    }
    public void dayCycle() {
        /*
        1) usunięcie martwych zwierząt
        2) ruch zwierzaków
        3) jedzenie
        4) rozmnażanie się
        5) wzrost nowych roslin
        */
        worldMap.removeDeadAnimals();
        worldMap.moveAllAnimals();
        worldMap.eatingAndReproduction();
        worldMap.generatePlants(specifications.dailyPlantGrowth());
    }
    public void run() {

        int simLength = 100;

        for(int i = 0; i<simLength; i++){
            dayCycle();
            if(worldMap.getLivingAnimalAmount() == 0) break;
        }
    }

}
