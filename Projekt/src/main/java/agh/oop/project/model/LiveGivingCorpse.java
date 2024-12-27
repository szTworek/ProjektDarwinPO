package agh.oop.project.model;

import java.util.*;

public class LiveGivingCorpse  extends AbstractWorldMap implements WorldMap{
    private final Random random = new Random();
    private HashSet<Vector2d> lastDeadPositionsHash =new HashSet<>();
    private List<Vector2d> otherAvailablePositions =new ArrayList<>();

    public LiveGivingCorpse(int height, int width) {
        super(height, width);
    }

    @Override
    public void removeAnimals() {
        lastDeadPositionsHash.clear();
        for (Map.Entry<Vector2d, List<Animal>> entry : livingAnimals.entrySet()) {
            Vector2d position = entry.getKey();
            List<Animal> animals = entry.getValue();
//            boolean flag = false; // czy na danej pozycji zmarł
            for (Animal animal : animals) {
                if (animal.getEnergy==0) {
                    lastDeadPositionsHash.add(position);
                    animals.remove(animal);
                    if (animals.isEmpty()) {
                        livingAnimals.remove(position);
                    }
                    deadAnimals.putIfAbsent(position, new ArrayList<>());
                    deadAnimals.get(position).add(animal);
                    break;
                }
            }
        }
    }

    @Override
    public void generatePlants(int quantity) {
        otherAvailablePositions.clear();
        List<Vector2d> lastDeadPositionsList=new ArrayList<>(lastDeadPositionsHash);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2d position = new Vector2d(i, j);
                if (!plants.containsKey(position) && !lastDeadPositionsHash.contains(position)) {
                    otherAvailablePositions.add(position);
                } else if (!plants.containsKey(position)) {
                    lastDeadPositionsList.add(position);
                }
            }
        }
        for(int i = 0; i < quantity; i++){
            int isOnCorpse=random.nextInt(10); // 80% na wybór z równika / 20% na wybór z poza równika

            if ((isOnCorpse<8 && !lastDeadPositionsList.isEmpty()) || otherAvailablePositions.isEmpty()) {
                int index=random.nextInt(lastDeadPositionsList.size());
                Vector2d position=lastDeadPositionsList.get(index);
                lastDeadPositionsList.remove(index);
                plants.put(position, new Plant(position));
            }
            else{
                int index=random.nextInt(otherAvailablePositions.size());
                Vector2d position= otherAvailablePositions.get(index);
                otherAvailablePositions.remove(index);
                plants.put(position, new Plant(position));
            }
        }
    }
}

