package com.greenorbs.wsnmatch;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;

/**
 * 配置信息类
 * @author GC
 *
 */
public class Config {
	//================默认配置
	
	//最小速度
	private int minSpeed = 200;
    //最大速度
	private int maxSpeed = 600;
    //最左角度
	private int minAngle = 3200;
    //最右角度        
	private int maxAngle = 1800;
    //角度中间值
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
