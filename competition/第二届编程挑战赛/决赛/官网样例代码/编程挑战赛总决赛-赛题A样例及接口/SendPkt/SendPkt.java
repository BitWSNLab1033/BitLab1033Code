
import net.tinyos.util.*;
import net.tinyos.packet.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class SendPkt {
	
	public static int payloadlen = 2+75; // sizeof(setting_t)+8 in TestNetwork.h  	
	public static byte[] packet = new byte[payloadlen+8];
	
	public static PacketSource sfw;
	public static BufferedImage bufferedimage;
	
    public static void main(String[] argv) throws IOException{
    	send();
    }
    
    public static void send() throws IOException{
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入你要发送到的端口号:");
		final String name = sc.nextLine();
		sc.close();

		sfw = BuildSource.makePacketSource("serial@/dev/ttyUSB"+name+":telosb");						
		System.out.println("准备往【/dev/ttyUSB"+name+"】发送数据包");

    	bufferedimage = ImageIO.read(new FileInputStream("aa.jpg"));
    	
		// set the preamble: see TinyOS tutorials: Mote-PC communications
		packet[0] = (byte) 0x00; // start of message
		// destination address
		packet[1] = (byte) 0xff;
		packet[2] = (byte) 0xff;
		// link source address
		packet[3] = (byte) 0x00;
		packet[4] = (byte) 0x00;
		// message length
		packet[5] = (byte) payloadlen; // 
		// group id
		packet[6] = (byte) 0x00;
		// amtype
		packet[7] = (byte) 0x89;
    	   	
		new Timer().schedule(new TimerTask() {
			
			int x=0,y=0;
			int serialnum=0;
			int now=10;

			public void run() {
				
				packet[8] = (byte) (serialnum/256);
				packet[9] = (byte) (serialnum%256);
	

				for (int i = 0; i< 25; i++) {
					Color mycolor = new Color(bufferedimage.getRGB(x+i, y));					
					packet[now++] = (byte) (mycolor.getRed());
					packet[now++] = (byte) (mycolor.getGreen());					
					packet[now++] = (byte) (mycolor.getBlue());
				}


					try {
						sfw.open(PrintStreamMessenger.err);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.out.println("错误：你输入的端口号/dev/ttyUSB"+name+"不存在！");
						System.exit(0);
						e1.printStackTrace();
					} 
					
				try {
					sfw.writePacket(packet);
					System.out.println("第serialnum = "+ serialnum +"包发送完成");
					sfw.close();
					serialnum++;
					now=10;
					if(x==75){
						x=0;
						y++;					
					}
					else{ x=x+25;}

					if(y==100){	System.exit(0);}
				} catch (IOException e) {
					
					System.exit(2);
				}
			}
		}, 0,50);	
      }    	
}    
