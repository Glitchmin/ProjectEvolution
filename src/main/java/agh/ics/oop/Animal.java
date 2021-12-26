package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.System.out;

public class Animal implements IMapElement {
    private Vector2d position;
    private MapDirection direction;
    private final AbstractWorldMap map;
    final List<IPositionChangeObserver> observers = new ArrayList<>();
    private int energy;
    private final int dayOfBirth;
    private int childrenCounter;
    final private int [] genotype;
    private boolean isOffspringOfTrackedAnimal;
    private boolean isTracked;

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

    public boolean isOutOfEnergy(){ return energy < 0; }

    public int getEnergy() {
        return energy;
    }

    public void giveEnergy(int energyGiven){
        energy+=energyGiven;
    }

    public void subtractMoveEnergy(){
        energy-=moveEnergy;
    }

    public Animal(Animal parent1, Animal parent2, int dayOfBirth){
        this.dayOfBirth = dayOfBirth;
        this.childrenCounter=0;
        parent1.childrenCounter++;
        parent2.childrenCounter++;
        this.isOffspringOfTrackedAnimal = parent1.isOffspringOfTrackedAnimal || parent2.isOffspringOfTrackedAnimal || parent1.isTracked || parent2.isTracked;
        this.position = new Vector2d(parent1.getPosition().getX(), parent1.getPosition().getY());
        Random rn = new Random();
        this.direction = MapDirection.values()[rn.nextInt(8)];
        this.map = parent1.map;
        this.energy = parent1.energy/4 + parent2.energy/4;
        parent1.energy -= parent1.energy/4;
        parent2.energy -= parent2.energy/4;
        boolean doesP1GetRightSide = rn.nextBoolean();
        int howManyGenesDoesP1Give = (32* parent1.energy)/(parent1.energy+ parent2.energy);
        this.genotype=new int[32];
        if (!doesP1GetRightSide){
            System.arraycopy(parent1.genotype, 0, this.genotype, 0, howManyGenesDoesP1Give);
            System.arraycopy(parent2.genotype, howManyGenesDoesP1Give, this.genotype, howManyGenesDoesP1Give, 32-howManyGenesDoesP1Give);
        }else{
            System.arraycopy(parent2.genotype, 0, this.genotype, 0, 32-howManyGenesDoesP1Give);
            System.arraycopy(parent1.genotype, 32-howManyGenesDoesP1Give, this.genotype, 32-howManyGenesDoesP1Give, howManyGenesDoesP1Give);
        }
        Arrays.sort(this.genotype);
        /*out.print(parent1.energy);
        out.print(" ");
        out.print(parent2.energy);
        out.print(" ");
        out.println(doesP1GetRightSide);

        out.println(Arrays.toString(parent1.genotype));
        out.println(Arrays.toString(parent2.genotype));
        out.println(Arrays.toString(this.genotype));
        out.print("zwierzak powstal na skutek SEXU haha");
        out.println(Arrays.toString(this.genotype));*/
    }

    public Animal(AbstractWorldMap map, Vector2d initialPos, int dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
        this.position = initialPos;
        this.direction = MapDirection.NORTH;
        this.map = map;
        this.childrenCounter=0;
        this.energy = startEnergy;
        this.genotype = new int[32];
        Random rn = new Random();
        for (int i=0; i<32;i++){
            genotype[i]=rn.nextInt(8);
        }
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
        return Integer.toString(energy);
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public Vector2d getPosition() {
        return new Vector2d(this.position.getX(), this.position.getY());
    }

    public int[] getGenotype() {
        return genotype;
    }

    public boolean isAt(Vector2d position) {
        return (this.position.equals(position));
    }


    public void move() {
        Vector2d moveVector = new Vector2d(0, 0);
        int direction = genotype[new Random().nextInt(32)];
        switch (direction) {
            case 0 -> moveVector = moveVector.add(this.direction.toUnitVector());
            case 4 -> moveVector = moveVector.subtract(this.direction.toUnitVector());
            default -> this.direction = this.direction.turnRightBy(direction);
        }
        ;

        positionChanged(this.position, map.positionAfterMove(position,moveVector), this);
        this.position = map.positionAfterMove(position,moveVector);

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

    public int getChildrenCounter() {
        return childrenCounter;
    }

    public void setIsTracked(boolean isTracked){
        this.isTracked = isTracked;
        if (!isTracked){
            this.isOffspringOfTrackedAnimal=false;
        }
    }

    public boolean isOffspringOfTrackedAnimal() {
        return isOffspringOfTrackedAnimal;
    }

    public int getDayOfBirth() {
        return dayOfBirth;
    }
}
