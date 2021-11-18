package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {


    @Test
    void testuj() {
        String[] dirs = {"f","r","f","l","f","r"};
        MoveDirection[] directions = OptionsParser.parse(dirs);
        IWorldMap map = new RectangularMap(10, 5);
        Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(2, 4)};
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();
        assert(map.isOccupied(new Vector2d(2,3)));
        assert(map.isOccupied(new Vector2d(2,4)));
    }

    @Test
    void testuj2() {
        String[] dirs = {"l","r","f","f","f","f","f","f","f"};
        MoveDirection[] directions = OptionsParser.parse(dirs);
        IWorldMap map = new RectangularMap(10, 5);
        Vector2d[] positions = {new Vector2d(0, 2), new Vector2d(10, 4), new Vector2d(5,4)};
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();
        assert(map.isOccupied(new Vector2d(0,2)));
        assert(map.isOccupied(new Vector2d(10,4)));
        assert(map.isOccupied(new Vector2d(5,5)));
    }
}