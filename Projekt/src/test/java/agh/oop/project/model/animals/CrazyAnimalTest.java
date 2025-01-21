package agh.oop.project.model.animals;

import agh.oop.project.model.MapDirection;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.worlds.ForestedEquator;
import agh.oop.project.model.worlds.WorldMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CrazyAnimalTest {

    private final Specifications specifications = new Specifications(
            10,10,10,2,5,true,2,6,3,2,0,0,4,true);


    @Test
    void reproduce() {
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genomes = new ArrayList<>();
        genomes.add(1);
        Animal animal1 = new CrazyAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0, null, null);
        Animal animal2 = new CrazyAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.EAST, 0, null, null);
        WorldMap map = new ForestedEquator(specifications);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);

        animal1.reproduce(animal2, map, specifications);
        assertEquals(3, map.getLivingAnimalAmount());
        assertEquals(specifications.startingEnergyForAnimals()-specifications.energyUsageForReproduction(), animal1.getEnergy());
        assertEquals(specifications.startingEnergyForAnimals()-specifications.energyUsageForReproduction(), animal2.getEnergy());
        assertEquals(1, animal1.getChildAmount());
        assertEquals(1, animal2.getChildAmount());
        assertEquals(1, animal1.getDescendantAmount());
        assertEquals(1, animal2.getDescendantAmount());

        animal2.reproduce(animal1, map, specifications);
        assertEquals(4, map.getLivingAnimalAmount());
        assertEquals(specifications.startingEnergyForAnimals()-2*specifications.energyUsageForReproduction(), animal1.getEnergy());
        assertEquals(specifications.startingEnergyForAnimals()-2*specifications.energyUsageForReproduction(), animal2.getEnergy());
        assertEquals(2, animal1.getChildAmount());
        assertEquals(2, animal2.getChildAmount());
        assertEquals(2, animal1.getDescendantAmount());
        assertEquals(2, animal2.getDescendantAmount());
    }
}