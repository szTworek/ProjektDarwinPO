package agh.oop.project.model;

import java.util.Vector;

public class Plant implements WorldElement{
    private Vector2d position;
    private int plantEnergy;
    public Plant(Vector2d position, int plantEnergy){
        this.position = position;
        this.plantEnergy = plantEnergy;
    }

    @Override
    public Vector2d getPosition(){
        return position;
    }

    public int getPlantEnergy(){
        return plantEnergy;
    }
}
