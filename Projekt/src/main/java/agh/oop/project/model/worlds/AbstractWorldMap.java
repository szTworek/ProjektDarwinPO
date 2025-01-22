package agh.oop.project.model.worlds;

import agh.oop.project.model.CsvWriter;
import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.WorldElement;
import agh.oop.project.model.animals.Animal;
import agh.oop.project.model.app.MapChangeListener;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.round;

public abstract class AbstractWorldMap implements WorldMap {
    private final Random random = new Random();
    protected MapChangeListener listener;
    protected Map<Vector2d, List<Animal>> livingAnimals = new HashMap<>();
    protected Map<Vector2d, Plant> plants = new HashMap<>();
    protected Vector2d upperRight;
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
    protected Map<List<Integer>, Integer> genotypes = new HashMap<>();
    protected int sumOfLivingEnergy = 0;
    protected int sumOfDeadDays = 0; // średnia liczba to suma/deadAnimalAmount
    protected int sumOfKids = 0; // średnia liczba to suma/livingAnimalAmount

    public AbstractWorldMap(Specifications specifications) {
        this.specifications = specifications;
        upperRight = new Vector2d(specifications.width(), specifications.height());
    }

    @Override
    public void writeStatsToFile(CsvWriter writer, int day) throws IOException {
        writer.write("\"" + day + "\",\"" + livingAnimalAmount + "\",\"" + plants.size() + "\",\"" + getFreeAreas() + "\",\"" + getPopularGenotype(genotypes) + "\",\"" + (float) round((float) sumOfLivingEnergy / livingAnimalAmount * 100) / 100 + "\",\"" + (float) round((float) sumOfDeadDays / deadAnimalAmount * 100) / 100 + "\",\"" + (float) round((float) sumOfKids / livingAnimalAmount * 100) / 100 + "\"");
    }

    @Override
    public int getHeight() {
        return specifications.height();
    }

    @Override
    public int getWidth() {
        return specifications.width();
    }

    @Override
    public Map<Vector2d, List<Animal>> getLivingAnimals() {
        return livingAnimals;
    }

    @Override
    public Map<Vector2d, Plant> getPlants() {
        return plants;
    }

    @Override
    public int getDeadAnimalAmount() {
        return deadAnimalAmount;
    }

    @Override
    public int getFreeAreas() {
        HashSet<Vector2d> occupiedAreas = new HashSet<>(plants.keySet());

        occupiedAreas.addAll(livingAnimals.keySet());

        return specifications.width()*specifications.height() - occupiedAreas.size();
    }

    @Override
    public int getSumOfLivingEnergy() {
        return sumOfLivingEnergy;
    }

    @Override
    public int getSumOfDeadDays() {
        return sumOfDeadDays;
    }

    @Override
    public int getSumOfKids() {
        return sumOfKids;
    }

    @Override
    public int getLivingAnimalAmount() {
        return livingAnimalAmount;
    }

    @Override
    public HashSet<Vector2d> getBetterArea() {
        return betterArea;
    }

    @Override
    public List<Integer> getTheMostPopularGenotype() {
        if (genotypes.isEmpty())
            return null;

        return genotypes.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public Animal getAnimalWithTheMostPopularGenotype(Vector2d position) {
        List<Animal> animals = livingAnimals.get(position);
        if (animals!=null){
            sortAnimals(animals);
            List<Integer> genotype= getTheMostPopularGenotype();
            for (Animal animal : animals) {
                if (animal.getGenome().equals(genotype)){
                    return animal;
                }
        }}
        return null;
    }

    public void statsUpdateWhenAnimalDied(Animal animal){
        livingAnimalAmount--;
        deadAnimalAmount++;
        sumOfDeadDays += animal.getAge();
        sumOfKids -= animal.getChildAmount();
        sumOfLivingEnergy -= animal.getEnergy();
    }

    public void statsUpdateWhenAnimalPlaced(Animal animal) {
        livingAnimalAmount++;
        genotypes.putIfAbsent(animal.getGenome(), 0);
        genotypes.replace(animal.getGenome(), genotypes.get(animal.getGenome()) + 1);
        sumOfLivingEnergy += animal.getEnergy();
    }

    private void statsUpdateWhenSex() {
        sumOfLivingEnergy -= 2 * specifications.energyUsageForReproduction();
        sumOfKids += 2;
    }

    @Override
    public void placeAnimal(Animal animal) {
        livingAnimals.putIfAbsent(animal.getPosition(), new LinkedList<>());
        livingAnimals.get(animal.getPosition()).add(animal);

        statsUpdateWhenAnimalPlaced(animal);
    }

    @Override
    public void removeDeadAnimals(int day) {
        for (Map.Entry<Vector2d, List<Animal>> entry : new ArrayList<>(livingAnimals.entrySet())) {
            Vector2d position = entry.getKey();
            List<Animal> animals = entry.getValue();

            animals.removeIf(animal -> {
                if (animal.isDead()) {
                    animal.setDeathDay(day);
                    statsUpdateWhenAnimalDied(animal);
                    return true;
                }
                return false;
            });
            if (animals.isEmpty()) {
                livingAnimals.remove(position);
            }
        }
        mapChanges(day);
    }

    @Override
    public boolean isPlantAt(Vector2d position) {
        return plants.containsKey(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.getY() >= 0 && position.getY() < specifications.height();
    }

    @Override
    public void manageReproduction(List<Animal> animals) {
        int initialSize = animals.size(); // robimy kopię aby dopiero co urodzone dzieci nie mogły się rozmnażać
        for (int i = 1; i < initialSize; i += 2) {
            Animal animal1 = animals.get(i - 1);
            Animal animal2 = animals.get(i);
            if (!animal2.isHealthy(specifications))
                break; // wystarczy że sprawdzimy to dla animal2 bo ma zawsze mniej lub tyle samo energii co animal1
            animal1.reproduce(animal2, this, specifications);
            statsUpdateWhenSex();
        }
    }

    @Override
    public Animal getTheBestAnimal(List<Animal> animals) {
        List<Animal> copy = new ArrayList<>(animals);
        sortAnimals(copy);

        if(copy.size()!=animals.size()) System.out.println("AAAA");

        return copy.getFirst();
    }

    @Override
    public void sortAnimals(List<Animal> animals) {
        animals.sort(Comparator.comparingInt(Animal::getEnergy).reversed());
        boolean flag = true;
        while (flag) {
            flag = false;

            for (int i = 0; i < animals.size() - 1; i++) {
                if (animals.get(i).getEnergy() == animals.get(i + 1).getEnergy()) {
                    if (animals.get(i).getAge() < animals.get(i + 1).getAge()) {
                        Animal switchedAnimal = animals.get(i);
                        animals.set(i, animals.get(i + 1));
                        animals.set(i + 1, switchedAnimal);
                        flag = true;
                    } else if (animals.get(i).getAge() == animals.get(i + 1).getAge()) {
                        if (animals.get(i).getChildAmount() < animals.get(i + 1).getChildAmount()) {
                            Animal switchedAnimal = animals.get(i);
                            animals.set(i, animals.get(i + 1));
                            animals.set(i + 1, switchedAnimal);
                            flag = true;
                        } else if (animals.get(i).getChildAmount() == animals.get(i + 1).getChildAmount() && random.nextBoolean()) {
                            Animal switchedAnimal = animals.get(i);
                            animals.set(i, animals.get(i + 1));
                            animals.set(i + 1, switchedAnimal);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void eatingAndReproduction() {
        // trzeba usuwać listy z animalami z pozycji jezeli nic na nich nie stoi, w przeciwnym
        // wypadku nie oplaca sie uzywac hashmapy
        for (Vector2d position : livingAnimals.keySet()) {
            List<Animal> animals = livingAnimals.get(position);
            sortAnimals(animals);

            if (isPlantAt(position)) {
                animals.getFirst().eat(specifications);
                sumOfLivingEnergy += specifications.amountOfEnergyPerPlant();
                plants.remove(position);
            }
            manageReproduction(animals);
        }
    }

    private HashSet<Vector2d> removePlantFieldsFromArea(HashSet<Vector2d> area) {
        HashSet<Vector2d> result = new HashSet<>(area);
        for (Vector2d position : plants.keySet()) result.remove(position);
        return result;
    }

    @Override
    public void placePlant(Vector2d position) {
        plants.putIfAbsent(position, new Plant(position));
    }

    @Override
    public void generatePlants(int quantity) {

        generateBetterArea();

        ArrayList<Vector2d> betterAreaList = new ArrayList<>(removePlantFieldsFromArea(betterArea));
        ArrayList<Vector2d> otherAreaList = new ArrayList<>(removePlantFieldsFromArea(worseArea));

        for (int i = 0; i < quantity; i++) {
            int isBetter = random.nextInt(5); // 80% na wybór z równika / 20% na wybór z poza równika
            int index;
            Vector2d position;

            if (betterAreaList.isEmpty() && otherAreaList.isEmpty()) break;

            if ((isBetter == 0 || betterAreaList.isEmpty()) && !otherAreaList.isEmpty()) {
                index = random.nextInt(otherAreaList.size());
                position = otherAreaList.get(index);
                otherAreaList.remove(index);
            } else {
                index = random.nextInt(betterAreaList.size());
                position = betterAreaList.get(index);
                betterAreaList.remove(index);
            }

            placePlant(position);
        }
    }

    @Override
    public void moveAllAnimals() {
        ArrayList<List<Animal>> positions = new ArrayList<>();
        for (List<Animal> animals: livingAnimals.values()) {
            positions.add(new ArrayList<>(animals));
        }
        for (List<Animal> animals : positions) {
            for (Animal animal : new ArrayList<>(animals)) {
                move(animal);
            }
        }
    }

    @Override
    public void move(Animal animal) {

        animal.decreaseEnergy(1);

        Vector2d oldPosition = animal.getPosition();
        List<Animal> animals = livingAnimals.get(animal.getPosition());
        animal.move(specifications.width(), this);

        if (oldPosition != animal.getPosition()) {
            animals.remove(animal);
            livingAnimals.putIfAbsent(animal.getPosition(), new LinkedList<>());
            livingAnimals.get(animal.getPosition()).add(animal);
            if (animals.isEmpty()) livingAnimals.remove(oldPosition);
        }

        sumOfLivingEnergy--;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (livingAnimals.containsKey(position)){
            List<Animal> animals = livingAnimals.get(position);
            return getTheBestAnimal(animals);
        } else if (plants.containsKey(position)) {
            return plants.get(position);
        }
        return null;
    }

    public List<Integer> getPopularGenotype(Map<List<Integer>, Integer> genotypes ){
        List<Integer> result = new ArrayList<>(genotypes.size());
        Integer maxi = 0;
        for(List<Integer> i : genotypes.keySet()){
            if (genotypes.get(i)>maxi){
                maxi = genotypes.get(i);
                result=i;
            }
        }
        return result;
    }

    public void setListener(MapChangeListener listener) {
        this.listener = listener;
    }
    public void mapChanges( int day) {
        if (listener != null)
            listener.mapChanges(this, day, livingAnimalAmount, plants.size(), getFreeAreas(), getPopularGenotype(genotypes), (float) sumOfLivingEnergy /livingAnimalAmount, (float) sumOfDeadDays /deadAnimalAmount, (float) sumOfKids /livingAnimalAmount );
    }
}
