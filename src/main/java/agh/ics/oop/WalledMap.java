package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class WalledMap extends AbstractWorldMap {

    public boolean canMoveTo(Vector2d position) {
        return (position.x <= width && position.x >= 0 && position.y >= 0 && position.y <= height);
    }


}
