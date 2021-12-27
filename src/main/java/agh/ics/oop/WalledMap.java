package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class WalledMap extends AbstractWorldMap {

    public boolean canMoveTo(Vector2d position) {
        return (position.getX() < width && position.getX() >= 0 && position.getY() >= 0 && position.getY() < height);
    }

    @Override
    public Vector2d positionAfterMove(Vector2d oldPosition, Vector2d moveVector) {
        if (canMoveTo(oldPosition.add(moveVector))){
            return oldPosition.add(moveVector);
        }else{
            return oldPosition;
        }
    }
}
