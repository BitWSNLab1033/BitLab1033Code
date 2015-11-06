package com.greenorbs.wsnmatch.test;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

public class WSNMatch extends Application {
	 final static String[] captions =new String[]{
	        "greenorbs",
	        "Education",
	        "Partners",
	        "Support"
	    };
	 
	    final static String[] urls = new String[]{
	       "http://www.greenorbs.org/",
	       "http://education.oracle.com/",
	       "http://www.oracle.com/partners/index.html",
	       "http://www.oracle.com/us/support/index.html"
	    };
	   
	    final Hyperlink[] hpls = new Hyperlink[captions.length];
	    
	    /**
	     * 添加超链接
	     */
	    
	    
	    Timeline timeline;
	    
	    public void initHyperlink(){
	    	//==============================================hyperlink
			for (int i = 0; i <captions.length; i++) {
	            final Hyperlink hpl =hpls[i] = new Hyperlink(captions[i]);
	            hpl.setFont(Font.font("Arial",14));
	            final String url =urls[i];
	 
	            hpl.setOnAction(new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(ActionEvent e) {
	                  // webEngine.load(url);
	               	// 判断当前系统是否支持Java AWT Desktop扩展
	           		if (java.awt.Desktop.isDesktopSupported()) {
	           			try {
	           				// 创建一个URI实例
	           				java.net.URI uri = java.net.URI.create(url);
	           				// 获取当前系统桌面扩展
	           				java.awt.Desktop dp = java.awt.Desktop.getDesktop();
	           				// 判断系统桌面是否支持要执行的功能
	           				if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
	           					// 获取系统默认浏览器打开链接
	           					dp.browse(uri);
	           				}
	           			} catch (java.lang.NullPointerException e1) {
	           				// 此为uri为空时抛出异常
	           			} catch (java.io.IOException e1) {
	           				// 此为无法获取系统默认浏览器
	           			}
	           		}
	                }
	            });
	        }
	             
	        
	    }
	    
	@Override
	public void start(Stage primaryStage) {
		try {
			Group root = new Group();
			
			
			ImageView background = new ImageView(
					new Image(
							WSNMatch.class
									.getResourceAsStream("img/bg.png")));
			
			//小车上下左右控制按钮
			Button leftBtn = new Button();
			Button rightBtn = new Button();
			Button upBtn = new Button();
			Button downBtn = new Button();
			
			
			
			leftBtn.resizeRelocate(813, 208, 50, 50);
			rightBtn.resizeRelocate(916, 208, 50, 50);
			upBtn.resizeRelocate(863, 160, 50, 50);
			downBtn.resizeRelocate(863, 261, 50, 50);
			
			final TextArea textArea = new TextArea();
			textArea.setEditable(false);
			

		  
	         
	        ScrollPane scrollPane = new ScrollPane();
	        scrollPane.setContent(textArea);
	        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
	        scrollPane.setFitToWidth(true);
	        scrollPane.setFitToHeight(true);
	        scrollPane.setPrefSize(575, 325);
	        
			

	        VBox vBox = new VBox();
	        vBox.getChildren().add(scrollPane);
	        
	        vBox.setPrefSize(575, 325);
	        vBox.setLayoutX(209);
	        vBox.setLayoutY(95);
	        vBox.autosize();	        
			

			
			leftBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println();
					textArea.appendText("向左\n");
				}
			});
			
			rightBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println();
					textArea.appendText("向右\n");
				}
			});
			upBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println();
					textArea.appendText("向上\n");
				}
			});
			downBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println();
					textArea.appendText("向下\n");
				}
			});
			
			initHyperlink();
			
			VBox Hyperlinkvbox = new VBox();
	        Hyperlinkvbox.getChildren().addAll(hpls);
			
	        Hyperlinkvbox.setPrefSize(140, 315);
	        Hyperlinkvbox.setLayoutX(60);
	        Hyperlinkvbox.setLayoutY(110);
	        Hyperlinkvbox.autosize();
			
	      //=======================

		        //create a timeline for moving the circle

		        timeline = new Timeline();        

		        timeline.setCycleCount(Timeline.INDEFINITE);

		        timeline.setAutoReverse(true);
		      

		        //add the following keyframes to the timeline

		        timeline.getKeyFrames().addAll

		            (new KeyFrame(Duration.ZERO,

		                          new KeyValue(hpls[0].translateYProperty(), 0),
		                          new KeyValue(hpls[1].translateYProperty(), 0),
		                          new KeyValue(hpls[2].translateYProperty(), 0),
		            			  new KeyValue(hpls[3].translateYProperty(), 0)
		            ),
		                       

		             new KeyFrame(new Duration(40000),
		            		 
		                          new KeyValue(hpls[0].translateYProperty(), 205),
		                          new KeyValue(hpls[2].translateYProperty(), 205),
		                          new KeyValue(hpls[3].translateYProperty(), 205),
		                          new KeyValue(hpls[1].translateYProperty(), 205)
		             ));
			
		        timeline.play();
		        //=========================
	        
		        
	        root.getChildren().addAll(background,vBox,Hyperlinkvbox,leftBtn,rightBtn,upBtn,downBtn);
			//root.getChildren().add(vBox);
			root.setAutoSizeChildren(false);
			
			
			
			
	        Scene scene = new Scene(root,1004,607);
			
			scene.setFill(Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
