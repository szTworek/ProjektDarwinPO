package agh.oop.project.model.animals;

import agh.oop.project.model.*;
import agh.oop.project.model.worlds.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrazyAnimal extends AbstractAnimal {

    public CrazyAnimal(Vector2d position, ArrayList<Integer> genome, int energy) {
        this(position, genome, energy, MapDirection.values()[new Random().nextInt(8)], new Random().nextInt(genome.size()));
    }

    public CrazyAnimal(Vector2d position, ArrayList<Integer> genome, int energy, MapDirection direction, int nextGenome) {
        this.direction = direction;
        this.position = position;
        this.genome = genome;
        this.nextGenome = nextGenome;
        this.energy = energy;
    }

    @Override
    public void reproduce(Animal animal, WorldMap map, Specifications specs) {
        ArrayList<Integer> newGenome = createNewGenome(animal, specs);

        decreaseParentsEnergy(animal, specs.energyUsageForReproduction());

        CrazyAnimal kid = new CrazyAnimal(this.position, newGenome, 2*specs.energyUsageForReproduction());

        map.placeAnimal(kid);
    }

    @Override
    public void nextGenome(){
        Random rand = new Random();
        if(rand.nextInt(5)==1){
            nextGenome = rand.nextInt(genome.size()) - 1;
        }else{
            super.nextGenome();
        }
    }
}
