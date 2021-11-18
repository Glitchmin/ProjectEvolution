package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class SimulationEngine implements IEngine {
    MoveDirection[] directions;
    IWorldMap map;
    final List<Vector2d> animals_positions = new ArrayList<>();

    SimulationEngine(MoveDirection[] directions, IWorldMap map, Vector2d[] positions) {
        for (Vector2d position : positions) {
            map.place(new Animal(map, position));
            animals_positions.add(position);
        }
        this.map = map;
        this.directions = directions;
    }

    public void run() {
        int i = 0;
        out.println(directions.length);
        for (MoveDirection movdir : directions) {
            out.println(movdir);
            out.println(map);
            Animal animal = (Animal) map.objectAt(animals_positions.get(i % animals_positions.size()));
            animal.move(movdir);
            animals_positions.set(i % animals_positions.size(), animal.getPosition());
            i++;
        }
        out.println(map);
    }

}
