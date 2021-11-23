package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.sqrt;

public class GrassField implements IWorldMap {
    final List<Grass> grassList = new ArrayList<>();
    Vector2d lowerLeft;
    Vector2d upperRight;



    public GrassField(int grassAmount) {
        for (int i = 0; i < grassAmount; i++) {
            Random rd = new Random();
            Vector2d position = new Vector2d(rd.nextInt(), rd.nextInt());
            while (objectAt(position) != null) {
                position = new Vector2d(rd.nextInt()%(int)sqrt(grassAmount*10), rd.nextInt()%(int)sqrt(grassAmount*10));
            }

        }
    }

}

    public Object objectAt(Vector2d position) {
        for (Grass grass : grassList) {
            if (grass.getPosition().equals(position)) {
                return grass;
            }
        }
        return null;
    }
}
