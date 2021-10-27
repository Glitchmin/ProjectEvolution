import agh.ics.oop.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    void testuj_equals() {
        assertEquals(new Vector2d(1,1), new Vector2d(1,1));
    }
    @Test
    void testuj_toString(){
        assertEquals(new Vector2d(-1,1).toString(),"(-1,1)");
    }
    @Test
    void testuj_precedes(){
        assertTrue(new Vector2d(-1,1).precedes(new Vector2d(-1,1)));
        assertTrue(new Vector2d(-1,1).precedes(new Vector2d(1,2)));
        assertTrue(new Vector2d(-1,1).precedes(new Vector2d(-1,2)));
        assertFalse(new Vector2d(-1,1).precedes(new Vector2d(-1,-1)));
    }
    @Test
    void testuj_follows(){
        assertTrue(new Vector2d(-1,1).follows(new Vector2d(-1,1)));
        assertTrue(new Vector2d(1,2).follows(new Vector2d(-1,1)));
        assertTrue(new Vector2d(-1,2).follows(new Vector2d(-1,1)));
        assertFalse(new Vector2d(-1,-1).follows(new Vector2d(-1,1)));
    }
    @Test
    void testuj_upperRight(){
        assertEquals(new Vector2d(-1,1).upperRight(new Vector2d(2,-1)),new Vector2d(2,1));
        assertEquals(new Vector2d(5,10).upperRight(new Vector2d(7,12)),new Vector2d(7,12));
    }
    @Test
    void testuj_lowerLeft(){
        assertEquals(new Vector2d(-1,1).lowerLeft(new Vector2d(2,-1)),new Vector2d(-1,-1));
        assertEquals(new Vector2d(5,10).lowerLeft(new Vector2d(7,12)),new Vector2d(5,10));
    }
    @Test
    void testuj_add(){
        assertEquals(new Vector2d(1,1).add(new Vector2d(2,2)),new Vector2d(3,3));
        assertEquals(new Vector2d(-1,1).add(new Vector2d(1,-1)),new Vector2d(0,0));
    }
    @Test
    void testuj_substract(){
        assertEquals(new Vector2d(1,1).subtract(new Vector2d(2,2)),new Vector2d(-1,-1));
        assertEquals(new Vector2d(0,0).subtract(new Vector2d(1,-1)),new Vector2d(-1,1));
    }
    @Test
    void testuj_opposite(){
        assertEquals(new Vector2d(1,1).opposite(),new Vector2d(-1,-1));
        assertEquals(new Vector2d(7,-8).opposite(),new Vector2d(-7,8));
    }
}
