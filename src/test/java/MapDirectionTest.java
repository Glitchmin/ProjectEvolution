import agh.ics.oop.Direction;
import agh.ics.oop.MapDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    void testujnext() {
        assertEquals(MapDirection.WEST.next(), MapDirection.NORTH);
        assertEquals(MapDirection.NORTH.next(), MapDirection.EAST);
        assertEquals(MapDirection.EAST.next(), MapDirection.SOUTH);
        assertEquals(MapDirection.SOUTH.next(), MapDirection.WEST);
    }
    @Test
    void testujprevious() {
        assertEquals(MapDirection.WEST.next().previous(), MapDirection.WEST);
        assertEquals(MapDirection.NORTH.next().previous(), MapDirection.NORTH);
        assertEquals(MapDirection.EAST.next().previous(), MapDirection.EAST);
        assertEquals(MapDirection.SOUTH.next().previous(), MapDirection.SOUTH);
    }
}
