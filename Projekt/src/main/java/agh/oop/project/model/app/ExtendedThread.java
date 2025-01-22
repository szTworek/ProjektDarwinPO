package agh.oop.project.model.app;

import agh.oop.project.Simulation;

public class ExtendedThread extends Thread {

    private final Simulation simulation;
    private volatile boolean running = true;

    public ExtendedThread(Simulation simulation) {
        super(simulation);
        this.simulation = simulation;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        Object lock=simulation.getLock();
        this.running = running;
        if (running){
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}
