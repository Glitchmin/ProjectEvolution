package agh.ics.oop;

import static java.lang.System.out;

public class Animal {
    private Vector2d position;
    private MapDirection direction;

    public Animal( ) {
        Vector2d position = new Vector2d(2, 2);
        MapDirection direction = MapDirection.NORTH;
    }


    public String toString() {
        return "(" + Integer.toString(this.position.x) + "," + Integer.toString(this.position.y) + ") - " + direction.toString();
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public Vector2d getPosition() {
        return new Vector2d(this.position.x, this.position.y);
    }


    public boolean isAt(Vector2d position) {
        return (this.position.x == position.x && this.position.y == position.y);
    }


    public void move_zwierzaka(String[] behaviour_str) {
        MoveDirection[] behaviour_mov = OptionsParser.parse(behaviour_str);
        for (MoveDirection beh : behaviour_mov) {
            if (beh != null) {
                this.move(beh);
            } else {
                break;
            }
        }
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
