package agh.oop.project;

import agh.oop.project.model.Specifications;
import agh.oop.project.model.Vector2d;
import agh.oop.project.model.animals.Animal;
import agh.oop.project.model.animals.CrazyAnimal;
import agh.oop.project.model.animals.NormalAnimal;
import agh.oop.project.model.app.ExtendedThread;
import agh.oop.project.model.app.MapChangeListener;
import agh.oop.project.model.worlds.ForestedEquator;
import agh.oop.project.model.worlds.LiveGivingCorpse;
import agh.oop.project.model.worlds.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable {

    private final Specifications specifications;
    private final WorldMap worldMap;
    private final ExtendedThread thread;
    private final Object lock = new Object();
    private int day=0;

    public Simulation(Specifications specifications, MapChangeListener presenter){
        this.specifications = specifications;
        if (specifications.normalGrowth()) worldMap = new ForestedEquator(specifications);
        else worldMap = new LiveGivingCorpse(specifications);
        worldMap.setListener(presenter);
        initSimulation();
        this.thread = new ExtendedThread(this);
        thread.start();
    }

    public Specifications getSpecifications() {
        return specifications;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public ExtendedThread getThread() {
        return thread;
    }

    public int getDay() {
        return day;
    }

    public Object getLock() {
        return lock;
    }

    public void initSimulation() {

        for(int i = 0; i < specifications.startingAmountOfAnimals(); i++){
            worldMap.placeAnimal(
                specifications.normalGenome() ? new NormalAnimal(specifications)
                        : new CrazyAnimal(specifications));
        }

        worldMap.generatePlants(specifications.startingAmountOfPlants());

    }
    public void dayCycle() {
        /*
        1) usunięcie martwych zwierząt
        2) ruch zwierzaków
        3) jedzenie
        4) rozmnażanie się
        5) wzrost nowych roslin
        */
        worldMap.removeDeadAnimals(day);
        worldMap.moveAllAnimals();
        worldMap.eatingAndReproduction();
        worldMap.generatePlants(specifications.dailyPlantGrowth());
    }
    @Override
    public void run() {

        try {
            while(!Thread.currentThread().isInterrupted()) {
                synchronized (lock) {
                    while (!thread.isRunning()) {
                        lock.wait();
                    }}
                dayCycle();
                day++;
                Thread.sleep(500);
                if (worldMap.getLivingAnimalAmount() == 0) break;
            }
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
