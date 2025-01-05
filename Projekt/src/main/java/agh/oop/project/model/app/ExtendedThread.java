package agh.oop.project.model.app;

import agh.oop.project.Simulation;

public class ExtendedThread extends Thread {

    private final Simulation simulation;

    public ExtendedThread(Simulation simulation) {
        super(simulation);
        this.simulation = simulation;
    }

    public ExtendedThread restart() {
        ExtendedThread newExtendedThread = new ExtendedThread(simulation);
        newExtendedThread.start();
        return newExtendedThread;
    }
}
