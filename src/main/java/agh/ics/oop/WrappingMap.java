package agh.ics.oop;

import java.util.Vector;

public class WrappingMap extends AbstractWorldMap{

    public Vector2d positionAfterMove(Vector2d oldPosition, Vector2d moveVector) {
        Vector2d newPosition = oldPosition.add(moveVector);
            return new Vector2d((newPosition.x+width)%(width), (newPosition.y+height)%(height));

    }
}
