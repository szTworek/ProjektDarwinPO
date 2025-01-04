package agh.oop.project.model.animals;

import agh.oop.project.model.*;
import agh.oop.project.model.worlds.WorldMap;

import java.util.ArrayList;

public interface Animal extends WorldElement {
    boolean isAt (Vector2d position);

    // getters
    MapDirection getDirection();
    ArrayList<Integer> getGenome();
    int getNextGenome();
    int getEnergy();
    int getAge();
    int getChildAmount();
    int getDescendantAmount();

    // moving
    void turn(int turnAmount);
    void move(int width, WorldMap map);

    // sex
    void reproduce(Animal animal, WorldMap map, Specifications specs);
    void mutateGenome(ArrayList<Integer> genome, int numOfMutations);
    ArrayList<Integer> createNewGenome(Animal animal, Specifications specs);
    ArrayList<Integer> newGenome(Animal animal);
    void addChild(Animal child);

    void decreaseEnergy(int amount);

    // eat
    void eat(Specifications specs);

    // status of animal
    boolean isHealthy(Specifications specs);
    boolean isDead();

    //genome manipulations
    void nextGenome();

}
