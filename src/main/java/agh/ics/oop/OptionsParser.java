package agh.ics.oop;

public class OptionsParser {
    public static MoveDirection[] parse(String[] parametry) throws IllegalArgumentException {
        MoveDirection[] tabdir = new MoveDirection[parametry.length];
        int i = 0;
        for (String parametr : parametry) {
            switch (parametr) {
                case "f", "forward" -> tabdir[i] = MoveDirection.FORWARD;
                case "b", "backward" -> tabdir[i] = MoveDirection.BACKWARD;
                case "r", "right" -> tabdir[i] = MoveDirection.RIGHT;
                case "l", "left" -> tabdir[i] = MoveDirection.LEFT;
                default -> throw new IllegalArgumentException("argument "+parametr+" jest głupi");
            }
            i++;
        }
        return tabdir;
    }

}
