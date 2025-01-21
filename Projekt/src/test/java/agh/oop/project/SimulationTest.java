package agh.oop.project;

import agh.oop.project.model.MapDirection;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.WorldElement;
import agh.oop.project.model.animals.Animal;
import agh.oop.project.model.animals.NormalAnimal;
import agh.oop.project.model.app.MapChangeListener;
import agh.oop.project.model.app.SimulationPresenter;
import agh.oop.project.model.worlds.WorldMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    private final Specifications specifications = new Specifications(
            10,10,10,2,5,
            true,0,6,3,
            2,0,0,
            1,true);

    @Test
    void dayCycleTest() {
        Simulation simulation = new Simulation(specifications, null);
        WorldMap map = simulation.getWorldMap();

        Vector2d position1 = new Vector2d(1,1);
        Vector2d position2 = new Vector2d(2,2);
        ArrayList<Integer> genomes = new ArrayList<>();
        genomes.add(0);
        Animal animal1 = new NormalAnimal(position1, genomes, specifications.startingEnergyForAnimals(), MapDirection.NORTH, 0, null, null);
        Animal animal2 = new NormalAnimal(position1, genomes, specifications.startingEnergyForAnimals(), MapDirection.EAST, 0, null, null);
        Animal animal3 = new NormalAnimal(position2, genomes, specifications.startingEnergyForAnimals(), MapDirection.EAST, 0, null, null);
        Animal animal4 = new NormalAnimal(position2, genomes, specifications.startingEnergyForAnimals(), MapDirection.EAST, 0, null, null);

        Animal animalDead1 = new NormalAnimal(position1, genomes, 0, MapDirection.NORTH, 0, null, null);
        Animal animalDead2 = new NormalAnimal(position2, genomes, 0, MapDirection.EAST, 0, null, null);

        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        map.placeAnimal(animalDead1);
        map.placeAnimal(animalDead2);

        simulation.dayCycle();

        assertEquals(5, map.getLivingAnimalAmount());
        assertEquals(new Vector2d(1,2), animal1.getPosition());
        assertEquals(new Vector2d(2,1), animal2.getPosition());
        assertEquals(new Vector2d(3,2), animal3.getPosition());
        assertEquals(new Vector2d(3,2), animal4.getPosition());
        assertEquals(1, animal3.getChildAmount());
        assertEquals(1, animal1.getAge());
        assertEquals(1, animal3.getAge());
        assertEquals(0, animalDead1.getAge());
        assertTrue(map.getPlants().size() >= 11 && map.getPlants().size() <= 15);
    }
}