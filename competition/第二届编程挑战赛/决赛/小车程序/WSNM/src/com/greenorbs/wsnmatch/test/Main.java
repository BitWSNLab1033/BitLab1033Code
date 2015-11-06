package com.greenorbs.wsnmatch.test;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;
 
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(final Stage primaryStage) {
        Group root = new Group();
        TextArea textArea = new TextArea();
        Slider speedSlider = new Slider(0, 1,
				0.1);
        speedSlider.setBlockIncrement(0.1);
		speedSlider.setOrientation(Orientation.VERTICAL);
		speedSlider.resizeRelocate(0, 100, 53, 186 + 20);
		
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefWidth(400);
        scrollPane.setPrefHeight(300);

        VBox vBox = new VBox();
        vBox.getChildren().add(scrollPane);
        vBox.setPrefWidth(400);
        vBox.setPrefHeight(300);
        vBox.setLayoutX(10);
        vBox.autosize();
      //  vBox.setScaleY(10);
        
        root.getChildren().add(speedSlider);
        root.setAutoSizeChildren(false);
        
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }

}