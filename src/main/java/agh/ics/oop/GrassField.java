package agh.ics.oop;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static java.lang.System.out;

import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap {
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
        Grass grass = new Grass(position);
        mapBoundary.add(position, grass);

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
            animal.addObserver(this);
            animal.addObserver(mapBoundary);
            mapBoundary.add(animal.getPosition(), animal);
            return true;
        }
        throw new IllegalArgumentException("nie można tu stawiać zwierzaków mój panie");
    }


    public void eatgrass(Grass grass){
        mapBoundary.remove(grass.getPosition());
    }




    public boolean canMoveTo(Vector2d position){
        return !(objectAt(position) instanceof Animal);
    }
}
