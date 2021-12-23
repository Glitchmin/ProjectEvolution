package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class Grass implements IMapElement {
    private final Vector2d position;

    @Override
    public String getResourcePath() {
        return "src/main/resources/grass.png";
    }

    public Grass(Vector2d position) {
        this.position = position;
    }

    public boolean isAt(Vector2d position) {
        return getPosition().equals(position);
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString() {
        return "*";
    }

}
