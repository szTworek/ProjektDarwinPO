package agh.oop.project.model;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap{
    protected Map<Vector2d, List<Animal>> livingAnimals=new HashMap<>();
    protected Map<Vector2d,List<Animal>> deadAnimals=new HashMap<>();
    protected Map<Vector2d,Plant> plants=new HashMap<Vector2d, Plant>();
    protected Vector2d lowerLeft = new Vector2d(0,0);
    protected Vector2d upperRight ;

    public AbstractWorldMap(int height, int width){
        upperRight =new Vector2d(width,height) ;
    }

    @Override
    public void placeAnimal(Animal animal, Vector2d position) {
        livingAnimals.putIfAbsent(position, new ArrayList<>());
        livingAnimals.get(position).add(animal);
    }

    @Override
    public void removeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        List<Animal> animalsAtPosition = livingAnimals.get(position);
        animalsAtPosition.remove(animal);
        if (animalsAtPosition.isEmpty()) {
            livingAnimals.remove(position);
        }
        deadAnimals.putIfAbsent(position, new ArrayList<>());
        deadAnimals.get(position).add(animal);
    }

    @Override
    public boolean isPlantAt(Vector2d position) {
        return plants.containsKey(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(lowerLeft) && position.precedes(upperRight);
    }

    @Override
    public void manageReproduction(Vector2d position,List<Animal> animals) {
        for (int i=1; i<animals.size(); i+=2) {
            Animal animal1 = animals.get(i-1);
            Animal animal2 = animals.get(i);
            animal1.reproduce(animal2, this, specification);
        }
    }


    @Override
    public void handleAction(){
        // trzeba usuwać listy z animalami z pozycji jezeli nic na nich nie stoi, w przeciwnym
        // wypadku nie oplaca sie uzywac hashmapy
        for (Vector2d position : livingAnimals.keySet()) {
            List<Animal> animalsAtPosition = livingAnimals.get(position);
            animalsAtPosition.sort(Comparator.comparingInt(Animal::getEnergy).reversed());
            if (isPlantAt(position)) {animalsAtPosition.getFirst().eat();}
            if (animalsAtPosition.size()>=2){
                manageReproduction(position, animalsAtPosition);
            }
        }
    }



}
