package agh.oop.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForestedEquator  extends AbstractWorldMap implements WorldMap{
    private final Random random = new Random();
    private int equatorHeight;
    private int beyondEquatorHeight;
    private final int underEquator;
    private final List<Vector2d> availablePositionsAtEquator= new ArrayList<>();
    private final List<Vector2d> availablePositionsAtBeyondEquator= new ArrayList<>();


    public ForestedEquator(int height, int width) {

        super(height, width);
        this.equatorHeight = (int) Math.floor(height * 0.4);
        this.beyondEquatorHeight = height - equatorHeight;
        setBeyondEquator();
        underEquator=beyondEquatorHeight/2;
        }


    public void setBeyondEquator() {
        if (beyondEquatorHeight%2==1 && beyondEquatorHeight!=1) {
            beyondEquatorHeight--;
            equatorHeight++;
        }
    }

    @Override
    public void generatePlants(int quantity){
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < equatorHeight; j++) {
                Vector2d on=new Vector2d(i,j+underEquator);
                if(!plants.containsKey(on)) {
                    availablePositionsAtEquator.add(on);
                }
            }
            for (int k = 0; k < underEquator; k++) {
                Vector2d under=new Vector2d(i,k);
                Vector2d over=new Vector2d(i,k+equatorHeight+underEquator);
                if (!plants.containsKey(under)) {
                    availablePositionsAtBeyondEquator.add(under);
                }
                if (!plants.containsKey(over)) {
                    availablePositionsAtBeyondEquator.add(over);
                }
            }
        }
        for(int i = 0; i < quantity; i++){
            int isOnEquator=random.nextInt(10); // 80% na wybór z miejsca gdzie niedawno zmarł zwierzak / 20% na wybór z innego miejsca na mapie

            if ((isOnEquator<8 && !availablePositionsAtEquator.isEmpty()) || availablePositionsAtBeyondEquator.isEmpty()) {
                int index=random.nextInt(availablePositionsAtEquator.size());
                Vector2d position=availablePositionsAtEquator.get(index);
                availablePositionsAtEquator.remove(index);
                plants.put(position, new Plant(position));
            }
            else{
                int index=random.nextInt(availablePositionsAtBeyondEquator.size());
                Vector2d position=availablePositionsAtBeyondEquator.get(index);
                availablePositionsAtBeyondEquator.remove(index);
                plants.put(position, new Plant(position));
            }
        }
    }
    }

