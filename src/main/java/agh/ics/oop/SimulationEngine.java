package agh.ics.oop;

import agh.ics.oop.gui.App;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class SimulationEngine implements IEngine, Runnable {
    MoveDirection[] directions;
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
        int i = 0;
        out.println(directions.length);
        for (MoveDirection movdir : directions) {
            out.println(movdir);
            out.println(map);
            Vector2d oldposition = new Vector2d(animals_positions.get(i % animals_positions.size()).x,animals_positions.get(i % animals_positions.size()).y) ;
            out.println("test");
            Animal animal = (Animal) map.objectAt(animals_positions.get(i % animals_positions.size()));
            out.println("test2");
            animal.move(movdir);
            animals_positions.set(i % animals_positions.size(), animal.getPosition());
            positionChanged(oldposition,animals_positions.get(i % animals_positions.size()), animal );
            i++;
            try {
                Thread.sleep(moveDelayMs);
            } catch (InterruptedException e) {
                out.println("Interrupted Threat Simulation Engine");
                e.printStackTrace();
            }
        }
        out.println("koniec symulacji");
        out.println(map);
    }

    public void setDirections(MoveDirection[] directions) {
        this.directions = directions;
    }
}
