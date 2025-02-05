package agh.oop.project.model.animals;

import agh.oop.project.model.MapDirection;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.worlds.WorldMap;

import java.util.*;

import static java.lang.Math.min;

public abstract class AbstractAnimal implements Animal {

    protected static final Random rand = new Random();

    protected MapDirection direction;
    protected Vector2d position;
    protected ArrayList<Integer> genome;
    protected int nextGenome;
    protected int energy;
    protected int deathDay;
    protected int age = 0;
    protected int plantsEaten = 0;
    protected List<Animal> children = new LinkedList<>();
    protected long numOfDesc = 0;
    protected Animal parent1;
    protected Animal parent2;
    protected Animal recentDesc;

    @Override
    public void newDesc(Animal desc){
        if (desc != recentDesc){
            recentDesc = desc;
            numOfDesc++;
            if (parent1 != null) parent1.newDesc(desc);
            if (parent2 != null) parent2.newDesc(desc);
        }
    }

    protected static ArrayList<Integer> createRandomGenome(int length){
        ArrayList<Integer> genome = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            genome.add(rand.nextInt(8));
        }
        return genome;
    }

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
    public long getDescendantAmount() {
        return numOfDesc;
    }

    @Override
    public int getAge(){
        return age;
    }

    @Override
    public void setDeathDay(int day){
        deathDay = day;
    }

    @Override
    public int getDeathDay(){
        return deathDay;
    }

    @Override
    public int getPlantsEaten(){
        return plantsEaten;
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
        age++;
        this.turn(genome.get(nextGenome));
        Vector2d newPosition = position.add(direction.toUnitVector()).goAroundTheGlobe(width);
        nextGenome();
        if (map.canMoveTo(newPosition)) position = newPosition;
    }

    @Override
    public void mutateGenome(ArrayList<Integer> genome, int numOfMutations) {
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
            genome.set(changedGenomeIndex, newGenome);
        }
    }

    @Override
    public ArrayList<Integer> createNewGenome(Animal animal, Specifications specifications) {
        ArrayList<Integer> newGenome;
        if (rand.nextBoolean()) newGenome = this.newGenome(animal);
        else newGenome = animal.newGenome(this);

        // mutations
        int numberOfMutations = min(rand.nextInt(specifications.maximalAmountOfMutations()-specifications.minimalAmountOfMutations() + 1) + specifications.minimalAmountOfMutations(), specifications.genomeLength());
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
    public void eat(Specifications specs) {
        this.energy += specs.amountOfEnergyPerPlant();
        plantsEaten++;
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
