package com.greenorbs.wsnmatch.test;/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
import javafx.application.Application;import javafx.scene.Group;import javafx.scene.Scene;import javafx.stage.Stage;import javafx.animation.KeyFrame;import javafx.animation.KeyValue;import javafx.animation.Timeline;import javafx.beans.InvalidationListener;import javafx.beans.Observable;import javafx.event.ActionEvent;import javafx.event.EventHandler;import javafx.geometry.Insets;import javafx.geometry.Pos;import javafx.scene.Node;import javafx.scene.control.Button;import javafx.scene.control.CheckBox;import javafx.scene.control.Label;import javafx.scene.effect.Lighting;import javafx.scene.layout.HBox;import javafx.scene.layout.VBox;import javafx.scene.paint.Color;import javafx.scene.shape.Circle;import javafx.scene.shape.Rectangle;import javafx.scene.text.Text;import javafx.scene.text.TextBoundsType;import javafx.scene.transform.Rotate;import javafx.util.Duration;
 
/**
 * A sample that demonstrates the basics of timeline creation. 
 *
 * @see javafx.animation.KeyFrame
 * @see javafx.animation.KeyValue
 * @see javafx.animation.Timeline
 * @see javafx.util.Duration
 */
public class TimelineSample extends Application {
 
    Timeline timeline;
 
    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 600,500));
 
        //create a circle
        final Circle circle = new Circle(500,200, 20,  Color.web("1c89f4"));        final Circle circle1 = new Circle(500,200, 20,  Color.web("1c89f4"));        final Circle circle2 = new Circle(500,200, 20,  Color.web("1c89f4"));                       // circle.setOpacity(0);       // circle2.setOpacity(0);       // circle1.setOpacity(0);
        circle.setEffect(new Lighting());
        Label numberLabel0 = new Label(0+"");        numberLabel0.setOpacity(1);        numberLabel0.relocate(0, 190);		numberLabel0.resize(20, 20);		Label numberLabel1 = new Label(1+"");        numberLabel1.setOpacity(1);        numberLabel1.relocate(0, 190);		numberLabel1.resize(20, 20);				Label numberLabel2 = new Label(2+"");        numberLabel2.setOpacity(1);        numberLabel2.relocate(0, 190);		numberLabel2.resize(20, 20);
        //create a timeline for moving the circle
        timeline = new Timeline();        
      //  timeline.setCycleCount(Timeline.INDEFINITE);
       // timeline.setAutoReverse(true);
         Rotate  rotate = new Rotate(0,150,150);        Rotate  rotate1 = new Rotate(0,150,150);        Rotate  rotate2 = new Rotate(0,150,150);                final Circle circlecenter = new Circle(150,150, 2,  Color.web("1c89f4"));        final Circle sqcenter = new Circle(150,340, 2,  Color.web("1c89f4"));                timeline.getKeyFrames().addAll(                new KeyFrame(Duration.millis(0),                        new KeyValue(circle.opacityProperty(), 1),                    new KeyValue(rotate.angleProperty(), 0)                ),                new KeyFrame(Duration.millis(3000),                        new KeyValue(circle.opacityProperty(), 1),                    new KeyValue(rotate.angleProperty(), 180)                ),                new KeyFrame(Duration.millis(1000),                            new KeyValue(circle1.opacityProperty(), 1),                        new KeyValue(rotate1.angleProperty(), 0)                    )                ,                new KeyFrame(Duration.millis(3000),                            new KeyValue(circle1.opacityProperty(), 1),                        new KeyValue(rotate1.angleProperty(), 90)                    )                                ,                new KeyFrame(Duration.millis(2000),                            new KeyValue(circle2.opacityProperty(), 1),                        new KeyValue(rotate2.angleProperty(), 0)                    )                ,                new KeyFrame(Duration.millis(3000),                            new KeyValue(circle2.opacityProperty(), 1),                        new KeyValue(rotate2.angleProperty(), 0)                    )                            );        circle.getTransforms().add(rotate);        circle1.getTransforms().add(rotate1);        circle2.getTransforms().add(rotate2);
         numberLabel0.getTransforms().add(rotate);        numberLabel1.getTransforms().add(rotate1);        numberLabel2.getTransforms().add(rotate2);
        root.getChildren().add(createNavigation());        root.getChildren().add(numberLabel0);        root.getChildren().add(numberLabel1);        root.getChildren().add(numberLabel2);
        root.getChildren().add(circle);        root.getChildren().add(circlecenter);        root.getChildren().add(sqcenter);        root.getChildren().add(circle2);        root.getChildren().add(circle1);         
    }
 
    @Override public void stop() {
        timeline.stop();
    }
 
    private VBox createNavigation() {
        //method for creating navigation panel
        //start/stop/pause/play from start buttons
        Button buttonStart = new Button("Start");
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //start timeline
                timeline.play();
            }
        });
        Button buttonStop = new Button("Stop");
        buttonStop.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //stop timeline
                timeline.stop();
            }
        });
        Button buttonPlayFromStart = new Button("Play From Start");
        buttonPlayFromStart.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //play from start
                timeline.playFromStart();
            }
        });
        Button buttonPause = new Button("Pause");
        buttonPause.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                //pause from start
                timeline.pause();
            }
        });
        //text showing current time
        final Text currentRateText = new Text("Current time: 0 ms" );
        currentRateText.setBoundsType(TextBoundsType.VISUAL);
        timeline.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                int time = (int) timeline.getCurrentTime().toMillis();
                currentRateText.setText("Current time: " + time + " ms");
            }
        });
        //Autoreverse checkbox
        final CheckBox checkBoxAutoReverse = new CheckBox("Auto Reverse");
        checkBoxAutoReverse.setSelected(true);
        checkBoxAutoReverse.selectedProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                timeline.setAutoReverse(checkBoxAutoReverse.isSelected());
            }
        });
        //add all navigation to layout
        HBox hBox1 = new HBox(10);
        hBox1.setPadding(new Insets(0, 0, 0, 5));
        hBox1.getChildren().addAll(buttonStart, buttonPause, buttonStop, buttonPlayFromStart);
        hBox1.setAlignment(Pos.CENTER_LEFT);
        HBox hBox2 = new HBox(10);
        hBox2.setPadding(new Insets(0, 0, 0, 35));
        hBox2.getChildren().addAll( checkBoxAutoReverse, currentRateText);
        hBox2.setAlignment(Pos.CENTER_LEFT);
        VBox vBox = new VBox(10);
        vBox.setLayoutY(60);
        vBox.getChildren().addAll(hBox1, hBox2);
        return vBox;
    }
 
    public double getSampleWidth() { return 280; }
 
    public double getSampleHeight() { return 120; }
 
    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}