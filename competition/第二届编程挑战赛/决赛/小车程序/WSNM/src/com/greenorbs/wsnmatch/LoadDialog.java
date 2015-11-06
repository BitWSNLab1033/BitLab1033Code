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
public class LoadDialog {
    public static void loadPlayList(Stage owner,final Circle circle) {
//        System.out.println("existingUrl = " + playList.getUrl());
//        String url = playList.getUrl();
        final TextField minSpeedField = TextFieldBuilder.create()
            .text(WSNMatch.getInstance().getConf().getMinSpeed()+"")
            .prefWidth(200)            
            .style("-fx-font-size: 0.9em;")
            .build();
        final TextField maxSpeedField = TextFieldBuilder.create()
                .text(WSNMatch.getInstance().getConf().getMaxSpeed()+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        final TextField minAngleField = TextFieldBuilder.create()
                .text(WSNMatch.getInstance().getConf().getMinAngle()+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField maxAngleField = TextFieldBuilder.create()
                .text(WSNMatch.getInstance().getConf().getMaxAngle()+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField midAngleField = TextFieldBuilder.create()
                .text(WSNMatch.getInstance().getConf().getMidAngle()+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        final TextField comField = TextFieldBuilder.create()
                .text(WSNMatch.getInstance().getConf().getCOM()+"")
                .prefWidth(200)            
                .style("-fx-font-size: 0.9em;")
                .build();
        
        HBox.setHgrow(minSpeedField, Priority.ALWAYS);
        
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
                            new Label("config information"),
                            
                            HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("��С�ٶȣ�"),
                                		minSpeedField
                                )
                                .build()
                                ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("����ٶȣ�"),
                                		maxSpeedField
                                )
                                .build()
                                 ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("����Ƕȣ�"),
                                		minAngleField
                                )
                                .build()
                                  ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("���ҽǶȣ�"),
                                		maxAngleField
                                )
                                .build()
                                  ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("�м�Ƕȣ�"),
                                		midAngleField
                                )
                                .build()
                                 ,
                                  HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		new Label("�˿ڣ�"),
                                		comField
                                )
                                .build()
                                   ,
                                 HBoxBuilder.create()
                                .spacing(10)
                                .alignment(Pos.CENTER_RIGHT)
                                .children(
                                		ButtonBuilder.create()
                                		.text("ȡ��")
                                		.cancelButton(true)
                                		.onAction(new EventHandler<ActionEvent>() {
                                		    @Override public void handle(ActionEvent arg0) {
                                		        dialog.hide();
                                		      //����ť�ٵ�����
                                		        circle.setEffect(null);
                                		    }
                                		})
                                		.build(),
                                		ButtonBuilder.create()
                                		.text("ȷ��")
                                		.defaultButton(true)
                                		.onAction(new EventHandler<ActionEvent>() {
                                		    @Override public void handle(ActionEvent arg0) {
                                		        
                                		        //����������Ϣ
                                		    	int mins = Integer.parseInt(minSpeedField.getText());
                                		    	int maxs = Integer.parseInt(maxSpeedField.getText());
                                		    	int mina = Integer.parseInt(minAngleField.getText());
                                		    	int maxa = Integer.parseInt(maxAngleField.getText());
                                		    	int mida = Integer.parseInt(midAngleField.getText());
                                		    	//����С������
                                		    	WSNMatch.getInstance().getConf().setMaxAngle(maxa);
                                		    	WSNMatch.getInstance().getConf().setMaxSpeed(maxs);
                                		    	WSNMatch.getInstance().getConf().setMinAngle(mina);
                                		    	WSNMatch.getInstance().getConf().setMinSpeed(mins);
                                		    	WSNMatch.getInstance().getConf().setMidAngle(mida);
                                		    	WSNMatch.getInstance().getConf().setCOM(comField.getText());
                                		                                		
                                		    	WSNMatch.getInstance().writeConf();
                                		        dialog.hide();
                                		        
                                		        //����ť�ٵ�����
                                		        circle.setEffect(null);
                                		        /**
                                				 * ��ʼ������
                                				 */
                                		      //�������õ�ʱ�򽫴����ȹرգ������ڴ˴򿪣���ֱ�ӹر�
                            					if(WSNMatch.getInstance().getSerial()!=null){
                            						WSNMatch.getInstance().getSerial().getPhoenix().shutdown();
                            					}
                                		        WSNMatch.getInstance().initSerial();
                                		        WSNMatch.getInstance().Labelflush();
                                		        //
                                		        WSNMatch.getInstance().Stop();
//                                		        playList.load(urlField.getText());
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
				//����ť�ٵ�����
                circle.setEffect(null);
			}
		});
        dialog.show();
    }
}


