package agh.ics.oop.gui;

import agh.ics.oop.AbstractWorldMap;
import agh.ics.oop.Animal;
import agh.ics.oop.SimulationEngine;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class OptionsMenu {

    private List<TextField> menuTextFields;
    private final CheckBox leftMapMagicCheckBox;
    private final CheckBox rightMapMagicCheckBox;

    OptionsMenu() {
        leftMapMagicCheckBox = new CheckBox();
        rightMapMagicCheckBox = new CheckBox();
    }

    public int getStartAnimalsCount() {
        return Integer.parseInt(menuTextFields.get(5).getText());
    }

    public boolean doesLeftMapUseMagicStrategy() {
        return leftMapMagicCheckBox.isSelected();
    }

    public boolean doesRightMapUseMagicStrategy() {
        return rightMapMagicCheckBox.isSelected();
    }

    public void getParamsFromMenuTextFields() {
        AbstractWorldMap.setHeight(Integer.parseInt(menuTextFields.get(0).getText()));
        AbstractWorldMap.setWidth(Integer.parseInt(menuTextFields.get(1).getText()));
        Animal.setStartEnergy(Integer.parseInt(menuTextFields.get(2).getText()));
        Animal.setMoveEnergy(Integer.parseInt(menuTextFields.get(3).getText()));
        Animal.setPlantEnergy(Integer.parseInt(menuTextFields.get(4).getText()));
        SimulationEngine.setMoveDelayMs(Integer.parseInt(menuTextFields.get(6).getText()));
        AbstractWorldMap.setJungleRatio(Double.parseDouble(menuTextFields.get(7).getText()));
        AbstractWorldMap.calculateJungleSize();
    }

    public int addParamFieldsToMenu(GridPane gridPaneOfEverything) {
        int currentRow = 0;
        String[] intParamNames = {"Width: ", "Height: ", "Start energy: ", "Move energy: ", "Plant energy: ", "Amount of animals: ", "Day interval: "};
        String[] intParamTextAfterTextField = {" pixels", " pixels", "", "", "", "", " ms"};
        menuTextFields = new ArrayList<>();
        Integer[] intParamsDefaults = {30, 30, 100, 1, 100, 20, 50};
        for (int i = 0; i < 7; i++) {
            TextField intParamTextField = new TextField(intParamsDefaults[i].toString());
            gridPaneOfEverything.add(new Label(intParamNames[i]), 0, currentRow);
            gridPaneOfEverything.add(intParamTextField, 1, currentRow);
            gridPaneOfEverything.add(new Label(intParamTextAfterTextField[i]), 2, currentRow++);
            menuTextFields.add(intParamTextField);
        }
        TextField jungleParamTextField = new TextField("0.5");
        gridPaneOfEverything.add(new Label("Jungle ratio"), 0, currentRow);
        gridPaneOfEverything.add(jungleParamTextField, 1, currentRow++);
        menuTextFields.add(jungleParamTextField);
        gridPaneOfEverything.add(new Label("Magical strategy for wrapped map: "), 0, currentRow);
        gridPaneOfEverything.add(leftMapMagicCheckBox, 1, currentRow++);
        gridPaneOfEverything.add(new Label("Magical strategy for walled map: "), 0, currentRow);
        gridPaneOfEverything.add(rightMapMagicCheckBox, 1, currentRow++);
        return currentRow;
    }

}
