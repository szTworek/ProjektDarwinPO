package agh.oop.project.model;

import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

public class Vector2d {

    // skopiowałem z labów vector2d
    // będzie potrzebny do ustalania pozycji zwierząt i roslin
    // na razie zostawmy te metody co tu są i jak się okażą później niepotrzebne to usuniemy

    private final int x;
    private final int y;

    public Vector2d(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public boolean precedes(Vector2d other) {
        return (x<=other.x && y<=other.y);
    }
    public boolean follows(Vector2d other) {
        return (x>=other.x && y>=other.y);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }
    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        int x_new = Math.max(x, other.x);
        int y_new = Math.max(y, other.y);
        return new Vector2d(x_new, y_new);
    }
    public Vector2d lowerLeft(Vector2d other) {
        int x_new = Math.min(x, other.x);
        int y_new = Math.min(y, other.y);
        return new Vector2d(x_new, y_new);
    }
    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Vector2d that)) return false;
        return x==that.x && y==that.y;
    }
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public boolean isNear(Vector2d other, int radius) {
        return abs(this.x-other.x)+abs(this.y-other.y) <= radius;
    }
    public boolean isNear(List<Vector2d> others, int radius) {
        for(Vector2d other: others) {
            if(isNear(other, radius)) return true;
        }
        return false;
    }
}
