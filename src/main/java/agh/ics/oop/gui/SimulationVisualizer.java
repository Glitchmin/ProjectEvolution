package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import static java.lang.System.out;

public class SimulationVisualizer implements Runnable {
    private final AbstractWorldMap map;
    private final GridPane simulationGridPane;
    private final TreeMap<Vector2d, List<VBox>> vBoxListMap;
    private final Vector2d mapUpperRight;
    private final VBox observedAnimalVBox;
    private final Label observedAnimalPositionLabel;
    private final Label observedAnimalGenomeLabel;
    private final Label observedAnimalChildrenLabel;
    private final Label observedAnimalOffspringLabel;
    private final Label observedAnimalDeathLabel;

    private final AnimalTracker animalTracker;

    private void addGripPaneConstraints() {
        int rowSize=(500 / (mapUpperRight.y));
        int columnSize=(500 / (mapUpperRight.x));
        for (int y = 0; y < mapUpperRight.y; y++) {
            simulationGridPane.getRowConstraints().add(new RowConstraints(rowSize));
        }

        for (int x = 0; x < mapUpperRight.x; x++) {
            simulationGridPane.getColumnConstraints().add(new ColumnConstraints(columnSize));
        }
    }

    private void addToGuiElementsListMap(Vector2d position, GuiElementBox guiElementBox) {
        vBoxListMap.computeIfAbsent(position, k -> new Vector<>());
        simulationGridPane.add(guiElementBox.getVBox(), position.x, position.y, 1, 1);
        guiElementBox.getVBox().setOnMouseClicked(Action -> checkGridPane(guiElementBox.getVBox()));
        vBoxListMap.get(position).add(guiElementBox.getVBox());
    }

    SimulationVisualizer(AbstractWorldMap map, AnimalTracker animalTracker) {
        this.animalTracker = animalTracker;
        observedAnimalPositionLabel = new Label();
        observedAnimalChildrenLabel = new Label();
        observedAnimalOffspringLabel = new Label();
        observedAnimalDeathLabel = new Label();
        observedAnimalGenomeLabel = new Label();
        observedAnimalVBox = new VBox(observedAnimalPositionLabel,observedAnimalChildrenLabel,observedAnimalOffspringLabel,observedAnimalDeathLabel, observedAnimalGenomeLabel);
        vBoxListMap = new TreeMap<>(Vector2d.xFirstComparator);
        GuiElementBox.setHeight(500 / AbstractWorldMap.getHeight());
        GuiElementBox.setWidth(500 / AbstractWorldMap.getWidth());
        simulationGridPane = new GridPane();
        simulationGridPane.setGridLinesVisible(true);
        simulationGridPane.setMaxHeight(500);
        simulationGridPane.setMaxWidth(500);
        this.map = map;
        mapUpperRight = new Vector2d(AbstractWorldMap.getWidth(), AbstractWorldMap.getHeight());
        List<IMapElement> mapCopy = map.getCopyOfMapElements();
        addGripPaneConstraints();

        for (IMapElement mapElement : mapCopy) {
            try {
                GuiElementBox guiElementBox = new GuiElementBox(mapElement);
                addToGuiElementsListMap(mapElement.getPosition(), guiElementBox);
            } catch (java.io.FileNotFoundException ex) {
                out.println(ex);
            }
        }
    }



    private void checkGridPane(VBox vBox){
        for (Vector2d position : vBoxListMap.keySet()){
            for (VBox vBoxFromList: vBoxListMap.get(position)){
                if (vBox == vBoxFromList){
                    if (map.objectsAt(position)!=null) {
                        for (Animal animal: map.getAliveAnimals()){
                            animal.setIsTracked(false);
                        }
                        animalTracker.addAnimal((Animal) map.objectsAt(position).get(0));
                        updateObservedAnimalVBox();
                    }
                }
            }
        }
    }

    public void simulationPaused(boolean isPaused){
        if (isPaused) {

        }
    }

    public GridPane getSimulationGridPane() {
        return simulationGridPane;
    }

    private void updateObservedAnimalVBox(){
        observedAnimalPositionLabel.setText(animalTracker.getPosition());
        observedAnimalDeathLabel.setText(animalTracker.getDeathDay());
        observedAnimalOffspringLabel.setText(animalTracker.getOffspringCounter());
        observedAnimalChildrenLabel.setText(animalTracker.getChildrenCounter());
        observedAnimalGenomeLabel.setText(animalTracker.getGenotype());

    }

    public void run() {
        for (int i = 0; i < AbstractWorldMap.getWidth(); i++) {
            for (int j = 0; j < AbstractWorldMap.getHeight(); j++) {
                updateGridPaneNode(new Vector2d(i, j));
            }
        }
        updateObservedAnimalVBox();
    }

    private void updateGridPaneNode(Vector2d position) {
        if (vBoxListMap.get(position) != null) {
            for (VBox vBox: vBoxListMap.get(position)){
                simulationGridPane.getChildren().remove(vBox);
            }
            vBoxListMap.get(position).clear();
        }
        List<IMapElement> mapElementList = map.objectsAt(position);
        if (mapElementList != null) {
            for (IMapElement mapElement : mapElementList) {
                try {
                    addToGuiElementsListMap(position, new GuiElementBox(mapElement));
                } catch (java.io.FileNotFoundException ex) {
                    out.println(ex);
                }
            }
        }
    }

    public VBox getObservedAnimalVBox() {
        return observedAnimalVBox;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        updateGridPaneNode(oldPosition);
        updateGridPaneNode(newPosition);
    }

}
