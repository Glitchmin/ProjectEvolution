package agh.ics.oop;

public class OptionsParser {
    public static MoveDirection[] parse(String[] parametry) {
        MoveDirection[] tabdir = new MoveDirection[parametry.length];
        int i = 0;
        for (String parametr : parametry) {
            i++;
            switch (parametr) {
                case "f", "forward" -> tabdir[i] = MoveDirection.FORWARD;
                case "b", "backward" -> tabdir[i] = MoveDirection.BACKWARD;
                case "r", "right" -> tabdir[i] = MoveDirection.RIGHT;
                case "l", "left" -> tabdir[i] = MoveDirection.LEFT;
                default -> i--;
            }
        }
        return tabdir;
    }

}
