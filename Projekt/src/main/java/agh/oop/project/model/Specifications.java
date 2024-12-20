package agh.oop.project.model;

public interface Specifications {
    int getHeight();
    int getWidth();
    int getStartingAmountOfPlants();
    int getAmountOfEnergyPerPlant();
    int getDailyPlantGrowth();
    boolean isNormalGrowth(); // false - zalesione równiki, true - życiodajne truchła
    int getStartingAmountOfAnimals();
    int getStartingEnergyForAnimals();
    int getHealthyLimit();
    int getEnergyUsageForReproduction();
    int getMinimalAmountOfMutations();
    int getMaximalAmountOfMutations();
    int getGenomeLength();
    boolean isNormalGenome(); // false - nieco szaleństwa, true - pełna predestynacja

}
