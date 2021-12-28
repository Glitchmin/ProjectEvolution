package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class App extends Application implements IDayChangeObserver {

    private GridPane gridPaneOfEverything;
    private Stage primaryStage;
    private SimulationEngine leftMapEngine;
    private SimulationEngine rightMapEngine;
    private SimulationVisualizer leftMapSimulationVisualizer;
    private SimulationVisualizer rightMapSimulationVisualizer;
    private Label leftMagicCounterLabel;
    private Label rightMagicCounterLabel;
    private OptionsMenu optionsMenu;

    private final static int windowWidth = 1400;
    private final static int windowHeight = 800;
    public final static int simulationGripPaneWidth = 500;
    public final static int simulationGripPaneHeight = 500;
    private final static int plotsColumnWidth = 400;


    @Override
    public void init() {
        optionsMenu = new OptionsMenu();
        gridPaneOfEverything = new GridPane();
    }

    private void renderMenu() {
        int lowestGripPaneElement = optionsMenu.addParamFieldsToMenu(gridPaneOfEverything);
        Button startButton = new Button("Start!");
        gridPaneOfEverything.add(startButton, 0, lowestGripPaneElement);
        startButton.setOnAction(actionEvent -> startASimulation());
        Platform.runLater(primaryStage::show);
    }

    private void addGripPaneConstraints() {
        gridPaneOfEverything.setMaxHeight(windowHeight);
        gridPaneOfEverything.setMaxWidth(windowWidth);
    }

    private void startASimulationEngine(SimulationEngine engine, SimulationVisualizer simulationVisualizer, boolean isLeft, Label magicCounterLabel) {
        int columnIndex = 0;
        if (!isLeft) {
            columnIndex = 2;
        }
        Button pauseButton = new Button("Pause/Play");
        VBox middleVBox = new VBox(StatisticsEngine.getLineChart(LineCharts.aliveAnimalsCounter), StatisticsEngine.getLineChart(LineCharts.grassCounter),
                StatisticsEngine.getLineChart(LineCharts.avgEnergy), StatisticsEngine.getLineChart(LineCharts.avgAnimalsLiveSpan),
                StatisticsEngine.getLineChart(LineCharts.avgAnimalsChildrenNumber));

        middleVBox.setMaxWidth(plotsColumnWidth);
        VBox leftSideVBox;
        Button showDominantGenotypeButton = new Button("Show dominant genotype animals");
        Button getDataToFileButton = new Button("Get data to file");
        leftSideVBox = new VBox(simulationVisualizer.getSimulationGridPane(), new HBox(pauseButton, showDominantGenotypeButton, getDataToFileButton), new Label("Dominant Genotype:"),
                engine.statisticsEngine.getGenotypeLabel(),
                new Label("Tracker:"), simulationVisualizer.getObservedAnimalVBox(), magicCounterLabel);
        gridPaneOfEverything.add(leftSideVBox, columnIndex, 0);

        gridPaneOfEverything.add(middleVBox, 1, 0);
        addGripPaneConstraints();
        engine.addPositionObserver(simulationVisualizer);
        engine.addDayObserver(this);

        pauseButton.setOnAction(actionEvent -> engine.pausePlayButtonPressed());

        getDataToFileButton.setOnAction(actionEvent -> {
            if (engine.isPaused()) {
                engine.statisticsEngine.getStatsToFile();
            }
        });

        showDominantGenotypeButton.setOnAction(actionEvent -> {
            if (engine.isPaused()) {
                simulationVisualizer.markDominantGenotypes(engine.statisticsEngine.getDominantGenotypesPositions());
            }
        });

    }

    private void startASimulation() {
        optionsMenu.getParamsFromMenuTextFields();
        gridPaneOfEverything.getChildren().clear();

        leftMagicCounterLabel = new Label();
        rightMagicCounterLabel = new Label();

        AnimalTracker leftAnimalTracker = new AnimalTracker();
        AnimalTracker rightAnimalTracker = new AnimalTracker();

        AbstractWorldMap leftMap = new WrappingMap();
        AbstractWorldMap rightMap = new WalledMap();

        leftMapSimulationVisualizer = new SimulationVisualizer(leftMap, leftAnimalTracker);
        rightMapSimulationVisualizer = new SimulationVisualizer(rightMap, rightAnimalTracker);

        leftMapEngine = new SimulationEngine(leftMap, optionsMenu.getStartAnimalsCount(), leftAnimalTracker, optionsMenu.doesLeftMapUseMagicStrategy(), "WrappedMap");
        rightMapEngine = new SimulationEngine(rightMap, optionsMenu.getStartAnimalsCount(), rightAnimalTracker, optionsMenu.doesRightMapUseMagicStrategy(), "WalledMap");

        startASimulationEngine(leftMapEngine, leftMapSimulationVisualizer, true, leftMagicCounterLabel);
        startASimulationEngine(rightMapEngine, rightMapSimulationVisualizer, false, rightMagicCounterLabel);

        Thread leftEngineThread = new Thread(leftMapEngine);
        leftEngineThread.start();

        Thread rightEngineThread = new Thread(rightMapEngine);
        rightEngineThread.start();
    }


    public void start(Stage primaryStage) {
        Scene scene = new Scene(gridPaneOfEverything, windowWidth, windowHeight);
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
