package agh.oop.project.model;

public enum MapDirection {

    // animal będzie obrócony w którąs stronę
    // ruch będzie wyglądał tak, że najpierw animal się obróci o 0-7 w prawo (jakas funkcja turn w animal)
    // a później ruszy o 1 w przód (i po to będzie nam toUnitVector)

    NORTH,
    NE,
    EAST,
    SE,
    SOUTH,
    SW,
    WEST,
    NW;

    public String toString(){
        return switch(this){
            case NORTH -> "North";
            case SOUTH -> "South";
            case WEST -> "West";
            case EAST -> "East";
            case NW -> "North-West";
            case NE -> "North-East";
            case SW -> "South-West";
            case SE -> "South-East";
        };
    }

    public Vector2d toUnitVector(){
        return switch (this){
            case NORTH -> new Vector2d(0,1);
            case SOUTH -> new Vector2d(0,-1);
            case WEST -> new Vector2d(-1,0);
            case EAST -> new Vector2d(1,0);
            case NW -> new Vector2d(-1,1);
            case NE -> new Vector2d(1,1);
            case SW -> new Vector2d(-1,-1);
            case SE -> new Vector2d(1,-1);
        };
    }
}
