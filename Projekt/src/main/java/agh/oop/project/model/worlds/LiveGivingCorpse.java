package agh.oop.project.model.worlds;

import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.animals.Animal;

import java.util.*;

public class LiveGivingCorpse  extends AbstractWorldMap implements WorldMap {

    private final List<Vector2d> lastDeadPositions = new LinkedList<>();
    private final List<Vector2d> otherAvailablePositions = new LinkedList<>();

    public LiveGivingCorpse(Specifications specifications) {
        super(specifications);
    }

    @Override
    public void removeAnimals() {
        lastDeadPositions.clear();
        for (Map.Entry<Vector2d, List<Animal>> entry : livingAnimals.entrySet()) {
            Vector2d position = entry.getKey();
            List<Animal> animals = entry.getValue();
            for (Animal animal : animals) {
                if (animal.getEnergy()==0) {
                    lastDeadPositions.add(position);
                    animals.remove(animal);
                    if (animals.isEmpty()) {
                        livingAnimals.remove(position);
                    }
                    deadAnimals.putIfAbsent(position, new ArrayList<>());
                    deadAnimals.get(position).add(animal);
                    // tu był break ale nie może być bo by się mogły nie usunąć wszystkie martwe zwierzaki
                }
            }
        }
    }

    @Override
    public void generatePlants(int quantity) {

        // radius jest zależny od ilosci martwych zwierzaków, aby zawsze było około 20% lepszych pól
        int radius = 1;
        int expectedBetterArea = specifications.height()*specifications.width()/5;
        while((1 + (radius + 1) * radius * 2) * lastDeadPositions.size() < expectedBetterArea && !lastDeadPositions.isEmpty()) {
            radius++;
        }
        // radius zaokrąglamy w górę bo może nachodzić na siebie
        // i imo tak wyjdzie bliżej srendio 20%

        otherAvailablePositions.clear();
        for (int i = 0; i < specifications.width(); i++) {
            for (int j = 0; j < specifications.height(); j++) {
                Vector2d position = new Vector2d(i, j);
                // radius to jak blisko miejsca zgonu musi być pole aby być lepsze
                if (!plants.containsKey(position) && position.isNear(lastDeadPositions, radius)) {
                    lastDeadPositions.add(position);
                } else if (!plants.containsKey(position)) {
                    otherAvailablePositions.add(position);
                }
            }
        }
        generatePlants(quantity, lastDeadPositions, otherAvailablePositions);
    }
}

