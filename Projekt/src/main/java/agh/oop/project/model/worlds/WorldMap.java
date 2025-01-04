package agh.oop.project.model.worlds;

import agh.oop.project.model.Vector2d;
import agh.oop.project.model.animals.Animal;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public interface WorldMap {

    Map<Vector2d, List<Animal>> getLivingAnimals();
    Map<Vector2d, Plant> getPlants();

    int getLivingAnimalAmount();

    void placeAnimal(Animal animal);

    void removeDeadAnimals();

    boolean isPlantAt(Vector2d position);

    boolean canMoveTo( Vector2d position);

    void sortAnimals(List<Animal> animals);

    void manageReproduction(List<Animal> animals);

    void eatingAndReproduction();

    void generatePlants(int quantity);

    void moveAllAnimals();
    void move(Animal animal);
}
