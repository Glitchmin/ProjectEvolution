package agh.ics.oop;

import java.util.Arrays;

import static java.lang.System.arraycopy;
import static java.lang.System.out;

public class World {

    public static Direction[] zamien(String[] s) {
        Direction[] tab_dir = new Direction[s.length];
        int i = 0;
        for (String str : s) {
            tab_dir[i] = switch (str) {
                case "f" -> Direction.FORWARD;
                case "b" -> Direction.BACKWARD;
                case "r" -> Direction.RIGHT;
                case "l" -> Direction.LEFT;
                default -> Direction.FORWARD;
            };
            i++;
        }
        return tab_dir;
    }

    public static void run(Direction[] s) {
        out.println("Start");
        for (Direction argument : s) {
            switch (argument) {
                case FORWARD:
                    out.println("zwierzak idzie do przodu");
                    break;
                case BACKWARD:
                    out.println("zwierzak idzie do tyłu");
                    break;
                case RIGHT:
                    out.println("zwierzak skręca w prawo");
                    break;
                case LEFT:
                    out.println("zwierzak skręca w lewo");
                    break;
                default:
                    out.println("nieznana komenda");
                    break;
            }
        }
        out.println("Stop");
    }

    public static void main(String[] args) {
        /*out.println("system wystartował");
        String[] tab = new String[]{"f", "f", "r", "l", "b"};
        Direction[] tab_dir = zamien(tab);
        run(tab_dir);
        out.println("system zakończył działanie");*/
        Vector2d position1 = new Vector2d(1,2);
        System.out.println(position1);
        Vector2d position2 = new Vector2d(-2,1);
        System.out.println(position2);
        System.out.println(position1.add(position2));
        MapDirection tmp;
        tmp = MapDirection.NORTH;
        out.println(tmp.toString());
        tmp = tmp.next();
        out.println(tmp.toString());
        tmp = tmp.next();
        out.println(tmp .toString());
        tmp = tmp.next();
        out.println(tmp.toString());
    }
}
