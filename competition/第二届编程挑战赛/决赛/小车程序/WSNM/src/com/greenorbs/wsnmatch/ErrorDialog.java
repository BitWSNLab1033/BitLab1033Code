package com.greenorbs.wsnmatch;

import java.io.File;

import com.fxexperience.javafx.animation.FlipOutXTransition;

import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Loading playlist/music dialog
 * 
 * @author Jasper Potts
 */
public class ErrorDialog {
    public static void loadErrorDialog(Stage owner,String errorInfo) {
            
    	final Stage dialog = new Stage();
        dialog.initOwner(owner);
        
        Group root = new Group();
        
        Scene scene = new Scene(root, 1204, 763);

		scene.setFill(Color.TRANSPARENT);
		
		
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(SceneBuilder.create()
                .fill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.REPEAT, new Stop(0,Color.web("#282828")), new Stop(1,Color.web("#202020"))))
                .root(
                    VBoxBuilder.create()
                        .spacing(20)
                        .padding(new Insets(25))
                        .style("-fx-base: #282828; -fx-background: #282828; -fx-font-size: 1.1em;")
                        .children(
                            new Label(errorInfo),
                                 HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		
                                		ButtonBuilder.create()
                                		.text("È·¶¨")
                                		.defaultButton(true)
                                		.onAction(new EventHandler<ActionEvent>() {
                                		    @Override public void handle(ActionEvent arg0) {
                             		    	                               		    	
                                		        dialog.hide();
                     
                                		    }
                                		})
                                		.build()
                                )
                                .build()
                        )
                        .build()
                        
                )
                
                .build());
        dialog.show();
    }
}


