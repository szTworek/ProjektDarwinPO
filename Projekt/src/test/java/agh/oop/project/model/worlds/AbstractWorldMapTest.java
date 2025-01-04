package agh.oop.project.model.worlds;

import agh.oop.project.model.MapDirection;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.animals.Animal;
import agh.oop.project.model.animals.NormalAnimal;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AbstractWorldMapTest {

    private final Specifications specifications = new Specifications(
            10,10,10,2,5,true,2,6,3,2,0,0,4,true);

    @Test
    void placeAnimalTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genomes = new ArrayList<>();
        Animal animal1 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0);
        Animal animal2 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.EAST, 0);
        WorldMap map = new ForestedEquator(specifications);

        map.placeAnimal(animal1);
        assertEquals(1, map.getLivingAnimals().size());
        map.placeAnimal(animal2);
        assertEquals(1, map.getLivingAnimals().size());
        assertEquals(2, map.getLivingAnimals().get(position).size());
        assertEquals(2, map.getLivingAnimalAmount());
    }

    @Test
    void removeDeadAnimalsTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genomes = new ArrayList<>();
        Animal animal1 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0);
        Animal animal2 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.EAST, 0);
        WorldMap map = new ForestedEquator(specifications);

        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        assertEquals(2, map.getLivingAnimalAmount());
        animal1.decreaseEnergy(6);
        map.removeDeadAnimals();
        assertEquals(1, map.getLivingAnimalAmount());
        assertEquals(1, map.getLivingAnimals().get(position).size());
        animal2.decreaseEnergy(7);
        map.removeDeadAnimals();
        assertEquals(0, map.getLivingAnimalAmount());
        assertEquals(0, map.getLivingAnimals().size());
    }

    @Test
    void canMoveToTest(){
        WorldMap map = new ForestedEquator(specifications);
        assertTrue(map.canMoveTo(new Vector2d(9,9)));
        assertTrue(map.canMoveTo(new Vector2d(0,0)));
        assertTrue(map.canMoveTo(new Vector2d(9,0)));
        assertTrue(map.canMoveTo(new Vector2d(0,9)));
        assertTrue(map.canMoveTo(new Vector2d(4, 5)));
        assertTrue(map.canMoveTo(new Vector2d(-1,0)));
        assertTrue(map.canMoveTo(new Vector2d(-1,9)));
        assertTrue(map.canMoveTo(new Vector2d(10,0)));
        assertTrue(map.canMoveTo(new Vector2d(10,9)));
        assertFalse(map.canMoveTo(new Vector2d(0,-1)));
        assertFalse(map.canMoveTo(new Vector2d(9,-1)));
        assertFalse(map.canMoveTo(new Vector2d(0,10)));
        assertFalse(map.canMoveTo(new Vector2d(9,10)));
        assertFalse(map.canMoveTo(new Vector2d(-1,-1)));
        assertFalse(map.canMoveTo(new Vector2d(-1,10)));
        assertFalse(map.canMoveTo(new Vector2d(10,-1)));
        assertFalse(map.canMoveTo(new Vector2d(10,10)));
    }

    @Test
    void manageReproductionAndPartialSortAnimalsTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genomes = new ArrayList<>();
        genomes.add(0);
        Animal animal1 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0);
        Animal animal2 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.EAST, 0);
        WorldMap map = new ForestedEquator(specifications);

        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.sortAnimals(map.getLivingAnimals().get(position));
        assertEquals(2, map.getLivingAnimalAmount());
        map.sortAnimals(map.getLivingAnimals().get(position));
        map.manageReproduction(map.getLivingAnimals().get(position));
        assertEquals(3, map.getLivingAnimalAmount());
        map.sortAnimals(map.getLivingAnimals().get(position));
        map.manageReproduction(map.getLivingAnimals().get(position));
        assertEquals(4, map.getLivingAnimalAmount());
        map.sortAnimals(map.getLivingAnimals().get(position));
        map.manageReproduction(map.getLivingAnimals().get(position));
        assertEquals(5, map.getLivingAnimalAmount());
        map.sortAnimals(map.getLivingAnimals().get(position));
        map.manageReproduction(map.getLivingAnimals().get(position));
        assertEquals(5, map.getLivingAnimalAmount());
    }

    @Test
    void eatingAndReproductionTest(){
        //TODO
    }

    @Test
    void moveAllAnimalsTest(){
        //TODO
    }

    //TODO - Testy do generatePlants w innych plikach
}