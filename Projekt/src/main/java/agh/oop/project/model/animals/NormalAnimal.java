package agh.oop.project.model.animals;

import agh.oop.project.model.MapDirection;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.worlds.WorldMap;

import java.util.ArrayList;
import java.util.Random;

public class NormalAnimal extends AbstractAnimal {

    public NormalAnimal(Vector2d position, ArrayList<Integer> genome, int energy) {
        this(position, genome, energy, MapDirection.values()[new Random().nextInt(8)], new Random().nextInt(genome.size()));
    }

    public NormalAnimal(Vector2d position, ArrayList<Integer> genome, int energy, MapDirection direction, int nextGenome) {
        this.direction = direction;
        this.position = position;
        this.genome = genome;
        this.nextGenome = nextGenome;
        this.energy = energy;
    }


    @Override
    public void reproduce(Animal animal, WorldMap map, Specifications specs) {
        ArrayList<Integer> newGenome = createNewGenome(animal);

        decreaseParentsEnergy(animal, specs.energyUsageForReproduction());

        NormalAnimal kid = new NormalAnimal(this.position, newGenome, 2*specs.energyUsageForReproduction());

        this.addChild(kid);
        animal.addChild(kid);

        map.placeAnimal(kid);
    }
}
