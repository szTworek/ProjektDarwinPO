package agh.oop.project.model;

public record Specifications(
        int height,
        int width,
        int startingAmountOfPlants,
        int amountOfEnergyPerPlant,
        int dailyPlantGrowth,
        boolean normalGrowth, // true - zalesione równiki, false - życiodajne truchła
        int startingAmountOfAnimals,
        int startingEnergyForAnimals,
        int healthyLimit,
        int energyUsageForReproduction,
        int minimalAmountOfMutations,
        int maximalAmountOfMutations,
        int genomeLength,
        boolean normalGenome // false - nieco szaleństwa, true - pełna predestynacja
) {
}