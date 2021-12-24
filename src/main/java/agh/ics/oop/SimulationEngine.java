package agh.ics.oop;

import java.util.*;

import static java.lang.System.out;

public class SimulationEngine implements IEngine, Runnable {
    AbstractWorldMap map;
    final List<IPositionChangeObserver> observers = new ArrayList<>();
    Integer moveDelayMs = 300;

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    public SimulationEngine(AbstractWorldMap map, Vector2d[] positions) {
        for (Vector2d position : positions) {
            Animal animal = new Animal(map, position);
            map.place(animal);
        }
        this.map = map;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition, object);
        }
    }

    private void removeDeadAnimals() {
        for (Animal animal : map.getAliveAnimals()) {
            if (animal.isOutOfEnergy()) {
                map.removeAnimal(animal);
                map.getAliveAnimals().remove(animal);
            }
        }
    }

    private void moveAllAnimals() {
        for (Animal animal : map.getAliveAnimals()) {
            animal.move();
        }
    }

    private void feedAllAnimals() {
        SortedMap<Vector2d, List<IMapElement>> objectPositions = map.getObjectPositions();
        List<Vector2d> keySetCopy = new Vector<>(objectPositions.keySet());
        for (Vector2d position : keySetCopy) {
            calculateEatingForPos(objectPositions, position);
        }
    }

    private void calculateEatingForPos(SortedMap<Vector2d, List<IMapElement>> objectPositions, Vector2d position) {
        Integer highestEnergy = null;
        int highestEnergyCounter = 0;
        boolean isGrassHere = false;

        for (IMapElement mapElement : objectPositions.get(position)) {
            if (mapElement instanceof Animal) {
                if(highestEnergy == null){
                    highestEnergy = ((Animal) mapElement).getEnergy();
                    highestEnergyCounter=1;
                }else{
                    if (((Animal) mapElement).getEnergy() == highestEnergy){
                        highestEnergyCounter++;
                    }
                    if (((Animal) mapElement).getEnergy() > highestEnergy){
                        highestEnergy = ((Animal) mapElement).getEnergy();
                        highestEnergyCounter=1;
                    }
                }
            }
            if (mapElement instanceof Grass){
                isGrassHere=true;
            }

        }
        if (isGrassHere){
            for (IMapElement mapElement : objectPositions.get(position)) {
                if (mapElement instanceof Animal && highestEnergy != null && ((Animal) mapElement).getEnergy() == highestEnergy){
                    ((Animal) mapElement).giveEnergy(Animal.getPlantEnergy()/highestEnergyCounter);
                }
            }
            if (highestEnergy!=null) {
                map.removeGrass(position);
            }
        }
    }

    private void addGrassToMap(){
        map.addGrasses();
    }

    public void run() {
        while (true) {
            out.println(map);
            removeDeadAnimals();
            moveAllAnimals();
            feedAllAnimals();
            //hehe sex here
            addGrassToMap();

            positionChanged(new Vector2d(0, 0), new Vector2d(1, 1), null);

            try {
                Thread.sleep(moveDelayMs);
            } catch (InterruptedException e) {
                out.println("Interrupted Threat Simulation Engine");
                e.printStackTrace();
            }
        }
    }

}
