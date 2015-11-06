package com.greenorbs.wsnmatch;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;

/**
 * ������Ϣ��
 * @author GC
 *
 */
public class Config {
	//================Ĭ������
	
	//��С�ٶ�
	private int minSpeed = 200;
    //����ٶ�
	private int maxSpeed = 600;
    //����Ƕ�
	private int minAngle = 3200;
    //���ҽǶ�        
	private int maxAngle = 1800;
    //�Ƕ��м�ֵ
	private int midAngle = 2550;
	
	private String COM = "COM30";
	public String getCOM() {
		return COM;
	}


	public void setCOM(String cOM) {
		COM = cOM;
	}


	public Config(){
		
	}
	
	
	public  int getMinSpeed() {
		return minSpeed;
	}
	public  void setMinSpeed(int minSpeed) {
		this.minSpeed = minSpeed;
	}
	public  int getMaxSpeed() {
		return maxSpeed;
	}
	public  void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public  int getMinAngle() {
		return minAngle;
	}
	public  void setMinAngle(int minAngle) {
		this.minAngle = minAngle;
	}
	public  int getMaxAngle() {
		return maxAngle;
	}
	public  void setMaxAngle(int maxAngle) {
		this.maxAngle = maxAngle;
	}
	public  int getMidAngle() {
		return midAngle;
	}
	public  void setMidAngle(int midAngle) {
		this.midAngle = midAngle;
	}
	
	
	
	

}
