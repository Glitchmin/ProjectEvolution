package agh.ics.oop;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

abstract public class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    final MapBoundary mapBoundary = new MapBoundary();
    private static double jungleRatio;
    private static int width;
    private static int height;

    public static double getJungleRatio() {
        return jungleRatio;
    }

    public static void setJungleRatio(double jungleRatio) {
        AbstractWorldMap.jungleRatio = jungleRatio;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        AbstractWorldMap.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        AbstractWorldMap.height = height;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public void clearAnimals() {
        mapBoundary.clearAnimals();
    }

    public Object objectAt(Vector2d position) {
        return mapBoundary.objectAt(position);
    }

    public Vector2d[] wymiary() {
        Vector2d[] tab = new Vector2d[2];
        tab[0] = mapBoundary.getLower_left();
        tab[1] = mapBoundary.getUpper_right();
        return tab;
    }

    public Object[][] copy() {
        Vector2d lowerLeft = wymiary()[0];
        Vector2d upperRight = wymiary()[1];
        Object[][] mapCopy = new Object[upperRight.getX() - lowerLeft.getX() + 1][upperRight.getY() - lowerLeft.getY() + 1];
        for (int x = lowerLeft.getX(); x <= upperRight.getX(); x++) {
            for (int y = lowerLeft.getY(); y <= upperRight.getY(); y++) {
                mapCopy[x - lowerLeft.getX()][y - lowerLeft.getY()] = objectAt(new Vector2d(x, y));
            }
        }
        return mapCopy;
    }


    public String toString() {
        MapVisualizer mapvis = new MapVisualizer(this);
        Vector2d[] tab = wymiary();
        return mapvis.draw(tab[0], tab[1]);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        mapBoundary.positionChanged(oldPosition, newPosition, object);
    }


}
