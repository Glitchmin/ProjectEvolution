package agh.ics.oop;

public class Grass implements IMapElement{
    private final Vector2d position;

    public Grass(Vector2d position){
        this.position = position;
    }

    public boolean isAt(Vector2d position){
        return getPosition().equals(position);
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString(){
        return "*";
    }
}
