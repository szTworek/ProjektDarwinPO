package agh.oop.project.model.worlds;

import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;

import java.util.*;

import static java.lang.Math.*;

public class ForestedEquator  extends AbstractWorldMap implements WorldMap {

    private final List<Vector2d> availablePositionsAtEquator = new LinkedList<>();
    private final List<Vector2d> availablePositionsNotAtEquator = new LinkedList<>();
    private int lowestEquatorPoint;
    private int highestEquatorPoint;


    public ForestedEquator(Specifications specifications) {
        super(specifications);
        equatorPoints();
    }

    public void equatorPoints() {
        int equatorHeight = max(round((float) specifications.height() /5), 1);
        int middlePoint = (int) (ceil((double) specifications.height() /2) - 1);

        highestEquatorPoint = (int) (ceil((double) ((equatorHeight - 1) /2)) + middlePoint);
        lowestEquatorPoint = (int) (middlePoint - floor((double) ((equatorHeight - 1) /2)));
    }

    @Override
    public void generatePlants(int quantity){

        for (int i = 0; i < specifications.width(); i++) {
            for (int j = 0; j < specifications.height(); j++){
                Vector2d vector = new Vector2d(i,j);
                if(!plants.containsKey(vector)) {
                    if (j>=lowestEquatorPoint && j<=highestEquatorPoint) availablePositionsAtEquator.add(vector);
                    else availablePositionsNotAtEquator.add(vector);
                }
            }
        }
        generatePlants(quantity, availablePositionsAtEquator, availablePositionsNotAtEquator);
    }
}

