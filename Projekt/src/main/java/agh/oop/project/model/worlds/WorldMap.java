package agh.oop.project.model.worlds;

import agh.oop.project.model.Vector2d;
import agh.oop.project.model.animals.Animal;

import java.util.List;

public interface WorldMap {

    void placeAnimal(Animal animal);

    void removeAnimals();

    boolean isPlantAt(Vector2d position);

    boolean canMoveTo( Vector2d position);
  
    void manageReproduction(Vector2d position, List<Animal> animals);

    // obsluguje sytuacje ktora dzieje sie na polu, w szczegolnosci gdy znajduje sie na nim kilka zwierząt
    // jedzenie roslin, rozmnazanie albo nic jezeli zwierze jest slabsze niz pozostale
    void handleAction();

    void generatePlants(int quantity);
}
