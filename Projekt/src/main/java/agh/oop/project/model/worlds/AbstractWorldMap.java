package agh.oop.project.model.worlds;

import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.WorldElement;
import agh.oop.project.model.animals.Animal;
import agh.oop.project.model.app.MapChangeListener;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {
    private final Random random = new Random();
    protected MapChangeListener listener;
    protected Map<Vector2d, List<Animal>> livingAnimals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();
    protected Vector2d upperRight ;
    protected Specifications specifications;
    protected final HashSet<Vector2d> betterArea = new HashSet<>();
    protected final HashSet<Vector2d> worseArea = new HashSet<>();
    /*
    STATYSTYKI:
         liczby wszystkich zwierzaków,
         liczby wszystkich roślin,
         liczby wolnych pól,
         najpopularniejszych genotypów,
         średniego poziomu energii dla żyjących zwierzaków,
         średniej długości życia zwierzaków dla martwych zwierzaków (wartość uwzględnia wszystkie nieżyjące zwierzaki - od początku symulacji),
         średniej liczby dzieci dla żyjących zwierzaków (wartość uwzględnia wszystkie powstałe zwierzaki, a nie tylko zwierzaki powstałe w danej epoce).
    */
    protected int livingAnimalAmount = 0;
    protected int deadAnimalAmount = 0;
    // plantAmount = plants.size() - bez sensu osobną pisać
    protected int freeAreas;
    protected Map<List<Integer>, Integer> genotypes = new HashMap<>();
    protected int sumOfLivingEnergy;
    protected int sumOfDeadDays = 0; // średnia liczba to suma/deadAnimalAmount
    protected int sumOfKids; // średnia liczba to suma/livingAnimalAmount

    public AbstractWorldMap(Specifications specifications) {
        this.specifications = specifications;
        upperRight = new Vector2d(specifications.width(),specifications.height());
        sumOfLivingEnergy = specifications.startingAmountOfAnimals() * specifications.startingEnergyForAnimals();
        sumOfKids = -2*specifications.startingAmountOfAnimals();
        freeAreas = specifications.width() * specifications.height();
    }

    @Override
    public int getHeight() {
        return specifications.height();
    }
    @Override
    public int getWidth() {
        return specifications.width();
    }

    public Map<Vector2d, List<Animal>> getLivingAnimals(){
        return livingAnimals;
    }
    public Map<Vector2d, Plant> getPlants(){
        return plants;
    }

    public void statsUpdateWhenAnimalDied(Animal animal){
        livingAnimalAmount--;
        deadAnimalAmount++;
        sumOfDeadDays += animal.getAge();
        sumOfKids -= animal.getChildAmount();

        if(livingAnimals.get(animal.getPosition()).isEmpty() && !plants.containsKey(animal.getPosition())) freeAreas++;
        mapChanges();
    }
    public void statsUpdateWhenAnimalPlaced(Animal animal){
        livingAnimalAmount++;
        genotypes.putIfAbsent(animal.getGenome(), 0);
        genotypes.replace(animal.getGenome(), genotypes.get(animal.getGenome()) + 1);
        sumOfKids += 2;

        if(livingAnimals.get(animal.getPosition()).size() == 1 && !plants.containsKey(animal.getPosition())) freeAreas--;
        mapChanges();
    }
    public void statsUpdateWhenPlantPlaced(Vector2d position) {
        if (!livingAnimals.containsKey(position) && !isPlantAt(position)) { // Replaced plants.containsKey(position)
            freeAreas--;
        }
    }
    private void statsUpdateWhenMoving(Vector2d oldPosition, Vector2d position) {
        sumOfLivingEnergy--;
        if (livingAnimals.get(position).size() == 1 && !isPlantAt(position)) { // Replaced plants.containsKey(position)
            freeAreas--;
        }
        if (!livingAnimals.containsKey(oldPosition) && !isPlantAt(oldPosition)) { // Replaced plants.containsKey(oldPosition)
            freeAreas++;
        }
    }

    public int getLivingAnimalAmount(){
        return livingAnimalAmount;
    }

    @Override
    public void placeAnimal(Animal animal) {
        livingAnimals.putIfAbsent(animal.getPosition(), new LinkedList<>());
        livingAnimals.get(animal.getPosition()).add(animal);

        statsUpdateWhenAnimalPlaced(animal);
    }

    @Override
    public void removeDeadAnimals() {
        for (Map.Entry<Vector2d, List<Animal>> entry : new ArrayList<>(livingAnimals.entrySet())) {
            Vector2d position = entry.getKey();
            List<Animal> animals = entry.getValue();

            animals.removeIf(animal -> {
                if (animal.isDead()) {
                    statsUpdateWhenAnimalDied(animal);
                    return true;
                }
                return false;
            });
            if (animals.isEmpty()) {
                livingAnimals.remove(position);
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
    public void manageReproduction(List<Animal> animals) {
        int initialSize = animals.size(); // robimy tak aby dopiero co urodzone dzieci nie mogły się rozmnażać
        for (int i=1; i<initialSize; i+=2) {
            Animal animal1 = animals.get(i-1);
            Animal animal2 = animals.get(i);
            if (!animal2.isHealthy(specifications)) break; // wystarczy że sprawdzimy to dla animal2 bo ma zawsze mniej lub tyle samo energii co animal1
            animal1.reproduce(animal2, this, specifications);
        }
    }

    @Override
    public void sortAnimals(List<Animal> animals) {
        animals.sort(Comparator.comparingInt(Animal::getEnergy).reversed());
        boolean flag = true;
        while(flag){
            flag = false;
            for (int i=0; i<animals.size()-1; i++) {
                if (animals.get(i).getEnergy()==animals.get(i+1).getEnergy() && ( // jak energia ta sama
                        animals.get(i).getAge()<animals.get(i+1).getAge() || ( // i jest młodszy
                        animals.get(i).getAge()==animals.get(i+1).getAge() && // lub jak jest w takim samym wieku
                        animals.get(i).getChildAmount()<animals.get(i+1).getChildAmount() // i ma więcej dzieci                        ))
                ))) {
                    Animal switchedAnimal = animals.get(i);
                    animals.set(i, animals.get(i+1));
                    animals.set(i+1, switchedAnimal);
                    flag = true;
                }else if(animals.get(i).getChildAmount()==animals.get(i+1).getChildAmount() && random.nextBoolean()){
                    // jak ma tyle samo dzieci to zmieniamy losowo ale nie dajemy flagi aby nie zapętliło się w nieskończonosć
                    Animal switchedAnimal = animals.get(i);
                    animals.set(i, animals.get(i+1));
                    animals.set(i+1, switchedAnimal);
                }
            }
        }
    }

    @Override
    public void eatingAndReproduction(){
        // trzeba usuwać listy z animalami z pozycji jezeli nic na nich nie stoi, w przeciwnym
        // wypadku nie oplaca sie uzywac hashmapy
        for (Vector2d position : livingAnimals.keySet()) {
            List<Animal> animals = livingAnimals.get(position);
            sortAnimals(animals);
            if (isPlantAt(position)) {
                animals.getFirst().eat(specifications);
                sumOfLivingEnergy += specifications.amountOfEnergyPerPlant();
            }
            manageReproduction(animals);
        }
        mapChanges();
    }

    private HashSet<Vector2d> removePlantFieldsFromArea(HashSet<Vector2d> area){
        HashSet<Vector2d> result = new HashSet<>(area);
        for(Vector2d position : plants.keySet()) result.remove(position);
        return result;
    }

    @Override
    public void generatePlants(int quantity) {

        getBetterArea();

        ArrayList<Vector2d> betterAreaList = new ArrayList<>(removePlantFieldsFromArea(betterArea));
        ArrayList<Vector2d> otherAreaList = new ArrayList<>(removePlantFieldsFromArea(worseArea));

        for(int i = 0; i < quantity; i++){
            int isBetter = random.nextInt(5); // 80% na wybór z równika / 20% na wybór z poza równika
            int index;
            Vector2d position;

            if (betterAreaList.isEmpty() && otherAreaList.isEmpty()) break;

            if ((isBetter == 0 || betterAreaList.isEmpty()) && !otherAreaList.isEmpty()) {
                index = random.nextInt(otherAreaList.size());
                position = otherAreaList.get(index);
                otherAreaList.remove(index);
                plants.put(position, new Plant(position));
            }
            else {
                index = random.nextInt(betterAreaList.size());
                position = betterAreaList.get(index);
                betterAreaList.remove(index);
                plants.put(position, new Plant(position));
            }

            statsUpdateWhenPlantPlaced(position);
        }
        mapChanges();
    }

    @Override
    public void moveAllAnimals(){
        for(List<Animal> animals: new ArrayList<>(livingAnimals.values())) {
            for (Animal animal : new ArrayList<>(animals)) {
                move(animal);
            }
        }
        mapChanges();
    }

    @Override
    public void move(Animal animal) {

        animal.decreaseEnergy(1);

        Vector2d oldPosition = animal.getPosition();
        List<Animal> animals = livingAnimals.get(animal.getPosition());
        animal.move(specifications.width(), this);

        if(oldPosition != animal.getPosition()) {
            animals.remove(animal);
            livingAnimals.putIfAbsent(animal.getPosition(), new LinkedList<>());
            livingAnimals.get(animal.getPosition()).add(animal);
            if (animals.isEmpty()) livingAnimals.remove(oldPosition);
        }

        statsUpdateWhenMoving(oldPosition, animal.getPosition());
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (livingAnimals.containsKey(position)){
            List<Animal> animals = livingAnimals.get(position);
            sortAnimals(animals);
            return animals.getFirst();
        } else if (plants.containsKey(position)) {
            return plants.get(position);
        }
        return null;
    }

    public void setListener(MapChangeListener listener) {
        this.listener = listener;
    }
    public void mapChanges() {
        listener.mapChanges(this, livingAnimalAmount, plants.size(), freeAreas, genotypes, (float) sumOfLivingEnergy /livingAnimalAmount, (float) sumOfDeadDays /deadAnimalAmount, (float) sumOfKids /livingAnimalAmount );
    }
}
