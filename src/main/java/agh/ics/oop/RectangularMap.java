package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class RectangularMap extends AbstractWorldMap {
    final int width;
    final int height;


    RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean canMoveTo(Vector2d position) {
        return (!isOccupied(position) && position.x <= width && position.x >= 0 && position.y >= 0 && position.y <= height);
    }

    ;


    ;

    public boolean place(Animal animal) {
        if (canMoveTo(animal.getPosition())) {
            objects.add(animal);
            return true;
        }
        return false;
    }

    ;

    public Object objectAt(Vector2d position) {
        for (IMapElement animal : objects) {
            if (animal.getPosition().equals(position)) {
                return animal;
            }
        }
        return null;
    }
}
