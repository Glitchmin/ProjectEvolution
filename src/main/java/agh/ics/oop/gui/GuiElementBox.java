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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiElementBox {
    private final VBox vBox;
    static private int width;
    static private int height;
    static Map<String, Image> imagesMap=new HashMap<>();

    public static void setWidth(int width) {
        GuiElementBox.width = width;
    }

    public static void setHeight(int height) {
        GuiElementBox.height = height;
    }

    public GuiElementBox(IMapElement element) throws FileNotFoundException {
        Image image;
        if (imagesMap.get(element.getResourcePath())!=null){
            image=imagesMap.get(element.getResourcePath());
        }else {
            image = new Image(new FileInputStream(element.getResourcePath()));
            imagesMap.put(element.getResourcePath(), image);
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        vBox = new VBox(imageView);
        vBox.setPrefHeight(width);
        vBox.setPrefWidth(height);
        vBox.setAlignment(Pos.CENTER);

    }

    public VBox getVBox() {
        return vBox;
    }
}
