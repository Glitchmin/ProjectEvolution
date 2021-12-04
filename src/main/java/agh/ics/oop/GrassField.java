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
    MapBoundary mapBoundary;

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
        objects_pos.put(position, grass);

    }

    public GrassField(int grassAmount) {
        mapBoundary = new MapBoundary();
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
            animal.addObserver(mapBoundary);
            mapBoundary.add(animal.getPosition(), animal);
            return true;
        }
        throw new IllegalArgumentException("nie można tu stawiać zwierzaków mój panie");
    }


    public void eatgrass(Grass grass){
        objects_pos.remove(grass.getPosition());
        mapBoundary.remove(grass.getPosition());
    }

    @Override
    protected Vector2d[] wymiary(){
        Vector2d[] tab = new Vector2d[2];
        tab[0] = mapBoundary.getLower_left();
        tab[1] = mapBoundary.getUpper_right();
        out.println(tab[1]);
        return tab;
    }


    public boolean canMoveTo(Vector2d position){
        return !(objectAt(position) instanceof Animal);
    }
}
