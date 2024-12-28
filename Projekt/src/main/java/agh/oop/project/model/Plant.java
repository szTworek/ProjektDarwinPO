package agh.oop.project.model;

import java.util.Vector;

public class Plant implements WorldElement{
    private final Vector2d position;
    public Plant(Vector2d position){
        this.position = position;
    }

    @Override
    public Vector2d getPosition(){
        return position;
    }

}
