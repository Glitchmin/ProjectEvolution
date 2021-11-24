package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractWorldMap implements IWorldMap {

    final List<IMapElement> objects = new ArrayList<>();

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public Object objectAt(Vector2d position) {
        for (IMapElement obiekt : objects) {
            if (obiekt.getPosition().equals(position)) {
                return obiekt;
            }
        }
        return null;
    }

    private Vector2d[] wymiary() {
        Vector2d lowerleft=new Vector2d(0,0);
        Vector2d upperright=new Vector2d(1,1);

        if (!objects.isEmpty()){
            lowerleft = objects.get(0).getPosition();
            upperright = objects.get(0).getPosition();
        }

        for (IMapElement grass : objects){
            lowerleft = lowerleft.lowerLeft(grass.getPosition());
            upperright = upperright.upperRight(grass.getPosition());
        }
        Vector2d [] tab = new Vector2d[2];
        tab[0]=lowerleft;
        tab[1]=upperright;
        return tab;
    }


    public String toString() {
        MapVisualizer mapvis = new MapVisualizer(this);
        Vector2d [] tab = wymiary();
        return mapvis.draw(tab[0], tab[1]);
    }




}
