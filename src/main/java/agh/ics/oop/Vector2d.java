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
        return "(" + this.x + "," + y + ")";
    }

    public boolean precedes(Vector2d other) {
        return (this.x <= other.x && this.y <= other.y);
    }

    public boolean follows(Vector2d other) {
        return (this.x >= other.x && this.y >= other.y);
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
        if (!(other instanceof Vector2d that))
            return false;
        return (that.x == this.x && that.y == this.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    public static Comparator<Vector2d> xFirstComparator = (a, b) -> {
        if (a.x == b.x && a.y == b.y) {
            return 0;
        }
        if (a.x == b.x) {
            if (a.y > b.y) {
                return 1;
            } else {
                return -1;
            }
        }
        if (a.x > b.x) {
            return 1;
        } else {
            return -1;
        }
    };
}
