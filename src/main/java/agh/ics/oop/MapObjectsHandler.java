package agh.ics.oop;

import java.util.*;

public class MapObjectsHandler implements IPositionChangeObserver {


    SortedMap<Vector2d, List<IMapElement>> objectPositions = new TreeMap<>(Vector2d.xFirstComparator);
    List<Animal> aliveAnimals = new Vector<>();
    List<Vector2d> grassPositionsList = new Vector<>();
    Set<Vector2d> freePositionsInTheJungle = new TreeSet<>(Vector2d.xFirstComparator);
    Set<Vector2d> freePositionsOutsideTheJungle = new TreeSet<>(Vector2d.xFirstComparator);

    private void addAFreePosition(Vector2d position) {
        if (AbstractWorldMap.isInsideTheJungle(position)) {
            freePositionsInTheJungle.add(position);
        } else {
            freePositionsOutsideTheJungle.add(position);
        }
    }

    private void removeAFreePosition(Vector2d position) {
        if (AbstractWorldMap.isInsideTheJungle(position)) {
            freePositionsInTheJungle.remove(position);
        } else {
            freePositionsOutsideTheJungle.remove(position);
        }
    }

    public boolean isThereAFreeJunglePosition() {
        return !freePositionsInTheJungle.isEmpty();
    }

    public boolean isThereAFreeNoJunglePosition() {
        return !freePositionsOutsideTheJungle.isEmpty();
    }

    public Vector2d getARandomFreeJunglePosition() {
        int whichPosition = new Random().nextInt(freePositionsInTheJungle.size());
        return (Vector2d) freePositionsInTheJungle.toArray()[whichPosition];
    }

    public Vector2d getARandomFreeNoJunglePosition() {
        int whichPosition = new Random().nextInt(freePositionsOutsideTheJungle.size());
        return (Vector2d) freePositionsOutsideTheJungle.toArray()[whichPosition];
    }


    MapObjectsHandler() {
        for (int i = 0; i < AbstractWorldMap.getWidth(); i++) {
            for (int j = 0; j < AbstractWorldMap.getHeight(); j++) {
                addAFreePosition(new Vector2d(i, j));
            }
        }
    }

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

    public boolean isOccupied(Vector2d position) {
        return objectPositions.get(position) != null;
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

    public void addGrass(Vector2d position) {
        if (objectPositions.get(position) != null) {
            return;
        }
        grassPositionsList.add(position);
        add(position, new Grass(position));
    }

    private Grass getGrassAtPos(Vector2d position) {
        if (objectPositions.get(position) == null) {
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
        removeAFreePosition(position);
    }

    private void remove(Vector2d position, IMapElement object) {
        if (objectPositions.containsKey(position)) {
            objectPositions.get(position).remove(object);
        }
        if (objectPositions.containsKey(position) && objectPositions.get(position).isEmpty()) {
            objectPositions.remove(position);
            addAFreePosition(position);
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

    public List<Vector2d> getGrassPositionsList() {
        return grassPositionsList;
    }

    public void removeTrackingFromAnimals() {
        for (Animal animal : getAliveAnimals()) {
            animal.setIsTracked(false);
        }
    }
}
