package com.greenorbs.wsnmatch.test;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TestGroup2 extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		Group g = new Group();
		Color[] c = new Color[]{Color.RED,Color.ANTIQUEWHITE,Color.AQUA,
				Color.AQUAMARINE,Color.AZURE};
		DropShadow ds = new DropShadow();
		ds.setWidth(30);
		for (int i = 0; i < 5; i++) {
		    Rectangle r = new Rectangle();
		    r.setY(i * 20);
		    r.setWidth(100);
		    r.setHeight(10);
		    r.setFill(c[i]);
//		    r.setEffect(ds);//�ܹ�������������ЧӦ����Group�ϣ���Ч�ĳ��ȿ���ǲ�ͳ�Ƶģ�������ЧҲ��Ӧ���ں�����
		    g.getChildren().add(r);
		}


		g.setEffect(ds);//��Ч��Ӧ�õ����еĺ�����
		stage.setScene(new Scene(g));
		stage.show();
		Bounds bounds = g.getLayoutBounds();
		System.out.println(bounds.getHeight());//90 108 
		System.out.println(bounds.getWidth());//100 128
		
	}
	public static void main(String[] args) {
		launch(args);
	}
	

}