package com.greenorbs.wsnmatch.test;/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
 
import javafx.animation.RotateTransition;
import javafx.animation.RotateTransitionBuilder;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
 
/**
 * A sample in which a node rotates around its center over a given time.
 *
 * @related animation/transitions/FadeTransition
 * @related animation/transitions/FillTransition
 * @related animation/transitions/ParallelTransition
 * @related animation/transitions/PathTransition
 * @related animation/transitions/PauseTransition
 * @related animation/transitions/ScaleTransition
 * @related animation/transitions/SequentialTransition
 * @related animation/transitions/StrokeTransition
 * @related animation/transitions/TranslateTransition
 * @see javafx.animation.RotateTransition
 * @see javafx.animation.RotateTransitionBuilder
 * @see javafx.animation.Transition
 */
public class RotateTransitionSample extends Application {
 
    private RotateTransition rotateTransition;
 
    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 140,140));
 
        Rectangle rect = new Rectangle(20, 20, 100, 100);
        rect.setArcHeight(20);
        rect.setArcWidth(20);
        rect.setFill(Color.ORANGE);
        root.getChildren().add(rect);
        
        rotateTransition = RotateTransitionBuilder.create()
                .node(rect)
                .duration(Duration.seconds(10))
                .fromAngle(0)
                .toAngle(180)
                .cycleCount(Timeline.INDEFINITE)
                .autoReverse(true)
                .build();
    }
 
    public void play() {
        rotateTransition.play();
    }
 
    @Override public void stop() {
        rotateTransition.stop();
    }
 
    public double getSampleWidth() { return 140; }
 
    public double getSampleHeight() { return 140; }
 
    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
        play();
    }
    public static void main(String[] args) { launch(args); }
}