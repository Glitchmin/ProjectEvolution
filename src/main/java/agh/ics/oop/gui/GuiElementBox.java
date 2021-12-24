package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private Image image;
    private ImageView imageView;
    private VBox vBox;

    public GuiElementBox(IMapElement element, String text) throws FileNotFoundException {
        this.image = new Image(new FileInputStream(element.getResourcePath()));
        imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        if (element instanceof Animal) {
            imageView.setOpacity(((Animal) element).getEnergySaturation());
            text=Double.toString(((Animal) element).getEnergySaturation());
        }
        vBox = new VBox(imageView,new Label(text));
        vBox.setPrefHeight(20);
        vBox.setPrefWidth(20);
        vBox.setAlignment(Pos.CENTER);

    }

    public VBox getVBox() {
        return vBox;
    }
}
