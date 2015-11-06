package com.greenorbs.wsnmatch.test;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
 
public class hyperlink extends Application {
 
  
    final static String[] captions =new String[]{
        "Products",
        "Education",
        "Partners",
        "Support"
    };
 
    final static String[] urls = new String[]{
       "http://www.oracle.com/us/products/index.html",
       "http://education.oracle.com/",
       "http://www.oracle.com/partners/index.html",
       "http://www.oracle.com/us/support/index.html"
    };
   
    final Hyperlink[] hpls = new Hyperlink[captions.length];
 
    public static void main(String[]args){
        launch(args);
    }
 
    @Override
    public void start(Stage stage) {        

 
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
             
        VBox vvbox = new VBox();
        vvbox.getChildren().addAll(hpls);

        Scene scene = new Scene(vvbox);
        stage.setScene(scene);
        stage.show();
    }
}