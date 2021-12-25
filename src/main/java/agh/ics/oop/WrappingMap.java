package agh.ics.oop;

import java.util.Vector;

public class WrappingMap extends AbstractWorldMap{

    public Vector2d positonAfterMove(Vector2d oldPosition, Vector2d moveVector) {
        Vector2d newPosition = oldPosition.add(moveVector);
            return new Vector2d((newPosition.x+width+1)%(width+1), (newPosition.y+height+1)%(height+1));

    }
}
