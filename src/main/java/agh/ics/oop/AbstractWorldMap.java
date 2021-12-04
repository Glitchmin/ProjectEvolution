package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    final Map<Vector2d, IMapElement> objects_pos = new HashMap<>();

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public Object objectAt(Vector2d position) {
        return objects_pos.get(position);
    }

    protected Vector2d[] wymiary() {
        Vector2d lowerleft = new Vector2d(0, 0);
        Vector2d upperright = new Vector2d(1, 1);

        for (Vector2d object_pos : objects_pos.keySet()) {
            lowerleft = lowerleft.lowerLeft(object_pos);
            upperright = upperright.upperRight(object_pos);
        }


        Vector2d[] tab = new Vector2d[2];
        tab[0] = lowerleft;
        tab[1] = upperright;
        return tab;
    }


    public String toString() {
        MapVisualizer mapvis = new MapVisualizer(this);
        Vector2d[] tab = wymiary();
        return mapvis.draw(tab[0], tab[1]);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        objects_pos.put(newPosition, (IMapElement) objectAt(oldPosition));
        objects_pos.remove(oldPosition);
    }


}
