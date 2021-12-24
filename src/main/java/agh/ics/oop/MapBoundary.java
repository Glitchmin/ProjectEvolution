package agh.ics.oop;

import java.util.*;

import static java.lang.System.out;

public class MapBoundary implements IPositionChangeObserver {

    Comparator<Vector2d> vector2dComparator = new Comparator<Vector2d>() {
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


    SortedMap<Vector2d, List <IMapElement> > objectPositions = new TreeMap<>(vector2dComparator);

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        remove(oldPosition, object);
        add(newPosition, object);
    }

    public List<IMapElement> objectAt(Vector2d position){
        return objectPositions.get(position);
    }




    public void add(Vector2d position, IMapElement object) {
        if (objectPositions.get(position) == null){
            objectPositions.put(position, new ArrayList<>());
        }
        objectPositions.get(position).add(object);
    }

    public void remove(Vector2d position, IMapElement object) {
        objectPositions.get(position).remove(object);
        if (objectPositions.get(position).isEmpty()){
            objectPositions.remove(position);
        }
    }

}
