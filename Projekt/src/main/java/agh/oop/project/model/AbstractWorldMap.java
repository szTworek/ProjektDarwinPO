package agh.oop.project.model;

import java.util.Map;

public abstract class AbstractWorldMap implements WorldMap{
    private Map<Vector2d,Animal> livingAnimals=new java.util.HashMap<>();
    private Map<Vector2d,Animal> deadAnimals=new java.util.HashMap<>();
    private Map<Vector2d,Plant> plants=new java.util.HashMap<>();
    private int height;
    private int width;
//    to pojdzie do klasy simulation i pozniej stworzymy funkcje generatePlants(int plantQuantity)
//    private int startPlantQuantity;

    public AbstractWorldMap(int height, int width, int startPlantQuantity){
        this.height=height;
        this.width=width;
    }

    public void placeAnimal(Animal animal, Vector2d position){
        livingAnimals.put(position,animal);
    }
}
