package agh.oop.project.model.worlds;

import agh.oop.project.model.Vector2d;
import agh.oop.project.model.WorldElement;

public class Plant implements WorldElement {
    private final Vector2d position;
    public Plant(Vector2d position){
        this.position = position;
    }

    @Override
    public Vector2d getPosition(){
        return position;
    }

}
