package agh.ics.oop;

import javafx.util.Pair;

import java.util.*;

abstract public class AbstractWorldMap {

    public final MapObjectsHandler mapObjectsHandler = new MapObjectsHandler();
    private static double jungleRatio;
    protected static int width;
    protected static int height;
    private static Vector2d jungleSize;
    private static Vector2d jungleLowerLeft;

    public static void calculateJungleSize() {
        jungleSize = new Vector2d((int) (width * jungleRatio), (int) (height * jungleRatio));
        jungleLowerLeft = new Vector2d(width / 2 - jungleSize.x / 2, height / 2 - jungleSize.y / 2);
    }

    public static boolean isInsideTheJungle(Vector2d position) {
        return position.follows(jungleLowerLeft) && position.precedes(jungleLowerLeft.add(jungleSize));
    }

    public static Vector2d getJungleSize() {
        return jungleSize;
    }

    public static Vector2d getJungleLowerLeft() {
        return jungleLowerLeft;
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

    public List<IMapElement> objectsAt(Vector2d position) {
        return mapObjectsHandler.objectAt(position);
    }

    public List<IMapElement> getCopyOfMapElements() {
        return mapObjectsHandler.getMapElementsList();
    }


    public void place(Animal animal) {
        animal.addObserver(mapObjectsHandler);
        mapObjectsHandler.addAnimal(animal);
    }

    public Pair<Vector2d, Vector2d> addGrasses() {
        Vector2d junglePosition = null;
        Vector2d noJunglePosition = null;
        if (mapObjectsHandler.isThereAFreeJunglePosition()) {
            junglePosition = mapObjectsHandler.getARandomFreeJunglePosition();
            mapObjectsHandler.addGrass(junglePosition);
        }
        if (mapObjectsHandler.isThereAFreeNoJunglePosition()) {
            noJunglePosition = mapObjectsHandler.getARandomFreeNoJunglePosition();
            mapObjectsHandler.addGrass(noJunglePosition);
        }
        return new Pair<>(junglePosition, noJunglePosition);
    }

    public abstract Vector2d positionAfterMove(Vector2d oldPosition, Vector2d moveVector);

    public Double getAliveAnimalsCounter() {
        return (double) mapObjectsHandler.getAliveAnimals().size();
    }

    public Double getGrassCounter() {
        return (double) mapObjectsHandler.getGrassPositionsList().size();
    }


}
