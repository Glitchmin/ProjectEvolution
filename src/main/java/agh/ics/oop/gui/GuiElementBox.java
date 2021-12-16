package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import agh.ics.oop.Vector2d;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox {
    private Image image;
    private ImageView imageView;
    private VBox vBox;

    public GuiElementBox(IMapElement element, String text) throws FileNotFoundException {
        this.image = new Image(new FileInputStream(element.returnResourcePath()));
        imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        vBox = new VBox(imageView,new Label(text));
        vBox.setPrefHeight(20);
        vBox.setPrefWidth(20);
        vBox.setAlignment(Pos.CENTER);

    }

    public VBox getVBox() {
        return vBox;
    }
}
