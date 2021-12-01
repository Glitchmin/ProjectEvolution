package agh.ics.oop;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.lang.System.out;

import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap {
    Vector2d lowerLeft;
    Vector2d upperRight;
    private int  grassAmount;

    public int getGrassAmount(){
        return grassAmount;
    }



    public void addGrass(int grassAmount){
        Random rd = new Random();
        this.grassAmount = grassAmount;
        Vector2d position = new Vector2d(rd.nextInt((int)sqrt(grassAmount*10)), rd.nextInt((int)sqrt(grassAmount*10)));
        while (objectAt(position) != null) {
            position = new Vector2d(rd.nextInt((int)sqrt(grassAmount*10)), rd.nextInt((int)sqrt(grassAmount*10)));
        }
        objects_pos.put(position, new Grass(position));
    }

    public GrassField(int grassAmount) {
        for (int i = 0; i < grassAmount; i++) {
            addGrass(grassAmount);
        }
    }

    public boolean place (Animal animal){
        if (objectAt(animal.getPosition()) instanceof Grass){
            eatgrass((Grass) objectAt(animal.getPosition()));
        }
        if (!isOccupied(animal.getPosition())){
            objects_pos.put(animal.getPosition(), animal);
            animal.addObserver(this);
            return true;
        }
        return false;
    }


    public void eatgrass(Grass grass){
        objects_pos.remove(grass.getPosition());
    }



    public boolean canMoveTo(Vector2d position){
        return !(objectAt(position) instanceof Animal);
    }
}
