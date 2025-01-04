package agh.oop.project.model.animals;

import agh.oop.project.model.MapDirection;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.worlds.ForestedEquator;
import agh.oop.project.model.worlds.WorldMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractAnimalTest {

    private final Specifications specifications = new Specifications(
            10,10,10,2,5,true,2,6,3,2,0,0,4,true);

    @Test
    void getChildAmountTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genomes = new ArrayList<>(4);
        genomes.add(1);
        genomes.add(2);
        genomes.add(3);
        genomes.add(4);
        Animal animal1 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals());
        Animal animal2 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals());
        Animal animal3 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals());

        animal1.reproduce(animal2, new ForestedEquator(specifications), specifications);
        animal2.reproduce(animal3, new ForestedEquator(specifications), specifications);
        animal1.reproduce(animal2, new ForestedEquator(specifications), specifications);

        assertEquals(2, animal1.getChildAmount());
        assertEquals(3, animal2.getChildAmount());
        assertEquals(1, animal3.getChildAmount());
    }

    @Test
    void turnTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genomes = new ArrayList<>();
        Animal animal1 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0);

        animal1.turn(1);
        assertEquals(MapDirection.NE, animal1.getDirection());

        animal1.turn(2);
        assertEquals(MapDirection.SE, animal1.getDirection());

        animal1.turn(3);
        assertEquals(MapDirection.WEST, animal1.getDirection());

        animal1.turn(4);
        assertEquals(MapDirection.EAST, animal1.getDirection());

        animal1.turn(0);
        assertEquals(MapDirection.EAST, animal1.getDirection());
    }

    @Test
    void moveTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genomes = new ArrayList<>();
        genomes.add(0);
        genomes.add(6);
        genomes.add(7);
        genomes.add(5);
        Animal animal1 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0);
        WorldMap map = new ForestedEquator(specifications);

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.NORTH, animal1.getDirection());
        assertEquals(new Vector2d(1,2), animal1.getPosition());

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.WEST, animal1.getDirection());
        assertEquals(new Vector2d(0,2), animal1.getPosition());

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.SW, animal1.getDirection());
        assertEquals(new Vector2d(specifications.width()-1,1), animal1.getPosition());

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.EAST, animal1.getDirection());
        assertEquals(new Vector2d(0,1), animal1.getPosition());

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.EAST, animal1.getDirection());
        assertEquals(new Vector2d(1,1), animal1.getPosition());
    }

    @Test
    void isBlockedByPoles(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genomes = new ArrayList<>();
        genomes.add(0);
        genomes.add(0);
        genomes.add(1);
        genomes.add(6);
        Animal animal1 = new NormalAnimal(position, genomes, specifications.startingEnergyForAnimals(), MapDirection.SOUTH, 0);
        WorldMap map = new ForestedEquator(specifications);

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.SOUTH, animal1.getDirection());
        assertEquals(new Vector2d(1,0), animal1.getPosition());

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.SOUTH, animal1.getDirection());
        assertEquals(new Vector2d(1, 0), animal1.getPosition());

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.SW, animal1.getDirection());
        assertEquals(new Vector2d(1,0), animal1.getPosition());

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.SE, animal1.getDirection());
        assertEquals(new Vector2d(1,0), animal1.getPosition());

        animal1.move(specifications.width(), map);
        assertEquals(MapDirection.SE, animal1.getDirection());
        assertEquals(new Vector2d(1,0), animal1.getPosition());
    }

    @Test
    void createNewGenomeTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genome1 = new ArrayList<>();
        genome1.add(0);
        genome1.add(0);
        genome1.add(0);
        genome1.add(0);
        ArrayList<Integer> genome2 = new ArrayList<>();
        genome2.add(1);
        genome2.add(1);
        genome2.add(1);
        genome2.add(1);
        Animal animal1 = new NormalAnimal(position, genome1, 10);
        Animal animal2 = new NormalAnimal(position, genome2, 10);

        ArrayList<Integer> resultGenome1 = new ArrayList<>();
        resultGenome1.add(0);
        resultGenome1.add(0);
        resultGenome1.add(1);
        resultGenome1.add(1);
        ArrayList<Integer> resultGenome2 = new ArrayList<>();
        resultGenome2.add(1);
        resultGenome2.add(1);
        resultGenome2.add(0);
        resultGenome2.add(0);

        ArrayList<Integer> resultGenome3 = new ArrayList<>();
        resultGenome3.add(0);
        resultGenome3.add(0);
        resultGenome3.add(1);
        resultGenome3.add(1);
        ArrayList<Integer> resultGenome4 = new ArrayList<>();
        resultGenome4.add(1);
        resultGenome4.add(1);
        resultGenome4.add(1);
        resultGenome4.add(0);

        ArrayList<Integer> newGenome = animal1.createNewGenome(animal2);
        assertTrue(newGenome.equals(resultGenome1) || newGenome.equals(resultGenome2));
        animal1.decreaseEnergy(5);
        newGenome = animal1.createNewGenome(animal2);
        assertTrue(newGenome.equals(resultGenome3) || newGenome.equals(resultGenome4));
    }

    @Test
    void newGenomeTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genome1 = new ArrayList<>();
        genome1.add(0);
        genome1.add(0);
        genome1.add(0);
        genome1.add(0);
        ArrayList<Integer> genome2 = new ArrayList<>();
        genome2.add(1);
        genome2.add(1);
        genome2.add(1);
        genome2.add(1);
        ArrayList<Integer> resultGenome1 = new ArrayList<>();
        resultGenome1.add(0);
        resultGenome1.add(0);
        resultGenome1.add(1);
        resultGenome1.add(1);
        ArrayList<Integer> resultGenome2 = new ArrayList<>();
        resultGenome2.add(1);
        resultGenome2.add(1);
        resultGenome2.add(0);
        resultGenome2.add(0);
        ArrayList<Integer> resultGenome3 = new ArrayList<>();
        resultGenome3.add(0);
        resultGenome3.add(0);
        resultGenome3.add(1);
        resultGenome3.add(1);
        ArrayList<Integer> resultGenome4 = new ArrayList<>();
        resultGenome4.add(1);
        resultGenome4.add(1);
        resultGenome4.add(1);
        resultGenome4.add(0);
        Animal animal1 = new NormalAnimal(position, genome1, 10);
        Animal animal2 = new NormalAnimal(position, genome2, 10);

        assertEquals(resultGenome1, animal1.newGenome(animal2));
        assertEquals(resultGenome2, animal2.newGenome(animal1));

        animal1.decreaseEnergy(5);
        assertEquals(resultGenome3, animal1.newGenome(animal2));
        assertEquals(resultGenome4, animal2.newGenome(animal1));
    }

    @Test
    void decreaseEnergyTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genome1 = new ArrayList<>();
        genome1.add(0);
        Animal animal1 = new NormalAnimal(position, genome1, 10);

        animal1.decreaseEnergy(3);
        assertEquals(7, animal1.getEnergy());
    }

    @Test
    void getDescendantAmountTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genome1 = new ArrayList<>();
        genome1.add(2);
        Animal animal1 = new NormalAnimal(position, genome1, 10);
        Animal animal2 = new NormalAnimal(position, genome1, 10);
        Animal animal3 = new NormalAnimal(position, genome1, 10);
        Animal animal4 = new NormalAnimal(position, genome1, 10);

        animal1.addChild(animal2);
        animal2.addChild(animal3);
        animal3.addChild(animal4);

        assertEquals(3, animal1.getDescendantAmount());
        assertEquals(2, animal2.getDescendantAmount());
        assertEquals(1, animal3.getDescendantAmount());
        assertEquals(0, animal4.getDescendantAmount());
    }

    @Test
    void eatTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genome1 = new ArrayList<>();
        genome1.add(0);
        Animal animal1 = new NormalAnimal(position, genome1, 10);

        animal1.eat(specifications);

        assertEquals(12, animal1.getEnergy());
    }

    @Test
    void isHealthyTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genome1 = new ArrayList<>();
        genome1.add(0);
        Animal animal1 = new NormalAnimal(position, genome1, specifications.startingEnergyForAnimals());

        assertTrue(animal1.isHealthy(specifications));
        animal1.decreaseEnergy(3);
        assertTrue(animal1.isHealthy(specifications));
        animal1.decreaseEnergy(1);
        assertFalse(animal1.isHealthy(specifications));
    }

    @Test
    void isDeadTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genome1 = new ArrayList<>();
        genome1.add(0);
        Animal animal1 = new NormalAnimal(position, genome1, 10);

        assertFalse(animal1.isDead());
        animal1.decreaseEnergy(9);
        assertFalse(animal1.isDead());
        animal1.decreaseEnergy(1);
        assertTrue(animal1.isDead());
    }

    @Test
    void nextGenomeTest(){
        Vector2d position = new Vector2d(1,1);
        ArrayList<Integer> genome1 = new ArrayList<>();
        genome1.add(2);
        genome1.add(3);
        Animal animal1 = new NormalAnimal(position, genome1, 10, MapDirection.NORTH, 0);

        assertEquals(2, animal1.getNextGenome());
        animal1.nextGenome();
        assertEquals(3, animal1.getNextGenome());
    }

}