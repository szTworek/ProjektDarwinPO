package agh.oop.project.model.app;

import agh.oop.project.model.worlds.WorldMap;

import java.util.List;
import java.util.Map;

public interface MapChangeListener {
    void mapChanges(WorldMap worldMap, int numberOfAnimals, int numberOfPlants, int freeAreas, Map<List<Integer>, Integer> genotypes, float averageEnergy, float averageLifespan, float averageNumberOfChildren);
}
