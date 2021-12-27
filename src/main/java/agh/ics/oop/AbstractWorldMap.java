package agh.ics.oop;

import java.util.*;

abstract public class AbstractWorldMap implements IPositionChangeObserver {

    final public MapObjectsHandler mapObjectsHandler = new MapObjectsHandler();
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

    public boolean isOccupied(Vector2d position) {
        return mapObjectsHandler.isOccupied(position);
    }


    public List<IMapElement> objectsAt(Vector2d position) {
        return mapObjectsHandler.objectAt(position);
    }

    public List<IMapElement> getCopyOfMapElements() {
        return mapObjectsHandler.getMapElementsList();
    }

    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(new Vector2d(0, 0), new Vector2d(width, height));
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        mapObjectsHandler.positionChanged(oldPosition, newPosition, object);
    }

    public void place(Animal animal) {
        mapObjectsHandler.addAnimal(animal);
        animal.addObserver(this);
    }

    public List<Animal> getAliveAnimals() {
        return mapObjectsHandler.getAliveAnimals();
    }

    public void removeAnimal(Animal animal) {
        mapObjectsHandler.removeAnimal(animal);
    }

    public SortedMap<Vector2d, List<IMapElement>> getObjectPositions() {
        return mapObjectsHandler.getObjectPositions();
    }

    public void addGrasses() {
        if (mapObjectsHandler.isThereAFreeJunglePosition()){
            mapObjectsHandler.addGrass(mapObjectsHandler.getARandomFreeJunglePosition());
        }
        if (mapObjectsHandler.isThereAFreeNoJunglePosition()){
            mapObjectsHandler.addGrass(mapObjectsHandler.getARandomFreeNoJunglePosition());
        }
    }

    public void removeGrass(Vector2d position) {
        mapObjectsHandler.removeGrass(position);
    }

    public abstract Vector2d positionAfterMove(Vector2d oldPosition, Vector2d moveVector);

    public int getAliveAnimalsCounter() {
        return mapObjectsHandler.getAliveAnimals().size();
    }

    public int getGrassCounter() {
        return mapObjectsHandler.getGrassPositionsList().size();
    }


}
