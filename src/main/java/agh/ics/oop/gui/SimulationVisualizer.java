package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import static java.lang.System.out;

public class SimulationVisualizer implements Runnable, IPositionChangeObserver {
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
    private final List<Vector2d> positionsToUpdate;
    private final List<VBox> marksList;
    private final AnimalTracker animalTracker;

    private void addGripPaneConstraints() {
        int rowSize = (App.simulationGripPaneWidth / (mapUpperRight.y));
        int columnSize = (App.simulationGripPaneHeight / (mapUpperRight.x));
        for (int y = 0; y < mapUpperRight.y; y++) {
            simulationGridPane.getRowConstraints().add(new RowConstraints(rowSize));
        }

        for (int x = 0; x < mapUpperRight.x; x++) {
            simulationGridPane.getColumnConstraints().add(new ColumnConstraints(columnSize));
        }
    }

    private void addToGuiElementsListMap(Vector2d position, GuiElementBox guiElementBox, boolean isGrass) {
        vBoxListMap.computeIfAbsent(position, k -> new Vector<>());
        simulationGridPane.add(guiElementBox.getVBox(), position.x, position.y, 1, 1);
        guiElementBox.getVBox().setOnMouseClicked(Action -> checkGridPane(guiElementBox.getVBox()));
        if (!isGrass) {
            vBoxListMap.get(position).add(guiElementBox.getVBox());
        }
    }

    SimulationVisualizer(AbstractWorldMap map, AnimalTracker animalTracker) {
        this.marksList = new Vector<>();
        this.animalTracker = animalTracker;
        this.positionsToUpdate = new Vector<>();
        observedAnimalPositionLabel = new Label();
        observedAnimalChildrenLabel = new Label();
        observedAnimalOffspringLabel = new Label();
        observedAnimalDeathLabel = new Label();
        observedAnimalGenomeLabel = new Label();
        observedAnimalVBox = new VBox(observedAnimalPositionLabel, observedAnimalChildrenLabel, observedAnimalOffspringLabel, observedAnimalDeathLabel, observedAnimalGenomeLabel);
        vBoxListMap = new TreeMap<>(Vector2d.xFirstComparator);
        GuiElementBox.setHeight(App.simulationGripPaneHeight / AbstractWorldMap.getHeight());
        GuiElementBox.setWidth(App.simulationGripPaneWidth / AbstractWorldMap.getWidth());
        simulationGridPane = new GridPane();
        simulationGridPane.setGridLinesVisible(true);
        simulationGridPane.setMaxHeight(App.simulationGripPaneHeight);
        simulationGridPane.setMaxWidth(App.simulationGripPaneWidth);
        this.map = map;
        mapUpperRight = new Vector2d(AbstractWorldMap.getWidth(), AbstractWorldMap.getHeight());
        List<IMapElement> mapCopy = map.getCopyOfMapElements();
        addGripPaneConstraints();

        for (IMapElement mapElement : mapCopy) {
            try {
                GuiElementBox guiElementBox = new GuiElementBox(mapElement);
                addToGuiElementsListMap(mapElement.getPosition(), guiElementBox, false);
            } catch (FileNotFoundException ex) {
                out.println(Arrays.toString(ex.getStackTrace()));
            }
        }
        for (int i = AbstractWorldMap.getJungleLowerLeft().x; i <= AbstractWorldMap.getJungleLowerLeft().add(AbstractWorldMap.getJungleSize()).x; i++) {
            for (int j = AbstractWorldMap.getJungleLowerLeft().y; j <= AbstractWorldMap.getJungleLowerLeft().add(AbstractWorldMap.getJungleSize()).y; j++) {
                Vector2d position = new Vector2d(i, j);
                if (AbstractWorldMap.isInsideTheJungle(position)) {
                    try {
                        addToGuiElementsListMap(position, new GuiElementBox("src/main/resources/jungle.png"), true);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void checkGridPane(VBox vBox) {
        for (Vector2d position : vBoxListMap.keySet()) {
            for (VBox vBoxFromList : vBoxListMap.get(position)) {
                if (vBox == vBoxFromList) {
                    if (map.objectsAt(position) != null) {
                        map.mapObjectsHandler.removeTrackingFromAnimals();
                        animalTracker.addAnimal((Animal) map.objectsAt(position).get(0));
                        updateObservedAnimalVBox();
                    }
                }
            }
        }
    }


    public GridPane getSimulationGridPane() {
        return simulationGridPane;
    }

    private void updateObservedAnimalVBox() {
        observedAnimalPositionLabel.setText(animalTracker.getPosition());
        observedAnimalDeathLabel.setText(animalTracker.getDeathDay());
        observedAnimalOffspringLabel.setText(animalTracker.getOffspringCounter());
        observedAnimalChildrenLabel.setText(animalTracker.getChildrenCounter());
        observedAnimalGenomeLabel.setText(animalTracker.getGenotype());

    }

    public void run() {
        List<Vector2d> positionsToUpdateCopy = new Vector<>(positionsToUpdate);
        for (Vector2d position : positionsToUpdateCopy) {
            updateGridPaneNode(position);
        }
        positionsToUpdate.clear();
        for (VBox vBox : marksList) {
            simulationGridPane.getChildren().remove(vBox);
        }
        updateObservedAnimalVBox();
    }

    private void updateGridPaneNode(Vector2d position) {
        if (vBoxListMap.get(position) != null) {
            for (VBox vBox : vBoxListMap.get(position)) {
                simulationGridPane.getChildren().remove(vBox);
            }
            vBoxListMap.get(position).clear();
        }
        if (map.objectsAt(position) != null) {
            List<IMapElement> mapElementList = new Vector<>(map.objectsAt(position));
            for (IMapElement mapElement : mapElementList) {
                try {
                    addToGuiElementsListMap(position, new GuiElementBox(mapElement), false);
                } catch (java.io.FileNotFoundException ex) {
                    out.println(Arrays.toString(ex.getStackTrace()));
                }
            }
        }
    }


    public void markDominantGenotypes(List<Vector2d> positionsList) {
        for (Vector2d position : positionsList) {
            try {
                VBox vBox = new GuiElementBox("src/main/resources/mark.png").getVBox();
                simulationGridPane.add(vBox, position.x, position.y);
                marksList.add(vBox);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public VBox getObservedAnimalVBox() {
        return observedAnimalVBox;
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        if (oldPosition.equals(newPosition)) {
            positionsToUpdate.add(oldPosition);
            return;
        }
        positionsToUpdate.add(oldPosition);
        positionsToUpdate.add(newPosition);
    }

}
