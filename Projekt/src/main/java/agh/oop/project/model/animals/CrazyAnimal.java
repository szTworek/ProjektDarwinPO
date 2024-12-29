package agh.oop.project.model.animals;

import agh.oop.project.model.*;
import agh.oop.project.model.worlds.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrazyAnimal extends AbstractAnimal {

    public CrazyAnimal(Vector2d position, ArrayList<Integer> genome, int energy) {
        Random rand = new Random();
        // obrót startowy ma być losowy - możemy zmienić później na testy
        this.direction = MapDirection.values()[rand.nextInt(8)];
        this.position = position;
        this.genome = genome;
        // zaczynamy też od losowego genomu
        nextGenome = rand.nextInt(genome.size());
        this.energy = energy;
    }

    @Override
    public void reproduce(Animal animal, WorldMap map, Specifications specs) {
        ArrayList<Integer> newGenome = createNewGenome(animal);

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
