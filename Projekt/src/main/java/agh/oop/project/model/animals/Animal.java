package agh.oop.project.model.animals;

import agh.oop.project.model.*;
import agh.oop.project.model.worlds.WorldMap;

import java.util.ArrayList;

public interface Animal extends WorldElement {
    boolean isAt (Vector2d position);

    // getters
    MapDirection getDirection();
    ArrayList<Integer> getGenome();
    int getEnergy();
    int getAge();
    int getChildAmount();

    // moving
    void turn(int turnAmount);
    void move();

    // sex
    void reproduce(Animal animal, WorldMap map, Specifications specs);
    ArrayList<Integer> newGenome(Animal animal);
    int getDescendantAmount();

    // eat
    void eat(Specifications specs);

    // status of animal
    boolean isHealthy(Specifications specs);
    boolean isDead();

    //genome manipulations
    void nextGenome();

}
