package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.IMapElement;
import agh.ics.oop.Vector2d;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import static java.lang.System.out;

public class SimulationVisualizer implements Runnable {
    private final AbstractWorldMap map;
    private final GridPane simulationGridPane;
    private final TreeMap<Vector2d, List<VBox>> vBoxListMap;
    private final Vector2d mapUpperRight;

    private void addGripPaneConstraints() {
        for (int y = 0; y < mapUpperRight.y; y++) {
            simulationGridPane.getRowConstraints().add(new RowConstraints((Integer) (500 / (mapUpperRight.x))));
        }

        for (int x = 0; x < mapUpperRight.x; x++) {
            simulationGridPane.getColumnConstraints().add(new ColumnConstraints((Integer) (500 / (mapUpperRight.y))));
        }
    }

    private void addToGuiElementsListMap(Vector2d position, GuiElementBox guiElementBox) {
        vBoxListMap.computeIfAbsent(position, k -> new Vector<>());
        simulationGridPane.add(guiElementBox.getVBox(), position.x, position.y, 1, 1);
        vBoxListMap.get(position).add(guiElementBox.getVBox());
    }

    SimulationVisualizer(AbstractWorldMap map) {
        vBoxListMap = new TreeMap<>(Vector2d.xFirstComparator);
        GuiElementBox.setHeight(500 / AbstractWorldMap.getHeight());
        GuiElementBox.setWidth(500 / AbstractWorldMap.getWidth());
        simulationGridPane = new GridPane();
        simulationGridPane.setGridLinesVisible(true);
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

    public GridPane getSimulationGridPane() {
        return simulationGridPane;
    }

    public void run() {
        for (int i = 0; i < AbstractWorldMap.getWidth(); i++) {
            for (int j = 0; j < AbstractWorldMap.getHeight(); j++) {
                updateGridPaneNode(new Vector2d(i, j));
            }
        }
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

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement object) {
        updateGridPaneNode(oldPosition);
        updateGridPaneNode(newPosition);
    }
}
