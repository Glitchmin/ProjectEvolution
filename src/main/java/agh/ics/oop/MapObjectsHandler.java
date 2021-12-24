package agh.ics.oop;

import java.util.*;

import static java.lang.System.out;

public class MapObjectsHandler implements IPositionChangeObserver {

    Comparator<Vector2d> vector2dComparator = (a, b) -> {
        if (a.x == b.x && a.y == b.y) {
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
    };

    SortedMap<Vector2d, List<IMapElement>> objectPositions = new TreeMap<>(vector2dComparator);
    List<Animal> aliveAnimals = new Vector<>();
    List<Vector2d> grassPositionsList = new Vector<>();

    public List<Animal> getAliveAnimals() {
        return aliveAnimals;
    }

    public SortedMap<Vector2d, List<IMapElement>> getObjectPositions() {
        return objectPositions;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        remove(oldPosition, object);
        add(newPosition, object);
    }

    public List<IMapElement> objectAt(Vector2d position) {
        return objectPositions.get(position);
    }


    public void addAnimal(Animal animal) {
        aliveAnimals.add(animal);
        add(animal.getPosition(), animal);
    }

    public void removeAnimal(Animal animal) {
        aliveAnimals.remove(animal);
        remove(animal.getPosition(), animal);
    }

    public boolean addGrass(Vector2d position) {
        if (getGrassAtPos(position)!=null){
            return false;
        }
        grassPositionsList.add(position);
        add(position, new Grass(position));
        return true;
    }

    private Grass getGrassAtPos(Vector2d position) {
        if (objectPositions.get(position) == null){
            return null;
        }
        for (IMapElement iMapElement : objectPositions.get(position)) {
            if (iMapElement instanceof Grass) {
                return (Grass) iMapElement;
            }
        }
        return null;
    }

    public void removeGrass(Vector2d position) {
        grassPositionsList.remove(position);
        remove(position, getGrassAtPos(position));
    }

    private void add(Vector2d position, IMapElement object) {
        objectPositions.computeIfAbsent(position, k -> new Vector<>());
        objectPositions.get(position).add(object);
    }

    private void remove(Vector2d position, IMapElement object) {
        objectPositions.get(position).remove(object);
        if (objectPositions.get(position).isEmpty()) {
            objectPositions.remove(position);
        }
    }


    public List<IMapElement> getMapElementsList() {
        List<IMapElement> mapElementsList = new Vector<>();
        for (Vector2d grassPosition : grassPositionsList) {
            mapElementsList.add(getGrassAtPos(grassPosition));
        }
        mapElementsList.addAll(aliveAnimals);
        return mapElementsList;
    }

}
