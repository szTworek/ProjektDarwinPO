package agh.oop.project.model.animals;

import agh.oop.project.model.MapDirection;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;

import java.util.ArrayList;
import java.util.List;

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

    public int getEnergy(){
        return energy;
    }

    public int getChildAmount() {
        return children.size();
    }

    public int getAge(){
        return age;
    }

    public void turn(int turnAmount) {
        // obrót zwierzaka
        // pierwsza częsć ruchu - można później do metody move dać
        int i = direction.ordinal();
        direction = MapDirection.values()[(turnAmount + i) % 8];
    }

    public void move(){
        this.turn(genome.get(nextGenome));
        position.add(direction.toUnitVector());
        nextGenome();
    }

        // reproduce will be separate for every animal subtype
        // bcs crazyAnimal will create crazyAnimal etc

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
