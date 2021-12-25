package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;


public class App extends Application implements IPositionChangeObserver {

    private AbstractWorldMap map;
    private GridPane gridPane;
    private Stage primaryStage;
    private SimulationEngine engine;
    private Thread engineThread;
    private List<TextField> menuTextFields;

    private void addConstraintsForColumns(GridPane gridPane, Vector2d mapUpperRight) {
        for (int y = mapUpperRight.getY() + 1; y >= 0; y--) {
            gridPane.getRowConstraints().add(new RowConstraints(40));
        }

        for (int x = 0; x <= mapUpperRight.getX() + 1; x++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(40));
        }
    }

    private void addCenteredLabel(GridPane gridPane, String tekst, int x, int y) {
        Label label = new Label(tekst);
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.add(label, x, y, 1, 1);

    }

    private void addYLabels(GridPane gridPane, Vector2d mapUpperRight) {
        for (int y = mapUpperRight.getY(); y >= 0; y--) {
            addCenteredLabel(gridPane, Integer.toString(y), 0, (mapUpperRight.getY() - y) + 1);
        }
    }


    private void addXLabels(GridPane gridPane, Vector2d mapUpperRight) {
        for (int x = 0; x <= mapUpperRight.getX(); x++) {
            addCenteredLabel(gridPane, Integer.toString(x), x + 1, 0);
        }
    }

    private void updateView() {

        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
        Vector2d mapUpperRight = new Vector2d(AbstractWorldMap.getWidth(), AbstractWorldMap.getHeight());
        List<IMapElement> mapCopy = map.getCopyOfMapElements();

        addCenteredLabel(gridPane, "y/x", 0, 0);

        addXLabels(gridPane, mapUpperRight);
        addYLabels(gridPane, mapUpperRight);
        addConstraintsForColumns(gridPane, mapUpperRight);

        for (IMapElement mapElement: mapCopy){
            try {
                gridPane.add(new GuiElementBox(mapElement,mapElement.getPosition().toString()).getVBox(),mapElement.getPosition().getX()+1,AbstractWorldMap.getHeight() - mapElement.getPosition().getY()+1,1,1);
            }catch (java.io.FileNotFoundException ex){
                out.println(ex);
            }
        }

        Button buttonStart = new Button("Go!");

        buttonStart.setOnAction(actionEvent -> {
            Vector2d[] positions = {new Vector2d(2, 2), new Vector2d(2,2)};
            engine = new SimulationEngine(map, positions);
            engineThread = new Thread(engine);
            engine.addObserver(this);
            engineThread.start();
        });
        gridPane.add(new HBox(buttonStart), 0, mapUpperRight.getY() + 4, 10, 1);
        out.println(gridPane.gridLinesVisibleProperty());
        primaryStage.show();
    }

    @Override
    public void init() {

    }

    private void renderMenu() {
        gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 500, 500);

        addParamFieldsToMenu();

        Button buttonStart = new Button("Start!");
        gridPane.add(buttonStart, 0, 7, 1, 1);
        primaryStage.setScene(scene);
        Platform.runLater(primaryStage::show);

        buttonStart.setOnAction(actionEvent -> {
            getParamsFromMenuTextFields();
            map = new WrappingMap();
            Platform.runLater(this::updateView);
        });

    }

    private void getParamsFromMenuTextFields() {
        AbstractWorldMap.setHeight(Integer.parseInt(menuTextFields.get(0).getText()));
        AbstractWorldMap.setWidth(Integer.parseInt(menuTextFields.get(1).getText()));
        Animal.setStartEnergy(Integer.parseInt(menuTextFields.get(2).getText()));
        Animal.setMoveEnergy(Integer.parseInt(menuTextFields.get(3).getText()));
        Animal.setPlantEnergy(Integer.parseInt(menuTextFields.get(4).getText()));
        AbstractWorldMap.setJungleRatio(Double.parseDouble(menuTextFields.get(5).getText()));
        out.println("test:");
        for (int i = 0; i < 5; i++) {
            out.println(menuTextFields.get(i).getText());
        }
    }

    private void addParamFieldsToMenu() {
        String[] intParamNames = {"Width", "Height", "Start Energy", "Move Energy", "Plant Energy"};
        menuTextFields = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TextField intParamTextField = new TextField(new Integer[]{9, 9, 100, 1, 10}[i].toString());
            gridPane.add(new HBox(new Label(intParamNames[i]), intParamTextField), 0, i, 1, 1);
            menuTextFields.add(intParamTextField);
        }
        TextField jungleParamTextField = new TextField("0.5");
        gridPane.add(new HBox(new Label("Jungle Ratio"), jungleParamTextField), 0, 6, 1, 1);
        menuTextFields.add(jungleParamTextField);


    }

    public void start(Stage primaryStage) {
        gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 600, 700);
        primaryStage.setScene(scene);
        this.primaryStage = primaryStage;
        Platform.runLater(primaryStage::show);
        Platform.runLater(this::renderMenu);
    }


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        out.println("app position changed");
        Platform.runLater(this::updateView);
    }
}
