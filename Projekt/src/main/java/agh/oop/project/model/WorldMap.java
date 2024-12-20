package agh.oop.project.model;

public interface WorldMap{

    //Place an animal on the map
    void placeAnimal(Animal animal);

    //Moves an animal (if it is present on the map) according to specified direction.
    //If the move is not possible, this method has no effect.
    void moveAnimal(Animal animal);

    //Removes an animal from the map upon its death
    void removeAnimal(Animal animal);

    //Returns a world element(plant or animal) at a given position
    WorldElement objectAt(Vector2d position);

    //Checks if an animal can move to a given position
    boolean canMoveTo(Animal animal, Vector2d position);
}
