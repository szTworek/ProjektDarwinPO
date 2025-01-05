package agh.oop.project.model.animals;

import agh.oop.project.model.MapDirection;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.worlds.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public MapDirection getDirection() {
        return direction;
    }

    public Vector2d getPosition() {
        return position;
    }

    public ArrayList<Integer> getGenome() {
        return genome;
    }

    public int getNextGenome() {
        return genome.get(nextGenome);
    }

    public int getEnergy(){
        return energy;
    }

    public int getChildAmount() {
        return children.size();
    }

    public int getAge(){
        return age;
    }

    public void decreaseEnergy(int amount) {
        energy-=amount;
    }

    public void turn(int turnAmount) {
        int i = direction.ordinal();
        direction = MapDirection.values()[(turnAmount + i) % 8];
    }

    public void move(int width, WorldMap map) {
        this.turn(genome.get(nextGenome));
        Vector2d newPosition = position.add(direction.toUnitVector()).goAroundTheGlobe(width);
        nextGenome();
        if (map.canMoveTo(newPosition)) position = newPosition;

        age++;
    }

        // reproduce is separate for every animal subtype
        // bcs crazyAnimal will create crazyAnimal etc


    // TODO - add a possibility of mutations to createNewGenome
    public ArrayList<Integer> createNewGenome(Animal animal) {
        Random rand = new Random();
        ArrayList<Integer> newGenome;
        if (rand.nextBoolean()) newGenome = this.newGenome(animal);
        else newGenome = animal.newGenome(this);
        return newGenome;
    }

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

    public void addChild(Animal kid){
        children.add(kid);
    }

    public int getDescendantAmount() {
       int result = children.size();
       for(Animal child: children) {
            result += child.getDescendantAmount();
       }
        return result;
    }

    public void eat(Specifications specs) {
        this.energy += specs.amountOfEnergyPerPlant();
    }

    public boolean isHealthy(Specifications specs) {
        return energy >= specs.healthyLimit();
    }

    public boolean isDead() {
        return energy <= 0;
    }

    public void nextGenome() {
        nextGenome = (nextGenome + 1) % genome.size();
    }
}
