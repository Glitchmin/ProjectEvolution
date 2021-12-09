package agh.ics.oop;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.lang.System.out;

public class MapBoundary implements IPositionChangeObserver {


    Comparator<Vector2d> v2d_comp_y = new Comparator<Vector2d>() {
        @Override
        public int compare(Vector2d a, Vector2d b) {
            if (a.x== b.x && a.y == b.y){
                return 0;
            }
            if (a.y == b.y) {
                if (a.x > b.x) {
                    return 1;
                } else {
                    return -1;
                }
            }
            if (a.y > b.y) {
                return 1;
            } else {
                return -1;
            }
        }
    };

    Comparator<Vector2d> v2d_comp_x = new Comparator<Vector2d>() {
        @Override
        public int compare(Vector2d a, Vector2d b) {
            if (a.x== b.x && a.y == b.y){
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
        }
    };


    SortedMap<Vector2d, IMapElement> object_positions_x_first = new TreeMap<Vector2d, IMapElement>(v2d_comp_x);
    SortedMap<Vector2d, IMapElement> object_positions_y_first = new TreeMap<Vector2d, IMapElement>(v2d_comp_y);

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        object_positions_x_first.remove(oldPosition);
        object_positions_y_first.remove(oldPosition);
        object_positions_x_first.put(newPosition, object);
        object_positions_y_first.put(newPosition, object);
    }

    public Object objectAt(Vector2d position){
        return object_positions_x_first.get(position);
    }


    public void add(Vector2d position, IMapElement object) {

        object_positions_x_first.put(position, object);
        object_positions_y_first.put(position, object);
    }

    public void remove(Vector2d position) {
        object_positions_x_first.remove(position);
        object_positions_y_first.remove(position);
    }


    public Vector2d getLower_left() {
        return new Vector2d(object_positions_x_first.firstKey().x, object_positions_y_first.firstKey().y) ;
    }

    public Vector2d getUpper_right() {
        return new Vector2d(object_positions_x_first.lastKey().x, object_positions_y_first.lastKey().y) ;
    }
}
