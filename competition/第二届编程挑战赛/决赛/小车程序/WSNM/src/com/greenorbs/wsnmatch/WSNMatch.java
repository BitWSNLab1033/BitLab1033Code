package com.greenorbs.wsnmatch;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.tinyos.util.Dump;

import com.fxexperience.javafx.animation.FadeOutTransition;
import com.fxexperience.javafx.animation.FlipOutXTransition;
import com.greenorbs.wsnmatch.util.CRC_16;
import com.greenorbs.wsnmatch.util.Serial;
import com.sun.javafx.scene.control.behavior.KeyBinding;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;

public class WSNMatch extends Application {
	
	public WSNMatch()
	{
		for(int i = 0; i<15; i++)
			tagIdList[i] = 100 + i;
	}
	
	//两条命令之间的间隔时间
	private static int TIME_DELAY = 2000;
	
	static WSNMatch instance;
	
	public static int[] tagIdList = new int[15];
	

	// 当前query的Tag的id
	private int currentTagId = -1;

	// 是否配置过了
	private boolean isConf = false;
	// 配置信息
	private Config conf = new Config();
	// 串口数据收发类
	private Serial serial;

	// private String TagInfoString = "";
	// 数据解析的标签
	private Label dataLabel;

	// 记录当前行驶方向，0-向前，1-向后，默认向前
	private int orientState = 0;
	// 小车使能状态，0-不能，1 - 能
	private int state = 0;
	//记录上次的发命令时间
	private long lastTime = 1L;
	
	//是否第一次发送命令
	private int angleFirstTime = 1;
	private int forwardFirstTime = 1;
	private int backFirstTime = 1;
	
	private long anglelastTime = 1L;
	private long forwardlastTime = 1L;
	private long backlastTime = 1L;

	//小车角度
	private int angle = 1000;
	
	//小车直行角度
	private int midAngle = 2600;
	//小车左角度
	private int leftAngle = 3100;
	//小车右角度
	private int rightAngle = 1800;
	
	private Label[] numberLabel = new Label[10];

	private Stage stage = null;

	String[] captions = new String[] { "主管单位：教育部科技发展中心", "主办单位：互联网应用创新开放平台联盟",
			"承办单位：清华大学软件学院", "协办单位：清华信息科学与技术国家实验室物联网技术中心",
			"协办单位：ACM中国理事会(ACM-China)", "全国高校物联网应用创新大赛-赛事官方网址" };

	String[] urls = new String[] { "http://www.cutech.edu.cn/cn/index.htm",
			"http://www.iiu.edu.cn/", "http://www.thss.tsinghua.edu.cn/",
			"http://www.tsinghua.edu.cn/publish/cs/4796/",
			"http://china.acm.org/", "http://iotcompetition.org/index.html" };

	Hyperlink[] hpls = new Hyperlink[captions.length];

	Circle[] circle = new Circle[10];
	Rotate[] rotateCircle = new Rotate[10];
	Rotate[] rotateRect = new Rotate[10];
	/**
	 * 添加超链接
	 */

	// 超链接的动画效果
	Timeline HyperlinkTimeline, Tagtimeline;
	// 显示屏上的标签
	private Label upOrdownLabel, speedLabel, angleLabel, stopOrStart,
			queryLabel;

	// 15个tag信息包的标签
	private Label[] tagInfoLabel = new Label[15];

	// 速度标签，角度标签
	private Label speedMinLabel;
	private Label speedMaxLabel;
	private Label angleMinLabel;
	private Label angleMaxLabel;
	private Label straightLabel;

	// 配置按钮
	Circle confCircle;
	// 收数按钮
	Circle recvCircle;

	// 直行按钮
	Button straightButton;

	// 角度旋钮，速度滑条
	private Slider angleSlider, speedSlider;

	public static WSNMatch getInstance() {
		return instance;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	// 角度的释放事件是，通过圆盘界面获得的，无法直接从Slider获取
	public Serial getSerial() {
		return serial;
	}

	public int getCurrentTagId() {
		return currentTagId;
	}

	public void setCurrentTagId(int currentTagId) {
		this.currentTagId = currentTagId;
	}

	public Label getTag() {
		return dataLabel;
	}

	public Label getUpOrdownLabel() {
		return upOrdownLabel;
	}

	public void setUpOrdownLabel(Label upOrdownLabel) {
		this.upOrdownLabel = upOrdownLabel;
	}

	public Label getSpeedLabel() {
		return speedLabel;
	}

	public void setSpeedLabel(Label speedLabel) {
		this.speedLabel = speedLabel;
	}

	public Label getAngleLabel() {
		return angleLabel;
	}

	public void setAngleLabel(Label angleLabel) {
		this.angleLabel = angleLabel;
	}

	/**
	 * 初始化超链接
	 */
	public void initHyperlink() {
		// ==============================================hyperlink
		for (int i = 0; i < captions.length; i++) {
			final Hyperlink hpl = hpls[i] = new Hyperlink(captions[i]);
			final String url = urls[i];
		}

	}

	public Config getConf() {
		return conf;
	}

	public void setConf(Config conf) {
		this.conf = conf;
	}

	/**
	 * 初始化超链接的动画
	 */
	public void initHyperlinkTimeline() {

		HyperlinkTimeline = new Timeline();

		HyperlinkTimeline.setCycleCount(Timeline.INDEFINITE);

		HyperlinkTimeline.setAutoReverse(false);

	}

	/**
	 * 初始化Tag节点弹出的效果
	 */
	public void initTagTimeline() {
		for (int i = 0; i < circle.length; i++) {
			circle[i] = new Circle(385, 580, 15, Color.rgb(57, 57, 57));
			circle[i].setOpacity(0);
			circle[i].setId(i + "");
			circle[i].setEffect(new Lighting());
			circle[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					Circle source = (Circle) event.getSource();
					currentTagId = Integer.parseInt(source.getId());
					int tagid = currentTagId + 100;
					Query(tagid);
					TagFadeout();
				}
			});
			rotateCircle[i] = new Rotate(0, 485, 580);
			rotateRect[i] = new Rotate(0, 105, 10);
			numberLabel[i] = new Label(i + "");
			numberLabel[i].setId(i + "");
			numberLabel[i].setOpacity(0);
			numberLabel[i].relocate(380, 570);
			numberLabel[i].resize(20, 20);
			numberLabel[i].getStyleClass().add("anspStyle");
			numberLabel[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					Label source = (Label) event.getSource();
					currentTagId = Integer.parseInt(source.getId());
					int tagid = currentTagId + 100;
					Query(tagid);
					TagFadeout();
				}
			});
		}

		// create a timeline for moving the circle

		Tagtimeline = new Timeline();

		// Tagtimeline.setCycleCount(Timeline.INDEFINITE);

		// Tagtimeline.setAutoReverse(true);
		for (int i = 0; i < circle.length; i++) {
			Tagtimeline.getKeyFrames().addAll(
					new KeyFrame(Duration.millis(i * 120), new KeyValue(
							circle[i].opacityProperty(), 0), new KeyValue(
							numberLabel[i].opacityProperty(), 0), new KeyValue(
							rotateCircle[i].angleProperty(), 0), new KeyValue(
							rotateRect[i].angleProperty(), 0)),
					new KeyFrame(Duration.millis(1200), new KeyValue(circle[i]
							.opacityProperty(), 1), new KeyValue(numberLabel[i]
							.opacityProperty(), 1), new KeyValue(rotateRect[i]
							.angleProperty(), (300 - i * 33)), new KeyValue(
							rotateCircle[i].angleProperty(), (300 - i * 33))

					));
		}

		for (int i = 0; i < circle.length; i++) {
			circle[i].getTransforms().add(rotateCircle[i]);
			numberLabel[i].getTransforms().add(rotateRect[i]);
		}
	}

	/**
	 * Tag消退
	 */
	public void TagFadeout() {
		for (int i = 0; i < circle.length; i++) {
			new FadeOutTransition(circle[i]).play();
			new FadeOutTransition(numberLabel[i]).play();
		}
		recvCircle.setEffect(null);
	}

	/**
	 * 配置过小车参数以后，要刷新界面图标
	 */
	public void Labelflush() {
		speedMinLabel.setText(conf.getMinSpeed() + "");
		speedMaxLabel.setText(conf.getMaxSpeed() + "");
		angleMinLabel.setText(conf.getMinAngle() + "");
		angleMaxLabel.setText(conf.getMaxAngle() + "");
	}

	/**
	 * 需要加入COM端口的配置
	 */
	public void initSerial() {

		serial = new Serial();
		String para2 = "serial@" + conf.getCOM() + ":telosb";

		String[] args = { "-comm", para2 };
		serial.initPhoenixSource(args);
	}

	/**
	 * 将指定标签信息设置为数据包里的值
	 * 
	 * @param i
	 * @param info
	 */
	public void setTagInfo(int tagid, String info) {
		int itemid = 0;
		for(int i = 0; i < 15; i++){
			if(tagIdList[i]==tagid){
				itemid = i+1;
			}
		}
		this.tagInfoLabel[itemid-1].setText("Tag"+itemid+"-info:" + info);
	}

	public void initTagInfo() {
		for (int i = 0; i < 15; i++) {
			tagInfoLabel[i] = new Label();
			tagInfoLabel[i].resizeRelocate(350, 125 + 20 * i, 300, 26);
			int temp = i+1;
			this.tagInfoLabel[i].setText("Tag"+temp+"-info:" + " ");
		}
	}

	// 写入配置信息
	public void writeConf() {
		ClassLoader cl = WSNMatch.class.getClassLoader();
		String filePath = System.getProperty("user.dir") + "/conf/config.txt";
		File histOperate = new File(filePath);
		FileWriter logWrite;
		try {
			logWrite = new FileWriter(histOperate, false);
			logWrite.write(conf.getMinSpeed() + "-" + conf.getMaxSpeed() + "-"
					+ conf.getMinAngle() + "-" + conf.getMaxAngle() + "-"
					+ conf.getMidAngle() + "-" + conf.getCOM());
			// System.out.println(conf.getMinSpeed()+"-"+conf.getMaxSpeed()+"-"+conf.getMinAngle()
			// +"-"+conf.getMaxAngle()+"-"+conf.getMidAngle()+"-"+conf.getCOM());
			logWrite.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 读取配置信息
	public void loadConf() {
		ClassLoader cl = WSNMatch.class.getClassLoader();
		String filePath = System.getProperty("user.dir") + "/conf/config.txt";
		BufferedReader reader = null;
		String tempString = null;
		String[] config = null;
		InputStream in = null;
		try {
			// in = configURL.openStream();
			in = new BufferedInputStream(new FileInputStream(filePath));
			reader = new BufferedReader(new InputStreamReader(in));

			tempString = reader.readLine();
			// 显示行号
			config = tempString.split("-");

			conf.setCOM(config[5]);
			conf.setMidAngle(Integer.parseInt(config[4]));
			conf.setMaxAngle(Integer.parseInt(config[3]));
			conf.setMinAngle(Integer.parseInt(config[2]));
			conf.setMaxSpeed(Integer.parseInt(config[1]));
			conf.setMinSpeed(Integer.parseInt(config[0]));

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			// e2.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			
			//小车初始角度
			angle = midAngle;
			
			instance = this;
			stage = primaryStage;

			// 导入默认的配置信息
			loadConf();
			initTagInfo();
			
			midAngle = conf.getMidAngle();
			leftAngle =  conf.getMinAngle();
			rightAngle =  conf.getMaxAngle();
			
			Group root = new Group();

			ImageView background = new ImageView(new Image(
					WSNMatch.class.getResourceAsStream("img/bg2.png")));

			// 小车上下左右控制按钮
			Button upBtn = new Button();
			Button downBtn = new Button();
			Button exitBtn = new Button();
			Button startBtn = new Button();
			Button stopBtn = new Button();

			// 配置按钮
			confCircle = new Circle(20, Color.rgb(57, 57, 57));
			// 收数按钮
			recvCircle = new Circle(20, Color.rgb(57, 57, 57));

			straightButton = new Button(".");
			angleSlider = new Slider(conf.getMinAngle(), conf.getMaxAngle(), conf.getMidAngle());
			angleSlider.setOrientation(Orientation.HORIZONTAL);

			speedSlider = new Slider(conf.getMinSpeed(), conf.getMaxSpeed(),
					conf.getMinSpeed());
			speedSlider.setOrientation(Orientation.VERTICAL);


			upBtn.resizeRelocate(206, 501, 77, 72);
			downBtn.resizeRelocate(206, 598, 77, 72);

			exitBtn.resizeRelocate(100, 598, 70, 73);

			startBtn.resizeRelocate(89, 506, 33, 33);
			stopBtn.resizeRelocate(148, 506, 33, 33);

			confCircle.resizeRelocate(671, 561, 52, 52);
			recvCircle.resizeRelocate(466, 561, 52, 52);
			straightLabel = new Label("直行");
			straightLabel.getStyleClass().add("anspStyle");
			straightButton.resizeRelocate(1102, 458, 24, 24);
			straightLabel.resizeRelocate(1105, 490, 45, 30);
			upBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					Forward(speedSlider.getValue());
					speedLabel.setText("向前-速度：" + speedSlider.getValue());

				}
			});
			downBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					Backward(speedSlider.getValue());
					speedLabel.setText("向后-速度：" + speedSlider.getValue());

				}
			});
			// 退出效果
			exitBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					Exit();
				}
			});

			startBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {

					Start();
					state = 1;
				}
			});
			stopBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {

					Stop();
					state = 0;
				}
			});
			confCircle.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println();

					confCircle.setEffect(new Lighting());
					LoadDialog.loadPlayList(stage, confCircle);
					upOrdownLabel.setText("圆形配置按钮\n");

				}
			});
			
			recvCircle.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println();

					confCircle.setEffect(new Lighting());
					ReceiveNumDialog.loadPlayList(stage, recvCircle);
					upOrdownLabel.setText("圆形配置按钮\n");

				}
			});

			// 直行按钮
			straightButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
//					StraightRun();
					Straight();
				}
			});

			straightButton.setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent ke) {
					if (ke.getCode() == KeyCode.ENTER) {
//						StraightRun();
						Straight();
					}
				}
			});
			initHyperlink();

			VBox Hyperlinkvbox = new VBox();
			Hyperlinkvbox.getChildren().addAll(hpls);

			Hyperlinkvbox.setPrefSize(339, 351);
			Hyperlinkvbox.setLayoutX(752);
			Hyperlinkvbox.setLayoutY(86);
			Hyperlinkvbox.autosize();

			// =======================
			initHyperlinkTimeline();

			HyperlinkTimeline.play();

			// ====================
			initTagTimeline();

			angleSlider.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> arg0,
						Number arg1, Number newValue) {
					
					if (orientState == 0) {
						angleLabel.setText("角度："
								+ angleSlider.getValue());
//						AngleChange(angleSlider.getValue());
						System.out.println("angerSpeed+++++++++++++++++"+angleSlider.getValue());

					} else {
						angleLabel.setText("角度："
								+ angleSlider.getValue());
//						AngleChange(angleSlider.getValue());

					}

				}
			});

			speedSlider.valueProperty().addListener(
					new ChangeListener<Number>() {
						@Override
						public void changed(
								ObservableValue<? extends Number> arg0,
								Number oldValue, Number newValue) {

							// speedLabel.setText(speedSlider.getValue() + "");
							if (orientState == 0) {
								speedLabel.setText("向前-速度："
										+ speedSlider.getValue());
								Forward(speedSlider.getValue());
							} else {
								speedLabel.setText("向后-速度："
										+ speedSlider.getValue());
								Backward(speedSlider.getValue());

							}

						}
					});

			// 鼠标释放时，发送命令
			speedSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (orientState == 0) {
						Forward(speedSlider.getValue());
					} else {
						Backward(speedSlider.getValue());
					}

				}
			});
			speedSlider.resizeRelocate(799, 480, 53, 186 + 20);
			// speedSlider.
			// =========================
			angleSlider.setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (orientState == 0) {
//						AngleChange(angleSlider.getValue());
					} else {
//						AngleChange(angleSlider.getValue());
					}

				}
			});
			angleSlider.resizeRelocate(999, 480, 53, 186 + 20);
			
			

			upOrdownLabel = new Label("方向");
			speedLabel = new Label("速度");
			angleLabel = new Label("角度");
			stopOrStart = new Label("小车状态");
			queryLabel = new Label("query状态");
			dataLabel = new Label("Tag_ID   Tag_Data      ");

			speedMinLabel = new Label();
			speedMaxLabel = new Label();
			angleMinLabel = new Label();
			angleMaxLabel = new Label();

			Labelflush();
			speedMinLabel.getStyleClass().add("anspStyle");
			speedMaxLabel.getStyleClass().add("anspStyle");
			angleMinLabel.getStyleClass().add("anspStyle");
			angleMaxLabel.getStyleClass().add("anspStyle");
			/**
			 * 文字显示屏
			 */

			upOrdownLabel.resizeRelocate(122, 95, 300, 26);
			speedLabel.resizeRelocate(122, 125, 300, 26);
			angleLabel.resizeRelocate(122, 155, 300, 26);
			stopOrStart.resizeRelocate(122, 185, 300, 26);
			queryLabel.resizeRelocate(122, 215, 300, 26);

			dataLabel.resizeRelocate(430, 95, 1000, 30);

			angleMaxLabel.resizeRelocate(1074, 681, 40, 26);
			angleMinLabel.resizeRelocate(888, 681, 40, 26);
			speedMinLabel.resizeRelocate(777, 665, 40, 26);
			speedMaxLabel.resizeRelocate(777, 485, 40, 26);
			
			// =========================
						/**
						 * 添加快捷键，操作
						 */
						root.setOnKeyPressed(new EventHandler<KeyEvent>() {
							@Override
							public void handle(KeyEvent ke) {
								switch (ke.getCode()) {

								case W:
									Forward(speedSlider.getValue());
									break;
								case A:
									Angle(leftAngle);
									break;
								case S:
									Backward(speedSlider.getValue());
									break;
								case D:
									Angle(rightAngle);
									break;
								case HOME:
									break;
								// 开始
								case SHIFT:
									Start();
									break;
								// 直行
								case E:
									Straight();
									break;
								//收数
//								case R:
//									QueryTagid();
//									break;
							    //停止
								case M:
									SendToNode();
									break;
								case Q:
									Stop();
									break;
									
									case DIGIT0:
										Query(100);
										break;
									case DIGIT1:
										Query(101);
										break;
									case DIGIT2:
										Query(102);
										break;
									case DIGIT3:
										Query(103);
										break;
									case DIGIT4:
										Query(104);
										break;
									case DIGIT5:
										Query(105);
										break;
									case DIGIT6:
										Query(106);
										break;
									case DIGIT7:
										Query(107);
										break;
									case DIGIT8:
										Query(108);
										break;
									case DIGIT9:
										Query(109);
										break;
									case Y:
										Query(110);
										break;
									case U:
										Query(111);
										break;
									case I:
										Query(112);
										break;
									case O:
										Query(113);
										break;
									case P:
										Query(114);
										break;

								default:
								}
							}
						});
						
						root.setOnKeyReleased(new EventHandler<KeyEvent>() {
							@Override
							public void handle(KeyEvent ke) {
								
								switch (ke.getCode()) {

								case W:
									Stop();
									break;
								case A:
									Straight();
									break;
								case S:
									Stop();
									break;
								case D:
									Straight();
									break;
								case HOME:
									break;
								default:
								}
							}
						});

			root.getChildren().addAll(background,/* vBox, */Hyperlinkvbox,
					upBtn, downBtn, exitBtn, stopBtn, startBtn, confCircle,
					straightButton, straightLabel, recvCircle, upOrdownLabel,
					dataLabel, speedLabel, angleLabel, stopOrStart, queryLabel);
			root.getChildren().addAll(speedMinLabel, speedMaxLabel,
					angleMinLabel, angleMaxLabel);
			root.getChildren().addAll(angleSlider, speedSlider);
			root.getChildren().addAll(circle);
			root.getChildren().addAll(numberLabel);
			root.getChildren().addAll(tagInfoLabel);
			root.setAutoSizeChildren(false);

			Scene scene = new Scene(root, 1204, 763);

			scene.setFill(Color.TRANSPARENT);
			scene.getStylesheets().add(
					getClass().getResource("wsnmatch.css").toExternalForm());
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.setScene(scene);
			primaryStage.show();
			// 从界面退出，不按退出按钮
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					event.consume();
					Exit();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	/**
	 * 角度调整
	 * 
	 * @param angleValue
	 */
	public void Angle(int angleValue) {
	
		
		if (WSNMatch.getInstance().getSerial() == null) {
			ErrorDialog.loadErrorDialog(stage, "请先配置");
		} else {

			System.out.println("FSpeed+++++++++++++++++"+angleValue);
			byte[] t = new byte[2];
			t[0] = (byte)((0xff&angleValue));
			t[1] = (byte)(0xff&(angleValue >> 8));
			
			byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
					0x3F, (byte) 0xBB, (byte) 0xD3, 0x00, 0x00 };
			// 将数据位填充到数据包里面，高位在后，低位在前
			data[data.length - 1] = t[0];
			data[data.length - 2] = t[1];
			
			Dump.dump("send message..........", data);

			//第一次发送命令，则初始化发送时间，并且发送命令
			if(angleFirstTime==1){	
				angleFirstTime = 0;
				serial.sendPackets(data);
				anglelastTime = System.currentTimeMillis();
				angleLabel.setText("角度:" + angleValue);			
			}
			else{
				//距离上一次发送100ms以内，则过滤该命令
				if((System.currentTimeMillis()-anglelastTime)<TIME_DELAY){
					System.out.println("过滤了转角命令\n");
				}
				//否则发送该命令
				else{
					serial.sendPackets(data);
					anglelastTime = System.currentTimeMillis();
					angleLabel.setText("角度:" + angleValue);
				}
				
			}
		}
	}
	/**
	 * 让节点缓存的数据发送给收集节点
	 */
public void SendToNode() {
		
		if (serial == null) {
			ErrorDialog.loadErrorDialog(stage, "请先配置");
		} else {

			byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F,
					0x0D, 0x3F, (byte) 0xBB, (byte) 0xFF, 0x00, 0x00 };

			Dump.dump("send message..........", data);
			//第一次发送命令，则初始化发送时间，并且发送命令
			if(forwardFirstTime==1){
				forwardFirstTime = 0;
				forwardlastTime = System.currentTimeMillis();
				serial.sendPackets(data);
				upOrdownLabel.setText("发送数据\n");
			}
		
			else{
				//距离上一次发送100ms以内，则过滤该命令
				if((System.currentTimeMillis()-forwardlastTime)<TIME_DELAY){
					System.out.println("过滤了forward命令\n");
				}
				//否则发送该命令
				else{
					serial.sendPackets(data);
					forwardlastTime = System.currentTimeMillis();
					upOrdownLabel.setText("向前\n");
				}
			}				
		}
	}
	
	/**
	 * 向前行进
	 */
	public void Forward(double value) {
		
		if (serial == null) {
			ErrorDialog.loadErrorDialog(stage, "请先配置");
		} else {

			int speedvalue = conf.getMinSpeed();
			byte[] t = new byte[2];
			t[0] = (byte)((0xff&speedvalue));
			t[1] = (byte)(0xff&(speedvalue >> 8));

			byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F,
					0x0D, 0x3F, (byte) 0xBB, (byte) 0xD5, 0x00, 0x00 };

			// 数据参数，2个 byte
			data[data.length - 1] = t[0];
			data[data.length - 2] = t[1];
			Dump.dump("send message..........", data);
			//第一次发送命令，则初始化发送时间，并且发送命令
			if(forwardFirstTime==1){
				forwardFirstTime = 0;
				forwardlastTime = System.currentTimeMillis();
				serial.sendPackets(data);
				upOrdownLabel.setText("向前\n");
			}
			else{
				//距离上一次发送100ms以内，则过滤该命令
				if((System.currentTimeMillis()-forwardlastTime)<TIME_DELAY){
					System.out.println("过滤了forward命令\n");
				}
				//否则发送该命令
				else{
					serial.sendPackets(data);
					forwardlastTime = System.currentTimeMillis();
					upOrdownLabel.setText("向前\n");
				}
			}				
		}
	}

	/**
	 * 向后行进
	 */
	public void Backward(double value) {
		
		if (serial == null) {
			ErrorDialog.loadErrorDialog(stage, "请先配置");
		} else {
			
			int speedvalue = conf.getMinSpeed();
			byte[] t = new byte[2];
			t[0] = (byte)((0xff&speedvalue));
			t[1] = (byte)(0xff&(speedvalue >> 8));
			
			byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F,
					0x0D, 0x3F, (byte) 0xBB, (byte) 0xD2, 0x00, 0x00 };

			// 数据参数，2个 byte
			data[data.length - 1] = t[0];
			data[data.length - 2] = t[1];

			Dump.dump("send message..........", data);
			//第一次发送命令，则初始化发送时间，并且发送命令
			if(backFirstTime==1){
				backFirstTime = 0;
				serial.sendPackets(data);
				backlastTime = System.currentTimeMillis();
				upOrdownLabel.setText("后退\n");					
			}
			else{
				//距离上一次发送100ms以内，则过滤该命令
				if((System.currentTimeMillis()-backlastTime)<TIME_DELAY){
					System.out.println("过滤了back命令\n");
				}
				//否则发送该命令
				else{
					serial.sendPackets(data);
					backlastTime = System.currentTimeMillis();
					upOrdownLabel.setText("后退\n");						
				}
				
			}
		}
	}

	/**
	 * 退出
	 */
	public void Exit() {
		if (serial != null) {
			byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
					0x3F, (byte) 0xBB, (byte) 0xD6, 0x00, 0x00 };

			serial.sendPackets(data);
		}
		Transition tran = new FlipOutXTransition(stage.getScene().getRoot());
		tran.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
				System.exit(1);
			}
		});
		tran.play();
	}

	
	/**
	 * 直行
	 */
	public void Straight() {
		if (serial == null) {
			ErrorDialog.loadErrorDialog(stage, "请先配置");
		} else {

			angleFirstTime = 1;
			
			byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
					0x3F, (byte) 0xBB, (byte) 0xD3, (byte) 0x00, 0x00 };
			
			byte[] t = new byte[2];
			t[0] = (byte)((0xff&midAngle));
			t[1] = (byte)(0xff&(midAngle >> 8));
			
			data[data.length - 1] = t[0];
			data[data.length - 2] = t[1];

			Dump.printPacket(System.out, data);
					
			serial.sendPackets(data);
		}
	}

	
	/**
	 * 开启
	 */
	public void Start() {
		
		if (serial == null) {
			ErrorDialog.loadErrorDialog(stage, "请先配置");
		} else {
			byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
					0x3F, (byte) 0xBB, (byte) 0xD1, 0x00, 0x00 };

			//第一次发送命令，则初始化发送时间，并且发送命令
			if(lastTime == 0){
				lastTime = System.currentTimeMillis();
				serial.sendPackets(data);
				stopOrStart.setText("小车开启\n");
			}
			else{
				//距离上一次发送100ms以内，则过滤该命令
				if((System.currentTimeMillis()-lastTime)<TIME_DELAY){
					System.out.println("过滤了start命令\n"+(System.currentTimeMillis()-lastTime));
				}
				//否则发送该命令
				else{

					serial.sendPackets(data);
					lastTime = System.currentTimeMillis();
					stopOrStart.setText("小车开启\n");
				}
			}
			// 停止后，速度角度回复初始值,方向超前
			angleSlider.setValue(midAngle);
			speedSlider.setValue(0);
			orientState = 0;
			midAngle = conf.getMidAngle();
			leftAngle =  conf.getMinAngle();
			rightAngle =  conf.getMaxAngle();
		}
	}

	/**
	 * 停止
	 * 
	 */

	public void Stop() {
		
		if (serial == null) {
			ErrorDialog.loadErrorDialog(stage, "请先配置");
		} else {
			byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
					0x3F, (byte) 0xBB, (byte) 0xD6, 0x00, 0x00 };

				serial.sendPackets(data);
				stopOrStart.setText("小车停止\n");

			// 停止后，速度角度回复初始值,方向超前
			speedSlider.setValue(0);

			//
			forwardFirstTime = 1;
			backFirstTime = 1;
		}
	}


	/**
	 * 查询TagId
	 * 
	 */
	private void QueryTagid() {
		for(int i=0; i < 15; i++){
			try {
				Thread.sleep(600);
				Query(tagIdList[i]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 *  查询TagId
	 */
	public void Query(int tagid) {
		
		if (serial == null) {
			ErrorDialog.loadErrorDialog(stage, "请先配置");
		} else {
			
			byte[] t = new byte[2];
			t[0] = (byte)((0xff&tagid));
			t[1] = (byte)(0xff&(tagid >> 8));

			byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
					0x3F, (byte) 0xBB, (byte) 0xD7, (byte) 0x00, 0x00 };

			// 数据参数，2个 byte
			data[data.length - 1] = t[0];
			data[data.length - 2] = t[1];

			Dump.dump("send message..........", data);

			//第一次发送命令，则初始化发送时间，并且发送命令
			if(lastTime == 0){
				serial.sendPackets(data);
				lastTime = System.currentTimeMillis();
				queryLabel.setText("query tag" + tagid);
			}
			else{
				//距离上一次发送50ms以内，则过滤该命令
				if((System.currentTimeMillis()-lastTime)<50){					
					System.out.println("过滤了query命令\n"+(System.currentTimeMillis()-lastTime));					
				}
				//否则发送该命令
				else{
					serial.sendPackets(data);				
					lastTime = System.currentTimeMillis();
					queryLabel.setText("query tag" + tagid);
				}
			}

		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
