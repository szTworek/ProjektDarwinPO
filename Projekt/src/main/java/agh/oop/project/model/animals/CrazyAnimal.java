package agh.oop.project.model.animals;

import agh.oop.project.model.*;
import agh.oop.project.model.worlds.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrazyAnimal extends AbstractAnimal {

    public CrazyAnimal(Vector2d position, ArrayList<Integer> genome, int energy, Animal parent1, Animal parent2) {
        this(position, genome, energy, MapDirection.values()[rand.nextInt(8)], rand.nextInt(genome.size()), parent1, parent2);
    }

    public CrazyAnimal(Vector2d position, ArrayList<Integer> genome, int energy, MapDirection direction, int nextGenome, Animal parent1, Animal parent2) {
        this.direction = direction;
        this.position = position;
        this.genome = genome;
        this.nextGenome = nextGenome;
        this.energy = energy;
        this.parent1 = parent1;
        this.parent2 = parent2;
    }

    public CrazyAnimal(Specifications specifications) {
        this(new Vector2d(rand.nextInt(specifications.width()), rand.nextInt(specifications.height())),
                createRandomGenome(specifications.genomeLength()),
                specifications.startingEnergyForAnimals(), null, null);
    }

    @Override
    public void reproduce(Animal animal, WorldMap map, Specifications specs) {

        ArrayList<Integer> newGenome = createNewGenome(animal, specs);

        decreaseParentsEnergy(animal, specs.energyUsageForReproduction());

        CrazyAnimal kid = new CrazyAnimal(this.position, newGenome, 2*specs.energyUsageForReproduction(), this, animal);

        this.addChild(kid);
        animal.addChild(kid);

        this.newDesc(kid);
        animal.newDesc(kid);

        map.placeAnimal(kid);
    }

    @Override
    public void nextGenome(){
        if(rand.nextInt(5)==1){
            nextGenome = rand.nextInt(genome.size());
        }else{
            super.nextGenome();
        }
    }
}
