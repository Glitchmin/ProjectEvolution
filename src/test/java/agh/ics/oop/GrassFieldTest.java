package agh.ics.oop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {
    @Test
    void testmap() {
        String[] dirs = {"f","r","f","l","f","r"};
        MoveDirection[] directions = OptionsParser.parse(dirs);
        IWorldMap map = new GrassField(10);
        Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(2, 2)};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            IEngine engine = new SimulationEngine(directions, map, positions);
            engine.run();
        });
    }

    @Test
    void testparser() {
        String[] dirs = {"mlem","r","f","l","f","r"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MoveDirection[] directions = OptionsParser.parse(dirs);
        });
        assertTrue(exception.toString().contains("argument mlem jest głupi"));
    }


    @Test
    void testmap2() {
        String[] dirs = {"f","r","f","l","f","l","f","f","f","f","f","f"};
        MoveDirection[] directions = OptionsParser.parse(dirs);
        IWorldMap map = new GrassField(10);
        Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(2, 4)};
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();
        assert(map.isOccupied(new Vector2d(2,5)));
        assert(map.isOccupied(new Vector2d(-1,4)));
    }
    @Test
    void testmap3() {
        String[] dirs = {"r","r","l","l","f","f","f","f"};
        MoveDirection[] directions = OptionsParser.parse(dirs);
        IWorldMap map = new GrassField(10);
        Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(3, 2),new Vector2d(4, 2),new Vector2d(5, 2)};
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();
        assert(map.isOccupied(new Vector2d(2,2)));
        assert(map.isOccupied(new Vector2d(3,2)));
        assert(map.isOccupied(new Vector2d(4,2)));
        assert(map.isOccupied(new Vector2d(5,2)));
    }

    @Test
    void testmap3aleRect() {
        String[] dirs = {"r","r","l","l","f","f","f","f"};
        MoveDirection[] directions = OptionsParser.parse(dirs);
        IWorldMap map = new RectangularMap(5,5);
        Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(3, 2),new Vector2d(4, 2),new Vector2d(5, 2)};
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();
        assert(map.isOccupied(new Vector2d(2,2)));
        assert(map.isOccupied(new Vector2d(3,2)));
        assert(map.isOccupied(new Vector2d(4,2)));
        assert(map.isOccupied(new Vector2d(5,2)));
    }

    @Test
    void testmap2aleRect() {
        String[] dirs = {"f","r","f","l","f","l","f","f","f","f","f","f"};
        MoveDirection[] directions = OptionsParser.parse(dirs);
        IWorldMap map = new RectangularMap(10,5);
        Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(2, 4)};
        IEngine engine = new SimulationEngine(directions, map, positions);
        engine.run();
        assert(map.isOccupied(new Vector2d(2,5)));
        assert(map.isOccupied(new Vector2d(0,4)));
    }
}