package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;

public class Animal implements IMapElement {
    private Vector2d position;
    private MapDirection direction;
    private final IWorldMap map;
    final List<IPositionChangeObserver> observers = new ArrayList<>();
    private int energy;
    final int [] genotype;

    static private int startEnergy;
    static private int moveEnergy;
    static private int plantEnergy;

    public static int getStartEnergy() {
        return startEnergy;
    }

    public static void setStartEnergy(int startEnergy) {
        Animal.startEnergy = startEnergy;
    }

    public static int getMoveEnergy() {
        return moveEnergy;
    }

    public static void setMoveEnergy(int moveEnergy) {
        Animal.moveEnergy = moveEnergy;
    }

    public static int getPlantEnergy() {
        return plantEnergy;
    }

    public static void setPlantEnergy(int plantEnergy) {
        Animal.plantEnergy = plantEnergy;
    }

    public Animal(IWorldMap map, Vector2d initialPos) {
        this.position = initialPos;
        this.direction = MapDirection.NORTH;
        this.map = map;

        this.energy = new Random().nextInt()%startEnergy;
        this.genotype = new int[32];
        Random rn = new Random();
        for (int i=0; i<32;i++){
            genotype[i]=rn.nextInt(8);
        }
        out.println("utworzono zwierzaka");
        Arrays.sort(genotype);
        out.println(Arrays.toString(genotype));
    }

    @Override
    public String getResourcePath() {
        return "src/main/resources/animal.png";
    }

    public double getEnergySaturation(){
        return (double)energy/(double)startEnergy;
    }

    public String toString() {
        return "a";
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public Vector2d getPosition() {
        return new Vector2d(this.position.x, this.position.y);
    }


    public boolean isAt(Vector2d position) {
        return (this.position.x == position.x && this.position.y == position.y);
    }


    public void move(MoveDirection direction) {
        Vector2d przem = new Vector2d(0, 0);
        switch (direction) {
            case RIGHT -> this.direction = this.direction.next();
            case LEFT -> this.direction = this.direction.previous();
            case FORWARD -> przem = przem.add(this.direction.toUnitVector());
            case BACKWARD -> przem = przem.subtract(this.direction.toUnitVector());
        }
        ;

        if (map.canMoveTo(this.position.add(przem))) {
            positionChanged(this.position, this.position.add(przem), this);
            this.position = this.position.add(przem);
        }

    }

    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition, this);
        }
    }
}
