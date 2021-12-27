package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;


public class App extends Application implements IDayChangeObserver {

    private GridPane gridPaneOfEverything;
    private Stage primaryStage;
    private SimulationEngine leftMapEngine;
    private SimulationEngine rightMapEngine;
    private List<TextField> menuTextFields;
    private SimulationVisualizer leftMapSimulationVisualizer;
    private SimulationVisualizer rightMapSimulationVisualizer;
    private CheckBox leftMagicCheckBox;
    private CheckBox rightMagicCheckBox;
    private Label leftMagicCounterLabel;
    private Label rightMagicCounterLabel;


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
        gridPaneOfEverything.add(buttonStart, 0, 9, 1, 1);

        Platform.runLater(primaryStage::show);

        buttonStart.setOnAction(actionEvent -> {
            startASimulation();
        });
    }

    private void addGripPaneConstraints() {
        gridPaneOfEverything.setMaxHeight(800);
        gridPaneOfEverything.setMaxWidth(1400);
    }

    private void startASimulationEngine(SimulationEngine engine, SimulationVisualizer simulationVisualizer, int columnIndex, boolean isLeft, Label magicCounterLabel) {
        Button pauseButton = new Button("Pause/Play");
        VBox middleVBox = new VBox(StatisticsEngine.getLineChart(LineCharts.aliveAnimalsCounter), StatisticsEngine.getLineChart(LineCharts.grassCounter),
                StatisticsEngine.getLineChart(LineCharts.avgEnergy), StatisticsEngine.getLineChart(LineCharts.avgAnimalsLiveSpan),
                StatisticsEngine.getLineChart(LineCharts.avgAnimalsChildrenNumber));
        middleVBox.setMaxWidth(400);
        VBox leftSideVBox;
        Button getDominantGenotypeButton = new Button("Show dominant genotype animals");
        leftSideVBox = new VBox(simulationVisualizer.getSimulationGridPane(), new HBox(pauseButton,getDominantGenotypeButton), new Label("Dominant Genotype:"),
                engine.statisticsEngine.getGenotypeLabel(),
                new Label("Tracker:"), simulationVisualizer.getObservedAnimalVBox(), magicCounterLabel);
        gridPaneOfEverything.add(leftSideVBox, columnIndex, 0);

        gridPaneOfEverything.add(middleVBox, 1, 0);
        addGripPaneConstraints();
        engine.addPositionObserver(simulationVisualizer);
        engine.addDayObserver(this);

        pauseButton.setOnAction(actionEvent -> {
            engine.pausePlayButtonPressed();
        });

        getDominantGenotypeButton.setOnAction(actionEvent -> {
            if (engine.isPaused()) {
                simulationVisualizer.markDominantGenotypes(engine.statisticsEngine.getDominantGenotypesPositions());
            }
        });

    }

    private void startASimulation() {
        leftMagicCounterLabel = new Label("");
        rightMagicCounterLabel = new Label("");
        AnimalTracker leftAnimalTracker = new AnimalTracker();
        AnimalTracker rightAnimalTracker = new AnimalTracker();
        getParamsFromMenuTextFields();
        gridPaneOfEverything.getChildren().clear();
        AbstractWorldMap leftMap = new WrappingMap();
        AbstractWorldMap rightMap = new WalledMap();
        leftMapSimulationVisualizer = new SimulationVisualizer(leftMap, leftAnimalTracker);
        rightMapSimulationVisualizer = new SimulationVisualizer(rightMap, rightAnimalTracker);


        leftMapEngine = new SimulationEngine(leftMap, Integer.parseInt(menuTextFields.get(5).getText()), leftAnimalTracker, leftMagicCheckBox.isSelected());
        rightMapEngine = new SimulationEngine(rightMap, Integer.parseInt(menuTextFields.get(5).getText()), rightAnimalTracker, rightMagicCheckBox.isSelected());


        startASimulationEngine(leftMapEngine, leftMapSimulationVisualizer, 0, true, leftMagicCounterLabel);
        startASimulationEngine(rightMapEngine, rightMapSimulationVisualizer, 2, false, rightMagicCounterLabel);


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
        AbstractWorldMap.calculateJungleSize();
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
        leftMagicCheckBox = new CheckBox();
        rightMagicCheckBox = new CheckBox();
        gridPaneOfEverything.add(new HBox(new Label("Magical strategy for wrapped map: "), leftMagicCheckBox), 0, 7);
        gridPaneOfEverything.add(new HBox(new Label("Magical strategy for walled map: "), rightMagicCheckBox), 0, 8);

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
            Platform.runLater(this::updateLeftMagicCounterLabel);
        }
        if (!rightMapEngine.isPaused()) {
            Platform.runLater(rightMapEngine.statisticsEngine::newDayHasCome);
            Platform.runLater(this::updateRightMagicCounterLabel);
        }
    }

    private void updateRightMagicCounterLabel() {
        updateMagicCounterLabel(rightMagicCounterLabel, rightMapEngine);
    }

    private void updateLeftMagicCounterLabel() {
        updateMagicCounterLabel(leftMagicCounterLabel, leftMapEngine);
    }

    private void updateMagicCounterLabel(Label leftMagicCounterLabel, SimulationEngine leftMapEngine) {
        leftMagicCounterLabel.setText("Magic situations left: " + leftMapEngine.getMagicCounter());
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

}
