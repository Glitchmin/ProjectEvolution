package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class SimulationEngine implements IEngine, Runnable {
    IWorldMap map;
    final List<Vector2d> animals_positions = new ArrayList<>();
    final List<IPositionChangeObserver> observers = new ArrayList<>();
    Integer moveDelayMs = 300;

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    public SimulationEngine(IWorldMap map, Vector2d[] positions) {
        for (Vector2d position : positions) {
            Animal animal = new Animal(map, position);
            map.place(animal);
            animals_positions.add(position);
        }
        this.map = map;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition, object);
        }
    }



    public void run() {
        while (true){
            out.println(map);

            try {
                Thread.sleep(moveDelayMs);
            } catch (InterruptedException e) {
                out.println("Interrupted Threat Simulation Engine");
                e.printStackTrace();
            }
        }
    }

}
