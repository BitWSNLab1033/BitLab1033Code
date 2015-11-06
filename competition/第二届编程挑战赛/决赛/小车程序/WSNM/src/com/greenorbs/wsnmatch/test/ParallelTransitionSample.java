package com.greenorbs.wsnmatch.test;/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
 
 
/**
 * A sample in which various transitions are executed in parallel.
 *
 * @related animation/transitions/FadeTransition
 * @related animation/transitions/FillTransition
 * @related animation/transitions/PathTransition
 * @related animation/transitions/PauseTransition
 * @related animation/transitions/RotateTransition
 * @related animation/transitions/ScaleTransition
 * @related animation/transitions/SequentialTransition
 * @related animation/transitions/StrokeTransition
 * @related animation/transitions/TranslateTransition
 * @see javafx.animation.ParallelTransition
 * @see javafx.animation.ParallelTransitionBuilder
 * @see javafx.animation.Transition
 */
public class ParallelTransitionSample extends Application {
 
    private ParallelTransition parallelTransition;
 
    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 400,150));
        // create rectangle
        Rectangle rect = new Rectangle(-25,-25,50, 50);
        rect.setArcHeight(15);
        rect.setArcWidth(15);
        rect.setFill(Color.CRIMSON);
        rect.setTranslateX(50);
        rect.setTranslateY(75);
        root.getChildren().add(rect); 
        // create parallel transition to do all 4 transitions at the same time        
        parallelTransition = ParallelTransitionBuilder.create()
            .node(rect)
            .children(
                FadeTransitionBuilder.create()
                    .duration(Duration.seconds(3))
                    .node(rect)
                    .fromValue(1)
                    .toValue(0.3)
                    .autoReverse(true)
                    .build(),
                TranslateTransitionBuilder.create()
                    .duration(Duration.seconds(2))
                    .fromX(50)
                    .toX(350)
                    .cycleCount(2)
                    .autoReverse(true)
                    .build(),
                RotateTransitionBuilder.create()
                    .duration(Duration.seconds(3))
                    .byAngle(180)
                    .cycleCount(4)
                    .autoReverse(true)
                    .build(),
                ScaleTransitionBuilder.create()
                    .duration(Duration.seconds(2))
                    .toX(2)
                    .toY(2)
                    .cycleCount(2)
                    .autoReverse(true)
                    .build()
            )
            .cycleCount(Timeline.INDEFINITE)
            .autoReverse(true)
            .build();
         
    }
 
    public void play() {
        parallelTransition.play();
    }
 
    @Override public void stop() {
        parallelTransition.stop();
    }
 
    public double getSampleWidth() { return 400; }
 
    public double getSampleHeight() { return 150; }
 
    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
        play();
    }
    public static void main(String[] args) { launch(args); }
}