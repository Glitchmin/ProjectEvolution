package agh.ics.oop;

import static java.lang.System.out;

public class Animal {
    private Vector2d position;
    private MapDirection direction;

    public Animal() {
        Vector2d position = new Vector2d(2, 2);
        MapDirection direction = MapDirection.NORTH;
    }


    public String toString() {
        return "(" + Integer.toString(this.position.x) + "," + Integer.toString(this.position.y) + ") - " + direction.toString();
    }

    public boolean isAt(Vector2d position) {
        return (this.position.x == position.x && this.position.y == position.y);
    }

    public void move(MoveDirection direction) {
        Vector2d przem = new Vector2d(0, 0);
        switch (direction) {
            case RIGHT -> this.direction = this.direction.next();
            case LEFT -> this.direction = this.direction.previous();
            case FORWARD -> przem = przem.add(this.direction.toUnitVector());
            case BACKWARD -> przem = przem.subtract(this.direction.toUnitVector());
        }
        ;
        if (this.position.add(przem).follows(new Vector2d(0, 0)) && this.position.add(przem).precedes(new Vector2d(4, 4))) {
            this.position = this.position.add(przem);
        }
    }
}
