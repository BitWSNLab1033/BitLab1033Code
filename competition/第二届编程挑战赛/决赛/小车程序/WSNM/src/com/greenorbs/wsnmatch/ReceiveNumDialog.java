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
public class ReceiveNumDialog {
    public static void loadPlayList(Stage owner,final Circle circle) {
//        System.out.println("existingUrl = " + playList.getUrl());
//        String url = playList.getUrl();
        final TextField tagId1 = TextFieldBuilder.create()
            .text(WSNMatch.tagIdList[0]+"")
            .prefWidth(200)            
            .style("-fx-font-size: 0.9em;")
            .build();
        final TextField tagId2 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[1]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        final TextField tagId3 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[2]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId4 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[3]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId5 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[4]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId6 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[5]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId7 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[6]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId8 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[7]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId9 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[8]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId10 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[9]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId11 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[10]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId12 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[11]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId13 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[12]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId14 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[13]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField tagId15 = TextFieldBuilder.create()
                .text(WSNMatch.tagIdList[14]+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        HBox.setHgrow(tagId1, Priority.ALWAYS);
        
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
                            new Label("TAG INFORMATION"),
                            
                            HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("Tag ID 1:"),
                                		tagId1
                                )
                                .build()
                                ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("Tag ID 2:"),
                                		tagId2
                                )
                                .build()
                                 ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("Tag ID 3:"),
                                		tagId3
                                )
                                .build()
                                  ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("Tag ID 4:"),
                                		tagId4
                                )
                                .build()
                                  ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("Tag ID 5:"),
                                		tagId5
                                )
                                .build()
                                 ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("Tag ID 6:"),
                                		tagId6
                                )
                                .build()
                                   ,
                                HBoxBuilder.create()
                                   .spacing(10)
                                   .alignment(Pos.CENTER_RIGHT)
                                   .children(
                                   		new Label("Tag ID 7:"),
                                   		tagId7
                                   )
                                   .build()
                                      ,
                                      HBoxBuilder.create()
                                      .spacing(10)
                                      .alignment(Pos.CENTER_RIGHT)
                                      .children(
                                      		new Label("Tag ID 8:"),
                                      		tagId8
                                      )
                                      .build()
                                         ,
                                         HBoxBuilder.create()
                                         .spacing(10)
                                         .alignment(Pos.CENTER_RIGHT)
                                         .children(
                                         		new Label("Tag ID 9:"),
                                         		tagId9
                                         )
                                         .build()
                                            ,
                                            HBoxBuilder.create()
                                            .spacing(10)
                                            .alignment(Pos.CENTER_RIGHT)
                                            .children(
                                            		new Label("Tag ID 10:"),
                                            		tagId10
                                            )
                                            .build()
                                               ,
                                               HBoxBuilder.create()
                                               .spacing(10)
                                               .alignment(Pos.CENTER_RIGHT)
                                               .children(
                                               		new Label("Tag ID 11:"),
                                               		tagId11
                                               )
                                               .build()
                                                  ,
                                                  HBoxBuilder.create()
                                                  .spacing(10)
                                                  .alignment(Pos.CENTER_RIGHT)
                                                  .children(
                                                  		new Label("Tag ID 12:"),
                                                  		tagId12
                                                  )
                                                  .build()
                                                     ,
                                                     HBoxBuilder.create()
                                                     .spacing(10)
                                                     .alignment(Pos.CENTER_RIGHT)
                                                     .children(
                                                     		new Label("Tag ID 13:"),
                                                     		tagId13
                                                     )
                                                     .build()
                                                        ,
                                                        HBoxBuilder.create()
                                                        .spacing(10)
                                                        .alignment(Pos.CENTER_RIGHT)
                                                        .children(
                                                        		new Label("Tag ID 14:"),
                                                        		tagId14
                                                        )
                                                        .build()
                                                           ,
                                                           HBoxBuilder.create()
                                                           .spacing(10)
                                                           .alignment(Pos.CENTER_RIGHT)
                                                           .children(
                                                           		new Label("Tag ID 15:"),
                                                           		tagId15
                                                           )
                                                           .build()
                                                              ,
                                 HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		ButtonBuilder.create()
                                		.text("取消")
                                		.cancelButton(true)
                                		.onAction(new EventHandler<ActionEvent>() {
                                		    @Override public void handle(ActionEvent arg0) {
                                		        dialog.hide();
                                		      //将按钮再弹起来
                                		        circle.setEffect(null);
                                		    }
                                		})
                                		.build(),
                                		ButtonBuilder.create()
                                		.text("确定")
                                		.defaultButton(true)
                                		.onAction(new EventHandler<ActionEvent>() {
                                		    @Override public void handle(ActionEvent arg0) {
                                		        
                                		        //保存配置信息
                                		    	int num_tagId1 = Integer.parseInt(tagId1.getText());
                                		    	int num_tagId2 = Integer.parseInt(tagId2.getText());
                                		    	int num_tagId3 = Integer.parseInt(tagId3.getText());
                                		    	int num_tagId4 = Integer.parseInt(tagId4.getText());
                                		    	int num_tagId5 = Integer.parseInt(tagId5.getText());
                                		    	int num_tagId6 = Integer.parseInt(tagId6.getText());
                                		    	int num_tagId7 = Integer.parseInt(tagId7.getText());
                                		    	int num_tagId8 = Integer.parseInt(tagId8.getText());
                                		    	int num_tagId9 = Integer.parseInt(tagId9.getText());
                                		    	int num_tagId10 = Integer.parseInt(tagId10.getText());
                                		    	int num_tagId11 = Integer.parseInt(tagId11.getText());
                                		    	int num_tagId12 = Integer.parseInt(tagId12.getText());
                                		    	int num_tagId13 = Integer.parseInt(tagId13.getText());
                                		    	int num_tagId14 = Integer.parseInt(tagId14.getText());
                                		    	int num_tagId15 = Integer.parseInt(tagId15.getText());
                                		    	//配置小车参数
                                		    	WSNMatch.tagIdList[0] = num_tagId1;
                                		    	WSNMatch.tagIdList[1] = num_tagId2;
                                		    	WSNMatch.tagIdList[2] = num_tagId3;
                                		    	WSNMatch.tagIdList[3] = num_tagId4;
                                		    	WSNMatch.tagIdList[4] = num_tagId5;
                                		    	WSNMatch.tagIdList[5] = num_tagId6;
                                		    	WSNMatch.tagIdList[6] = num_tagId7;
                                		    	WSNMatch.tagIdList[7] = num_tagId8;
                                		    	WSNMatch.tagIdList[8] = num_tagId9;
                                		    	WSNMatch.tagIdList[9] = num_tagId10;
                                		    	WSNMatch.tagIdList[10] = num_tagId11;
                                		    	WSNMatch.tagIdList[11] = num_tagId12;
                                		    	WSNMatch.tagIdList[12] = num_tagId13;
                                		    	WSNMatch.tagIdList[13] = num_tagId14;
                                		    	WSNMatch.tagIdList[14] = num_tagId15;
                                		                                		
                                		    	
                                		        dialog.hide();
                                		        
                                		        //将按钮再弹起来
                                		        circle.setEffect(null);
                                		        /**
                                				 * 初始化串口
                                				 */
                                		      //重新配置的时候将串口先关闭，否则在此打开，会直接关闭
                            					
                                		        
                                		        
                                		    }
                                		})
                                		.build()
                                )
                                .build()
                        )
                        .build()
                        
                )
                
                .build());
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				//将按钮再弹起来
                circle.setEffect(null);
			}
		});
        dialog.show();
    }
}


