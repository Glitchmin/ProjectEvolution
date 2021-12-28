package agh.ics.oop;

public class Grass implements IMapElement {
    private final Vector2d position;

    @Override
    public String getResourcePath() {
        return "src/main/resources/grass.png";
    }

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString() {
        return "*";
    }

}
