package agh.oop.project.model.worlds;

import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.animals.Animal;

import java.util.*;

public class LiveGivingCorpse  extends AbstractWorldMap implements WorldMap {

    private final List<Vector2d> lastDeadPositions = new LinkedList<>();

    public LiveGivingCorpse(Specifications specifications) {
        super(specifications);
    }

    @Override
    public void removeDeadAnimals(int day) {
        lastDeadPositions.clear();
        Map<Vector2d, List<Animal>> copyMap = new HashMap<>(livingAnimals);

        for (Map.Entry<Vector2d, List<Animal>> entry : copyMap.entrySet()) {
            Vector2d position = entry.getKey();
            List<Animal> animals = entry.getValue();
            List<Animal> copyAnimals = new ArrayList<>(animals);
            for (Animal animal : copyAnimals) {
                if (animal.isDead()) {
                    animal.setDeathDay(day);
                    lastDeadPositions.add(position);
                    animals.remove(animal);
                    statsUpdateWhenAnimalDied(animal);

                    if (animals.isEmpty()) livingAnimals.remove(position);
                    // tu był break ale nie może być bo by się mogły nie usunąć wszystkie martwe zwierzaki
                }
            }
        }
        mapChanges(day);
    }

    @Override
    public void generateBetterArea(){
        // radius jest zależny od ilosci martwych zwierzaków, aby zawsze było około 20% lepszych pól
        int radius = 1;
        int expectedBetterArea = specifications.height()*specifications.width()/5;
        while((1 + (radius + 1) * radius * 2) * lastDeadPositions.size() < expectedBetterArea && !lastDeadPositions.isEmpty()) {
            radius++;// radius zaokrąglamy w górę bo może nachodzić na siebie i imo tak wyjdzie bliżej srednio 20%
        }

        betterArea.clear();
        worseArea.clear();

        for (int i = 0; i < specifications.width(); i++) {
            for (int j = 0; j < specifications.height(); j++) {
                Vector2d position = new Vector2d(i, j);
                // radius to jak blisko miejsca zgonu musi być pole aby być lepsze
                if (position.isNear(lastDeadPositions, radius, specifications.width()))
                    betterArea.add(position);
                else
                    worseArea.add(position);
            }
        }
    }
}

