package agh.ics.oop;

import java.util.Comparator;
import java.util.Objects;

public class Vector2d {
    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    final public int x;
    final public int y;

    public String toString() {
        return "(" + Integer.toString(this.x) + "," + Integer.toString(y) + ")";
    }

    public boolean precedes(Vector2d other) {
        return (this.x <= other.x && this.y <= other.y);
    }

    public boolean follows(Vector2d other) {
        return (this.x >= other.x && this.y >= other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }


    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return (that.x == this.x && that.y == this.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Comparator<Vector2d> xFirstComparator = (a, b) -> {
        if (a.getX() == b.getX() && a.getY() == b.getY()) {
            return 0;
        }
        if (a.getX() == b.getX()) {
            if (a.getY() > b.getY()) {
                return 1;
            } else {
                return -1;
            }
        }
        if (a.getX() > b.getX()) {
            return 1;
        } else {
            return -1;
        }
    };
}
