package de.htw.ai.decentralised_calendar.engine;

public class EngineThread implements Runnable {

    private final Engine engine;


    public EngineThread(Engine engine) {
        this.engine = engine;
    }


    @Override
    public void run() {
        this.engine.startSynchronization();
    }
}
