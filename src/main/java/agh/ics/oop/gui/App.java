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


public class App extends Application implements IDayChangeObserver, IPositionChangeObserver {

    private GridPane gridPaneOfEverything;
    private Stage primaryStage;
    private SimulationEngine leftMapEngine;
    private SimulationEngine rightMapEngine;
    private List<TextField> menuTextFields;
    private SimulationVisualizer leftMapSimulationVisualizer;
    private SimulationVisualizer rightMapSimulationVisualizer;
    private Button leftMapPauseButton;
    private Button rightMapPauseButton;


    private Label addCenteredLabel(GridPane gridPane, String text, int x, int y) {
        Label label = new Label(text);
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.add(label, x, y, 1, 1);
        return label;
    }


    @Override
    public void init() {
    }

    private void renderMenu() {
        gridPaneOfEverything = new GridPane();
        gridPaneOfEverything.getChildren().clear();
        Scene scene = new Scene(gridPaneOfEverything, 1400, 700);
        primaryStage.setScene(scene);

        addParamFieldsToMenu();

        Button buttonStart = new Button("Start!");
        gridPaneOfEverything.add(buttonStart, 0, 7, 1, 1);

        Platform.runLater(primaryStage::show);

        buttonStart.setOnAction(actionEvent -> {
            startASimulation();

            leftMapPauseButton.setOnAction(actionEvent1 -> {
                out.println("lewy nac");
                leftMapEngine.pausePlayButtonPressed();
                leftMapSimulationVisualizer.simulationPaused(leftMapEngine.isPaused());
            });

            rightMapPauseButton.setOnAction(actionEvent1 -> {
                out.println("prawy nac");
                rightMapEngine.pausePlayButtonPressed();
                rightMapSimulationVisualizer.simulationPaused(rightMapEngine.isPaused());
            });
        });
    }

    private void addGripPaneConstraints() {
        gridPaneOfEverything.setMaxHeight(800);
        gridPaneOfEverything.setMaxWidth(1400);
    }

    private void startASimulationEngine(AbstractWorldMap map, SimulationEngine engine,  SimulationVisualizer simulationVisualizer, Button pauseButton, int columnIndex, boolean isLeft){
        VBox middleVBox = new VBox(StatisticsEngine.getLineChart(LineCharts.aliveAnimalsCounter), StatisticsEngine.getLineChart(LineCharts.grassCounter),
                StatisticsEngine.getLineChart(LineCharts.avgEnergy), StatisticsEngine.getLineChart(LineCharts.avgAnimalsLiveSpan),
                StatisticsEngine.getLineChart(LineCharts.avgAnimalsChildrenNumber));
        middleVBox.setMaxWidth(200);
        VBox leftSideVBox;
        if (isLeft) {
            leftSideVBox = new VBox(simulationVisualizer.getSimulationGridPane(), pauseButton, new Label("Dominant Genotype:"),
                    engine.statisticsEngine.getGenotypeLabel(),
                    new Label("Tracker:"), simulationVisualizer.getObservedAnimalVBox());
        }else {
            leftSideVBox = new VBox(simulationVisualizer.getSimulationGridPane(), pauseButton);
        }
        gridPaneOfEverything.add(leftSideVBox,columnIndex,0);

        gridPaneOfEverything.add(middleVBox, 1, 0);
        addGripPaneConstraints();
        engine.addPositionObserver(this);
        engine.addDayObserver(this);

    }

    private void startASimulation() {
        AnimalTracker animalTracker = new AnimalTracker();
        getParamsFromMenuTextFields();
        gridPaneOfEverything.getChildren().clear();
        AbstractWorldMap leftMap = new WrappingMap();
        AbstractWorldMap rightMap = new WalledMap();
        leftMapSimulationVisualizer = new SimulationVisualizer(leftMap, animalTracker);
        rightMapSimulationVisualizer = new SimulationVisualizer(rightMap, animalTracker);
        leftMapPauseButton = new Button("Pause/Play");
        rightMapPauseButton = new Button("Pause/Play");

        leftMapEngine = new SimulationEngine(leftMap, Integer.parseInt(menuTextFields.get(5).getText()), animalTracker);
        rightMapEngine = new SimulationEngine(rightMap, Integer.parseInt(menuTextFields.get(5).getText()), animalTracker);


        startASimulationEngine(leftMap, leftMapEngine, leftMapSimulationVisualizer, leftMapPauseButton,0, true);
        startASimulationEngine(rightMap, rightMapEngine, rightMapSimulationVisualizer, rightMapPauseButton,2, false);

        Thread leftEngineThread = new Thread(leftMapEngine);
        leftEngineThread.start();

        Thread rightEngineThread = new Thread(rightMapEngine);
        rightEngineThread.start();
    }


    private void getParamsFromMenuTextFields() {
        AbstractWorldMap.setHeight(Integer.parseInt(menuTextFields.get(0).getText()));
        AbstractWorldMap.setWidth(Integer.parseInt(menuTextFields.get(1).getText()));
        Animal.setStartEnergy(Integer.parseInt(menuTextFields.get(2).getText()));
        Animal.setMoveEnergy(Integer.parseInt(menuTextFields.get(3).getText()));
        Animal.setPlantEnergy(Integer.parseInt(menuTextFields.get(4).getText()));
        AbstractWorldMap.setJungleRatio(Double.parseDouble(menuTextFields.get(6).getText()));
        out.println("test:");
        for (int i = 0; i < 6; i++) {
            out.println(menuTextFields.get(i).getText());
        }
    }

    private void addParamFieldsToMenu() {
        String[] intParamNames = {"Width", "Height", "Start Energy", "Move Energy", "Plant Energy", "Amount of Animals"};
        menuTextFields = new ArrayList<>();
        Integer[] intParamsDefaults = {30, 30, 100, 1, 100, 10};
        for (int i = 0; i < 6; i++) {
            TextField intParamTextField = new TextField(intParamsDefaults[i].toString());
            gridPaneOfEverything.add(new HBox(new Label(intParamNames[i]), intParamTextField), 0, i, 1, 1);
            menuTextFields.add(intParamTextField);
        }
        TextField jungleParamTextField = new TextField("0.5");
        gridPaneOfEverything.add(new HBox(new Label("Jungle Ratio"), jungleParamTextField), 0, 6, 1, 1);
        menuTextFields.add(jungleParamTextField);


    }

    public void start(Stage primaryStage) {
        gridPaneOfEverything = new GridPane();
        Scene scene = new Scene(gridPaneOfEverything, 1400, 850);
        primaryStage.setScene(scene);
        this.primaryStage = primaryStage;
        Platform.runLater(primaryStage::show);
        Platform.runLater(this::renderMenu);
    }

    @Override
    public void newDayHasCome() {
        Platform.runLater(this::updateView);
        if (!leftMapEngine.isPaused()) {
            Platform.runLater(leftMapEngine.statisticsEngine::newDayHasCome);
        }
        if (!rightMapEngine.isPaused()) {
            Platform.runLater(rightMapEngine.statisticsEngine::newDayHasCome);
        }
    }

    private void updateView() {
        Platform.runLater(primaryStage::show);
        if (!leftMapEngine.isPaused()) {
            Platform.runLater(leftMapSimulationVisualizer);
        }
        if (!rightMapEngine.isPaused()) {
            Platform.runLater(rightMapSimulationVisualizer);
        }

    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        //simulationVisualizer.positionChanged(oldPosition,newPosition,object);
    }
}
