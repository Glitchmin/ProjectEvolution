package agh.ics.oop;

public class WalledMap extends AbstractWorldMap {

    public boolean canMoveTo(Vector2d position) {
        return (position.x < width && position.x >= 0 && position.y >= 0 && position.y < height);
    }

    @Override
    public Vector2d positionAfterMove(Vector2d oldPosition, Vector2d moveVector) {
        if (canMoveTo(oldPosition.add(moveVector))) {
            return oldPosition.add(moveVector);
        } else {
            return oldPosition;
        }
    }
}
