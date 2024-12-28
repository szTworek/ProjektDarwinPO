package agh.oop.project.model.worlds;

import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.animals.Animal;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    private final Random random = new Random();
    protected Map<Vector2d, List<Animal>> livingAnimals = new HashMap<>();
    protected Map<Vector2d, List<Animal>> deadAnimals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();
    protected Vector2d lowerLeft = new Vector2d(0,0);
    protected Vector2d upperRight ;
    protected Specifications specifications;

    public AbstractWorldMap(Specifications specifications) {
        this.specifications = specifications;
        upperRight = new Vector2d(specifications.width(),specifications.height());
    }

    @Override
    public void placeAnimal(Animal animal) {
        livingAnimals.putIfAbsent(animal.getPosition(), new ArrayList<>());
        livingAnimals.get(animal.getPosition()).add(animal);
    }

    @Override
    public void removeAnimals() {
        for (Map.Entry<Vector2d, List<Animal>> entry : livingAnimals.entrySet()) {
            Vector2d position = entry.getKey();
            List<Animal> animals = entry.getValue();
            for (Animal animal : animals) {
                if (animal.isDead()) {
                    animals.remove(animal);
                    if (animals.isEmpty()) {
                        livingAnimals.remove(position);
                    }
                    deadAnimals.putIfAbsent(position, new ArrayList<>());
                    deadAnimals.get(position).add(animal);
                }
            }
        }
    }

    @Override
    public boolean isPlantAt(Vector2d position) {
        return plants.containsKey(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.getY()>=0 && position.getY()<specifications.height();
    }

    @Override
    public void manageReproduction(Vector2d position, List<Animal> animals) {
        for (int i=1; i<animals.size(); i+=2) {
            Animal animal1 = animals.get(i-1);
            Animal animal2 = animals.get(i);
            if (animal2.isHealthy(specifications)) break; // wystarczy że sprawdzimy to dla animal2 bo ma zawsze mniej lub tyle samo energii co animal1
            animal1.reproduce(animal2, this, specifications);
        }
    }

    private void sortAnimals(List<Animal> animals) {
        animals.sort(Comparator.comparingInt(Animal::getEnergy).reversed());
        for (int i=0; i<animals.size(); i++) {
            if (animals.get(i).getEnergy()==animals.get(i+1).getEnergy() && ( // jak energia ta sama
                    animals.get(i).getAge()<animals.get(i+1).getAge() || ( // i jest młodszy
                    animals.get(i).getAge()==animals.get(i+1).getAge() && ( // lub jak jest w takim samym wieku
                    animals.get(i).getChildAmount()<animals.get(i+1).getChildAmount() || ( // i ma więcej dzieci
                    animals.get(i).getChildAmount()==animals.get(i+1).getChildAmount() && random.nextBoolean()) //lub jak tyle samo to losowo
                    ))
            )) {
                Animal switchedAnimal = animals.get(i);
                animals.set(i, animals.get(i+1));
                animals.set(i+1, switchedAnimal);
                i-=2; // technicznie może się w nieskończonosć swapować jak rand będzie unlucky ale bez przesady xd
            }
        }
    }

    @Override
    public void handleAction(){
        // trzeba usuwać listy z animalami z pozycji jezeli nic na nich nie stoi, w przeciwnym
        // wypadku nie oplaca sie uzywac hashmapy
        for (Vector2d position : livingAnimals.keySet()) {
            List<Animal> animals = livingAnimals.get(position);
            sortAnimals(animals);
            if (isPlantAt(position)) animals.getFirst().eat(specifications);
            manageReproduction(position, animals);
        }
    }

    public void generatePlants(int quantity, List<Vector2d> betterArea, List<Vector2d> otherArea) {

        for(int i = 0; i < quantity; i++){
            int isBetter = random.nextInt(5); // 80% na wybór z równika / 20% na wybór z poza równika

            if (betterArea.isEmpty() && otherArea.isEmpty()) {
                break;
            }

            if ((isBetter == 0 || betterArea.isEmpty()) && !otherArea.isEmpty()) {
                int index = random.nextInt(otherArea.size());
                Vector2d position = otherArea.get(index);
                otherArea.remove(index);
                plants.put(position, new Plant(position));
            }
            else {
                int index = random.nextInt(betterArea.size());
                Vector2d position= betterArea.get(index);
                betterArea.remove(index);
                plants.put(position, new Plant(position));
            }
        }
    }
}
