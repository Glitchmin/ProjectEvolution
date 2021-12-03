package agh.ics.oop;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

public class MapBoundary implements IPositionChangeObserver {
    Comparator<Vector2d> v2d_comp = new Comparator<Vector2d>() {
        @Override
        public int compare(Vector2d a, Vector2d b) {
            if (a.y == b.y) {
                if (a.x > b.x) {
                    return 1;
                } else {
                    return 0;
                }
            }
            if (a.y > b.y) {
                return 1;
            } else {
                return -1;
            }
        }
    };
    SortedMap<Vector2d, IMapElement> object_positions_x_first;
    SortedMap<Vector2d, IMapElement> object_positions_y_first = new TreeMap<Vector2d, IMapElement> (v2d_comp) ;

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {

    }


}
