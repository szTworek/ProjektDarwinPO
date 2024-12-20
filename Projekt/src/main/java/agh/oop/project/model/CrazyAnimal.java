package agh.oop.project.model;

import java.util.ArrayList;
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
    public CrazyAnimal(Vector2d position, ArrayList<Integer> genome){
        //poczatkowe zwierzeta maja full energii ale przy rozmnazaniu konieczne jest juz jej podawanie
        this(position, genome, 100);
    }


    @Override
    public void reproduce(Animal animal, WorldMap map, Specifications specs) {
        Random rand = new Random();
        ArrayList<Integer> newGenome;
        if (rand.nextBoolean()) newGenome = this.newGenome(animal);
        else newGenome = animal.newGenome(this);

        // decreasing parents energy

        CrazyAnimal kid = new CrazyAnimal(this.position, newGenome, 2*specs.getEnergyUsageForReproduction());

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
