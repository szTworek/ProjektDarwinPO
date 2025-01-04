package agh.oop.project.model.animals;

import agh.oop.project.model.MapDirection;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.worlds.WorldMap;

import java.util.*;

public abstract class AbstractAnimal implements Animal {

    protected MapDirection direction;
    protected Vector2d position;
    protected ArrayList<Integer> genome;
    protected int nextGenome;
    protected int energy;
    protected int age = 0;
    protected List<Animal> children = new ArrayList<>();

    public String toString() {
        return (position + " " + direction);
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    @Override
    public MapDirection getDirection() {
        return direction;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public ArrayList<Integer> getGenome() {
        return genome;
    }

    @Override
    public int getNextGenome() {
        return genome.get(nextGenome);
    }

    @Override
    public int getEnergy(){
        return energy;
    }

    @Override
    public int getChildAmount() {
        return children.size();
    }

    @Override
    public int getAge(){
        return age;
    }

    @Override
    public void decreaseEnergy(int amount) {
        energy-=amount;
    }

    @Override
    public void turn(int turnAmount) {
        int i = direction.ordinal();
        direction = MapDirection.values()[(turnAmount + i) % 8];
    }

    @Override
    public void move(int width, WorldMap map) {
        this.turn(genome.get(nextGenome));
        Vector2d newPosition = position.add(direction.toUnitVector()).goAroundTheGlobe(width);
        nextGenome();

        if (map.canMoveTo(newPosition)) position = newPosition;

        age++;
    }

        // reproduce is separate for every animal subtype
        // bcs crazyAnimal will create crazyAnimal etc

    @Override
    public void mutateGenome(ArrayList<Integer> genome, int numOfMutations) {
        Random rand = new Random();

        int length = genome.size();
        LinkedList<Integer> ableToChange = new LinkedList<>();

        for (int i = length-1; i >= 0; i--) {
            ableToChange.addFirst(i);
        }

        for(int i = 0; i < numOfMutations; i++) {
            int index = rand.nextInt(ableToChange.size());
            int changedGenomeIndex = ableToChange.get(index);
            ableToChange.remove(index);

            int newGenome = rand.nextInt(8);
            while(newGenome == genome.get(changedGenomeIndex)) newGenome = rand.nextInt(8);
            System.out.println("old genome " + genome.get(changedGenomeIndex) + "| new genome " + newGenome);
            genome.set(changedGenomeIndex, newGenome);
        }
    }

    @Override
    public ArrayList<Integer> createNewGenome(Animal animal, Specifications specifications) {
        Random rand = new Random();
        ArrayList<Integer> newGenome;
        if (rand.nextBoolean()) newGenome = this.newGenome(animal);
        else newGenome = animal.newGenome(this);

        // mutations
        int numberOfMutations = rand.nextInt(specifications.maximalAmountOfMutations()-specifications.minimalAmountOfMutations() + 1) + specifications.minimalAmountOfMutations();
        mutateGenome(newGenome, numberOfMutations);

        return newGenome;
    }

    @Override
    public ArrayList<Integer> newGenome(Animal animal) {
        ArrayList<Integer> newGenome = new ArrayList<>();
        int i = 0;
        double ratio = (double) this.energy / (this.energy+animal.getEnergy()) * genome.size();
        for (; i < ratio; i++) {
            newGenome.add(this.getGenome().get(i));
        }
        for (; i < genome.size(); i++) {
            newGenome.add(animal.getGenome().get(i));
        }
        return newGenome;
    }

    public void decreaseParentsEnergy(Animal parent2, int amount) {
        this.decreaseEnergy(amount);
        parent2.decreaseEnergy(amount);
    }

    @Override
    public void addChild(Animal kid){
        children.add(kid);
    }

    @Override
    public int getDescendantAmount() {
       int result = children.size();
       for(Animal child: children) {
            result += child.getDescendantAmount();
       }
        return result;
    }

    @Override
    public void eat(Specifications specs) {
        this.energy += specs.amountOfEnergyPerPlant();
    }

    @Override
    public boolean isHealthy(Specifications specs) {
        return energy >= specs.healthyLimit();
    }

    @Override
    public boolean isDead() {
        return energy <= 0;
    }

    @Override
    public void nextGenome() {
        nextGenome = (nextGenome + 1) % genome.size();
    }
}
