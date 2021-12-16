package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import static java.lang.System.out;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class App extends Application implements IPositionChangeObserver {

    private AbstractWorldMap map;
    private GridPane gridPane;
    private Stage primaryStage;
    private SimulationEngine engine;
    private Thread engineThread;
    private TextField textField;

    private void addConstraintsForColumns(GridPane gridPane, Vector2d mapUpperRight, Vector2d mapLowerLeft) {
        for (int y = mapUpperRight.getY() + 1; y >= mapLowerLeft.getY(); y--) {
            gridPane.getRowConstraints().add(new RowConstraints(40));
        }

        for (int x = mapLowerLeft.getX(); x <= mapUpperRight.getX() + 1; x++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(40));
        }
    }

    private void addCenteredLabel(GridPane gridPane, String tekst, int x, int y){
        Label label = new Label(tekst);
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.add(label, x, y, 1, 1);

    }

    private void addObjectsAndYLabels(Object[][] mapCopy, GridPane gridPane, Vector2d mapUpperRight, Vector2d mapLowerLeft) {
        for (int y = mapUpperRight.getY(); y >= mapLowerLeft.getY(); y--) {
            addCenteredLabel(gridPane,Integer.toString(y),0, (mapUpperRight.getY() - y) + 1);
            for (int x = mapLowerLeft.getX(); x <= mapUpperRight.getX(); x++) {
                if (mapCopy[x-mapLowerLeft.getX()][y-mapLowerLeft.getY()] != null) {
                    try{
                        gridPane.add(new GuiElementBox((IMapElement)mapCopy[x-mapLowerLeft.getX()][y-mapLowerLeft.getY()],new Vector2d(x,y).toString()).getVBox(),x - mapLowerLeft.getX() + 1,(mapUpperRight.getY() - y) + 1,1,1);
                    }catch (java.io.FileNotFoundException ex){
                        out.println(ex);
                    }
                }
            }
        }
    }

    private void addXLabels(GridPane gridPane, Vector2d mapUpperRight, Vector2d mapLowerLeft) {
        for (int x = mapLowerLeft.getX(); x <= mapUpperRight.getX(); x++) {
            addCenteredLabel(gridPane, Integer.toString(x), x - mapLowerLeft.getX() + 1, 0);
        }
    }

    private void updateView() {

        gridPane.getChildren().clear();


        Vector2d mapUpperRight = map.wymiary()[1];
        Vector2d mapLowerLeft = map.wymiary()[0];
        Object[][] mapCopy = map.copy();

        gridPane.add(new Label("y/x"), 0, 0, 1, 1);

        addXLabels(gridPane, mapUpperRight, mapLowerLeft);
        addObjectsAndYLabels(mapCopy, gridPane, mapUpperRight, mapLowerLeft);
        addConstraintsForColumns(gridPane, mapUpperRight, mapLowerLeft);

        Button buttonStart = new Button("Do przodu, dzielne stworzonka!");

        buttonStart.setOnAction(actionEvent ->  {
            Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(2, 3)};
            engine = new SimulationEngine(map, positions);
            engineThread  = new Thread(engine);
            engine.addObserver(this);
            engine.setDirections(OptionsParser.parse(textField.getText().split(" ")));
            engineThread.start();
        });
        gridPane.setGridLinesVisible(true);
        gridPane.add(new HBox(buttonStart,textField),0,-mapLowerLeft.getY()+mapUpperRight.getY()+4,10,1);


        primaryStage.show();
    }

    @Override
    public void init(){
        try {

            map = new GrassField(10);


        } catch (IllegalArgumentException ex) {
            out.println(ex);
        }


    }

    public void start(Stage primaryStage) {
        gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 600, 700);
        textField = new TextField("f b r l f f r r f f f f f f f f");
        primaryStage.setScene(scene);
        this.primaryStage=primaryStage;
        Platform.runLater(primaryStage::show);
        Platform.runLater(this::updateView);
    }


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        out.println("app position changed");
        Platform.runLater(this::updateView);
    }
}
