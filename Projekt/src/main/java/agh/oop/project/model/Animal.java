package agh.oop.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement{
    // propozycja: Animal dajmy jako interface, większosć metod dajmy w AbstractAnimal
    // i później będą 2 różne animale - jeden normalny a drugi z odrobiną szaleństwa

    private MapDirection direction;
    private Vector2d position;
    private ArrayList<Integer> genome;
    private int nextGenome;
    private int energy;
    private List<Animal> children = new ArrayList<>();

    public Animal(Vector2d position, ArrayList<Integer> genome, int energy) {
        Random rand = new Random();
        // obrót startowy ma być losowy - możemy zmienić później na testy
        this.direction = MapDirection.values()[rand.nextInt(8)];
        this.position = position;
        this.genome = genome;
        // zaczynamy też od losowego genomu
        nextGenome = rand.nextInt(genome.size());
        this.energy = energy;
    }

    public Animal(Vector2d position, ArrayList<Integer> genome){

        //poczatkowe zwierzeta maja full energii ale przy rozmnazaniu konieczne jest juz jej podawanie
        this(position, genome, 100);
    }

    public String toString() {
        return (position + " " + direction);
    }

    boolean isAt(Vector2d position) {
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

    public void turn(int turnAmount) {
        // obrót zwierzaka
        // pierwsza częsć ruchu - można później do metody move dać
        int i = direction.ordinal();
        direction = MapDirection.values()[(turnAmount + i) % 8];
    }
}
