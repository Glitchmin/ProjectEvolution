package agh.ics.oop;

import java.util.*;

abstract public class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {

    final MapObjectsHandler mapObjectsHandler = new MapObjectsHandler();
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
        return mapObjectsHandler.isOccupied(position);
    }


    public Object objectAt(Vector2d position) {
        return mapObjectsHandler.objectAt(position);
    }

    
    public List<IMapElement> getCopyOfMapElements() {
        return mapObjectsHandler.getMapElementsList();
    }


    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(new Vector2d(0,0), new Vector2d(width,height));
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        mapObjectsHandler.positionChanged(oldPosition, newPosition, object);
    }

    public void place(Animal animal){
            mapObjectsHandler.addAnimal(animal);
            animal.addObserver(this);
    }

    public List<Animal> getAliveAnimals() {
        return mapObjectsHandler.getAliveAnimals();
    }

    public void removeAnimal (Animal animal){
        mapObjectsHandler.removeAnimal(animal);
    }

    public SortedMap<Vector2d, List<IMapElement>> getObjectPositions() {
        return mapObjectsHandler.getObjectPositions();
    }

    public void addGrasses(){
        Random rn = new Random();
        Vector2d position = new Vector2d(rn.nextInt(width+1), rn.nextInt(height+1) );
        while(!mapObjectsHandler.addGrass(position)){
            position = new Vector2d(rn.nextInt(width+1), rn.nextInt(height+1) );
        }
    }

    public void removeGrass(Vector2d position){
        mapObjectsHandler.removeGrass(position);
    }





}
