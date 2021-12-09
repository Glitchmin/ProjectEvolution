package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import static java.lang.System.out;

import java.io.PrintStream;

public class App extends Application {
    public void start(Stage primaryStage) {

        MoveDirection[] directions;
        try {
            String[] args = getParameters().getRaw().toArray(new String[0]);
            directions = OptionsParser.parse(args);
        } catch (IllegalArgumentException ex) {
            out.println(ex);
            return;
        }
        AbstractWorldMap map = new GrassField(10);

        Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(2, 3)};
        try {
            IEngine engine = new SimulationEngine(directions, map, positions);
            engine.run();
        } catch (IllegalArgumentException ex) {
            out.println(ex);
        }


        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);


        gridPane.add(new Label("y/x"), 0, 0, 1, 1);
        Vector2d mapUpperRight = map.wymiary()[1];
        Vector2d mapLowerLeft = map.wymiary()[0];

        addXLabels(gridPane, mapUpperRight, mapLowerLeft);

        addObjectsAndYLabels(map, gridPane, mapUpperRight, mapLowerLeft);

        addConstraintsForColumns(gridPane, mapUpperRight, mapLowerLeft);


        Scene scene = new Scene(gridPane, (mapUpperRight.getX()-mapLowerLeft.getX()+2)*20, (mapUpperRight.getY()-mapLowerLeft.getY()+2)*15);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.show();

    }

    private void addConstraintsForColumns(GridPane gridPane, Vector2d mapUpperRight, Vector2d mapLowerLeft) {
        for (int y = mapUpperRight.getY() + 1; y >= mapLowerLeft.getY(); y--) {
            gridPane.getRowConstraints().add(new RowConstraints(15));
        }

        for (int x = mapLowerLeft.getX(); x <= mapUpperRight.getX() + 1; x++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(20));
        }
    }

    private void addCenteredLabel(GridPane gridPane, String tekst, int x, int y){
        Label label = new Label(tekst);
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.add(label, x, y, 1, 1);

    }

    private void addObjectsAndYLabels(AbstractWorldMap map, GridPane gridPane, Vector2d mapUpperRight, Vector2d mapLowerLeft) {
        for (int y = mapUpperRight.getY(); y >= mapLowerLeft.getY(); y--) {
            addCenteredLabel(gridPane,Integer.toString(y),0, (mapUpperRight.getY() - y) + 1);
            for (int x = mapLowerLeft.getX(); x <= mapUpperRight.getX(); x++) {
                String text = "";
                if (map.objectAt(new Vector2d(x, y)) != null) {
                    text = map.objectAt(new Vector2d(x, y)).toString();
                }
                addCenteredLabel(gridPane,text,x - mapLowerLeft.getX() + 1,(mapUpperRight.getY() - y) + 1);
            }
        }
    }

    private void addXLabels(GridPane gridPane, Vector2d mapUpperRight, Vector2d mapLowerLeft) {
        for (int x = mapLowerLeft.getX(); x <= mapUpperRight.getX(); x++) {
            addCenteredLabel(gridPane, Integer.toString(x), x - mapLowerLeft.getX() + 1, 0);
        }
    }
}
