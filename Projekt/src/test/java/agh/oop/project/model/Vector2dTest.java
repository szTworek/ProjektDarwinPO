package agh.oop.project.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void addTest(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(3,4);
        Vector2d v3 = new Vector2d(5,6);

        assertEquals(new Vector2d(4,6),v1.add(v2));
        assertEquals(new Vector2d(4,6),v2.add(v1));
        assertEquals(new Vector2d(8,10),v2.add(v3));
        assertEquals(new Vector2d(8,10),v3.add(v2));
    }

    @Test
    void subtractTest(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(3,4);
        Vector2d v3 = new Vector2d(5,6);

        assertEquals(new Vector2d(2,2),v2.subtract(v1));
        assertEquals(new Vector2d(2,2),v3.subtract(v2));
        assertEquals(new Vector2d(4,4),v3.subtract(v1));
        assertEquals(new Vector2d(-2, -2),v1.subtract(v2));
    }

    @Test
    void isNearTest(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(3,4);
        Vector2d v3 = new Vector2d(3,5);
        Vector2d v4 = new Vector2d(9,2);
        List<Vector2d> list1 = List.of(v2,v3);
        List<Vector2d> list2 = List.of(v1,v2);

        assertFalse(v1.isNear(v3, 2, 10));
        assertFalse(v1.isNear(v2, 2, 10));
        assertTrue(v2.isNear(v3, 2, 10));
        assertTrue(v4.isNear(v1, 2, 10));

        assertFalse(v1.isNear(list1, 2, 10));
        assertTrue(v3.isNear(list2, 2, 10));
    }

    @Test
    void goAroundTheGlobeTest(){
        Vector2d v1 = new Vector2d(1,1);

        assertEquals(new Vector2d(1,-1), v1.add(new Vector2d(0,-2)).goAroundTheGlobe(10));
        assertEquals(new Vector2d(9,1), v1.add(new Vector2d(-2, 0)).goAroundTheGlobe(10));
        assertEquals(new Vector2d(9, -1), v1.add(new Vector2d(-2,-2)).goAroundTheGlobe(10));
    }

}