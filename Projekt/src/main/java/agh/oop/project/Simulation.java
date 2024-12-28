package agh.oop.project;

import agh.oop.project.model.Specifications;
import agh.oop.project.model.worlds.ForestedEquator;
import agh.oop.project.model.worlds.LiveGivingCorpse;
import agh.oop.project.model.worlds.WorldMap;

public class Simulation {
    /*
    1) usunięcie martwych zwierząt
    2) ruch zwierzaków
    3) jedzenie
    4) rozmnażanie się
    5) wzrost nowych roslin
    */
    private final Specifications specifications;
    private final WorldMap worldMap;

    public Simulation(Specifications specifications) {
        this.specifications = specifications;
        if (specifications.normalGrowth()) worldMap = new ForestedEquator(specifications);
        else worldMap = new LiveGivingCorpse(specifications);
    }

//    todo - postaram się jutro
//    public void initSimulation() {}
//    public void dayCycle() {}
//    public void run() {}

}
