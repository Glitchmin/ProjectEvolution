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
    protected static int width;
    protected static int height;

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


    public Object objectAt(Vector2d position) {
        return mapBoundary.objectAt(position);
    }

    
    public Object[][] copy() {

        Object[][] mapCopy = new Object[width + 1][height + 1];
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                mapCopy[x][y] = objectAt(new Vector2d(x, y));
            }
        }
        return mapCopy;
    }


    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(new Vector2d(0,0), new Vector2d(width,height));
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        mapBoundary.positionChanged(oldPosition, newPosition, object);
    }

    public boolean place(Animal animal){
        if (canMoveTo(animal.getPosition())) {
            mapBoundary.add(animal.getPosition(), animal);
            animal.addObserver(this);
            return true;
        }
        return false;
    }



}
