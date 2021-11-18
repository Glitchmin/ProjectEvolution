package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class RectangularMap implements IWorldMap {
    final int width;
    final int height;
    final List<Animal> animals = new ArrayList<>(); //We're just a bunch of f*cking animals / But we're afraid of the outcome / Don't cry to me because the fiction that we're living in / Says I should pull the pin

    RectangularMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean canMoveTo(Vector2d position) {
        return (!isOccupied(position) && position.x <= width && position.x >= 0 && position.y >= 0 && position.y <= height);
    }

    ;

    public boolean isOccupied(Vector2d position) {
        for (Animal animal : animals) {
            if (animal.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    ;

    public boolean place(Animal animal) {
        if (canMoveTo(animal.getPosition())) {
            animals.add(animal);
            return true;
        }
        return false;
    }

    ;

    public Object objectAt(Vector2d position) {
        for (Animal animal : animals) {
            if (animal.getPosition().equals(position)) {
                return animal;
            }
        }
        return null;
    }

    ;

    public String toString() {
        MapVisualizer mapvis = new MapVisualizer(this);
        return mapvis.draw(new Vector2d(0, 0), new Vector2d(width, height));
    }
}
