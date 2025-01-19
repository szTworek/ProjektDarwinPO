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
        Vector2d position1 = new Vector2d(1,1);
        Vector2d position2 = new Vector2d(2,2);
        ArrayList<Integer> genomes = new ArrayList<>();
        genomes.add(0);
        Animal animal1 = new NormalAnimal(position1, genomes, 10, MapDirection.NORTH, 0);
        Animal animal2 = new NormalAnimal(position1, genomes, 8, MapDirection.EAST, 0);
        Animal animal3 = new NormalAnimal(position1, genomes, 6, MapDirection.NORTH, 0);
        Animal animal4 = new NormalAnimal(position1, genomes, 4, MapDirection.EAST, 0);
        Animal animal5 = new NormalAnimal(position1, genomes, 2, MapDirection.NORTH, 0);
        Animal animal6 = new NormalAnimal(position2, genomes, 10, MapDirection.EAST, 0);
        WorldMap map = new ForestedEquator(specifications);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        map.placeAnimal(animal5);
        map.placeAnimal(animal6);
        map.placePlant(position1);
        map.placePlant(position2);

        map.eatingAndReproduction();

        assertEquals(10, animal1.getEnergy());
        assertEquals(6, animal2.getEnergy());
        assertEquals(4, animal3.getEnergy());
        assertEquals(2, animal4.getEnergy());
        assertEquals(2, animal5.getEnergy());
        assertEquals(12, animal6.getEnergy());
        assertEquals(44, map.getSumOfLivingEnergy());

        assertEquals(1, animal1.getDescendantAmount());
        assertEquals(1, animal1.getChildAmount());
        assertEquals(1, animal2.getDescendantAmount());
        assertEquals(1, animal2.getChildAmount());
        assertEquals(1, animal3.getDescendantAmount());
        assertEquals(1, animal3.getChildAmount());
        assertEquals(1, animal4.getDescendantAmount());
        assertEquals(1, animal4.getChildAmount());
        assertEquals(0, animal5.getDescendantAmount());
        assertEquals(0, animal5.getChildAmount());
        assertEquals(0, animal6.getDescendantAmount());
        assertEquals(0, animal6.getChildAmount());
        assertEquals(4, map.getSumOfKids());

        assertEquals(8, map.getLivingAnimalAmount());
        assertEquals(7, map.getLivingAnimals().get(position1).size());
        assertEquals(1, map.getLivingAnimals().get(position2).size());
    }

    @Test
    void moveAllAnimalsTest(){
        Vector2d position1 = new Vector2d(1,1);
        Vector2d position2 = new Vector2d(2,2);
        ArrayList<Integer> genomes = new ArrayList<>();
        genomes.add(0);
        Animal animal1 = new NormalAnimal(position1, genomes, 10, MapDirection.NORTH, 0);
        Animal animal2 = new NormalAnimal(position1, genomes, 8, MapDirection.SOUTH, 0);
        Animal animal3 = new NormalAnimal(position1, genomes, 6, MapDirection.NORTH, 0);
        Animal animal4 = new NormalAnimal(position1, genomes, 4, MapDirection.NW, 0);
        Animal animal5 = new NormalAnimal(position1, genomes, 2, MapDirection.NORTH, 0);
        Animal animal6 = new NormalAnimal(position2, genomes, 10, MapDirection.SOUTH, 0);
        WorldMap map = new ForestedEquator(specifications);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        map.placeAnimal(animal5);
        map.placeAnimal(animal6);

        map.moveAllAnimals();

        assertEquals(9, animal1.getEnergy());
        assertEquals(7, animal2.getEnergy());
        assertEquals(5, animal3.getEnergy());
        assertEquals(3, animal4.getEnergy());
        assertEquals(1, animal5.getEnergy());
        assertEquals(9, animal6.getEnergy());
        assertEquals(34, map.getSumOfLivingEnergy());
        assertEquals(6, map.getLivingAnimalAmount());

        assertEquals(new Vector2d(1,2), animal1.getPosition());
        assertEquals(new Vector2d(1,0), animal2.getPosition());
        assertEquals(new Vector2d(1,2), animal3.getPosition());
        assertEquals(new Vector2d(0,2), animal4.getPosition());
        assertEquals(new Vector2d(1,2), animal5.getPosition());
        assertEquals(new Vector2d(2,1), animal6.getPosition());

        assertEquals(MapDirection.NORTH, animal1.getDirection());
        assertEquals(MapDirection.SOUTH, animal2.getDirection());
        assertEquals(MapDirection.NORTH, animal3.getDirection());
        assertEquals(MapDirection.NW, animal4.getDirection());
        assertEquals(MapDirection.NORTH, animal5.getDirection());
        assertEquals(MapDirection.SOUTH, animal6.getDirection());

        assertEquals(4, map.getLivingAnimals().size());
    }

    @Test
    void generateBetterAreaTest(){
        WorldMap map1 = new ForestedEquator(specifications);
        WorldMap map2 = new LiveGivingCorpse(specifications);

        ArrayList<Integer> genomes = new ArrayList<>();
        genomes.add(0);

        map1.generateBetterArea();
        map2.generateBetterArea();

        assertEquals(20, map1.getBetterArea().size());
        assertEquals(0, map2.getBetterArea().size());

        Animal animal1 = new NormalAnimal(new Vector2d(5,5), genomes, 0, MapDirection.NORTH, 0);
        map2.placeAnimal(animal1);
        map2.removeDeadAnimals();
        map2.generateBetterArea();
        assertEquals(25, map2.getBetterArea().size());

        Animal animal2 = new NormalAnimal(new Vector2d(7,8), genomes, 0, MapDirection.NORTH, 0);
        map2.placeAnimal(animal2);
        map2.removeDeadAnimals();
        map2.generateBetterArea();
        assertEquals(21, map2.getBetterArea().size());

        map2.placeAnimal(animal1);
        map2.placeAnimal(animal2);
        map2.removeDeadAnimals();
        map2.generateBetterArea();
        assertEquals(25, map2.getBetterArea().size());
    }

    @Test
    void generatePlantsTest() {
        WorldMap map1 = new ForestedEquator(specifications);
        WorldMap map2 = new LiveGivingCorpse(specifications);

        map1.generatePlants(20);
        map2.generatePlants(20);

        assertEquals(20, map1.getPlants().size());
        assertEquals(20, map2.getPlants().size());
        assertEquals(80, map1.getFreeAreas());
        assertEquals(80, map2.getFreeAreas());

        map1.generatePlants(15);
        map2.generatePlants(15);

        assertEquals(35, map1.getPlants().size());
        assertEquals(35, map2.getPlants().size());
        assertEquals(65, map1.getFreeAreas());
        assertEquals(65, map2.getFreeAreas());
    }

    @Test
    void getTheMostPopularGenotypeTest(){
        Vector2d position1 = new Vector2d(1,1);
        Vector2d position2 = new Vector2d(2,2);
        ArrayList<Integer> genome0 = new ArrayList<>();
        genome0.add(0);
        ArrayList<Integer> genome1 = new ArrayList<>();
        genome1.add(1);
        ArrayList<Integer> genome2 = new ArrayList<>();
        genome2.add(2);

        Animal animal1 = new NormalAnimal(position1, genome1, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0);
        Animal animal2 = new NormalAnimal(position1, genome2, specifications.startingEnergyForAnimals(), MapDirection.SOUTH, 0);
        Animal animal3 = new NormalAnimal(position1, genome0, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0);
        Animal animal4 = new NormalAnimal(position1, genome0, specifications.startingEnergyForAnimals(), MapDirection.NW, 0);
        Animal animal5 = new NormalAnimal(position1, genome1, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0);
        Animal animal6 = new NormalAnimal(position2, genome1, specifications.startingEnergyForAnimals(), MapDirection.SOUTH, 0);
        WorldMap map = new ForestedEquator(specifications);

        map.placeAnimal(animal1);
        assertEquals(genome1, map.getTheMostPopularGenotype());
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        assertEquals(genome0, map.getTheMostPopularGenotype());
        map.placeAnimal(animal5);
        map.placeAnimal(animal6);
        assertEquals(genome1, map.getTheMostPopularGenotype());
    }
}