package com.greenorbs.wsnmatch.util;

import java.io.*;

import net.tinyos.packet.BuildSource;
import net.tinyos.packet.PacketSource;
import net.tinyos.util.Dump;
import net.tinyos.util.PrintStreamMessenger;

public class SendPkt {
		
	private static PacketSource sfw;
	static int realLen ; // sizeof(setting_t)+8 in TestNetwork.h
	static int dataLen ;
    static int dataOff ;
    
    /**
     * 开启串口
     * @param argv
     */
	public static void openSerial(String[] argv){
		realLen = 3+8; // sizeof(setting_t)+8 in TestNetwork.h
		dataLen = argv.length + 8;
		dataOff = 0;
		    
		 if (argv[0].equals("-comm")) {
	            sfw = BuildSource.makePacketSource(argv[1]);
	            dataOff = 2;
	            dataLen -= 2;
	        }
	        else {
	            sfw = BuildSource.makePacketSource();
	        }
			// datalen=3+8
			
	        

	        try {
				sfw.open(PrintStreamMessenger.err);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	/**
	 * 构建数据包并发送
	 * @param argv
	 * @throws IOException
	 */
    public static void sendPacket(String[] argv) throws IOException {
    	
    	openSerial(argv);

    	byte[] packet = new byte[realLen];

        // set the preamble: see TinyOS tutorials: Mote-PC communications
        packet[0] = (byte)0x00; // start of message
        // destination address
        packet[1] = (byte)0xff; packet[2] = (byte)0xff;
        // link source address
        packet[3] = (byte)0x00; packet[4] = (byte)0x0f;
        // message length
        packet[5] = (byte)realLen; //(byte)0x17;
        // group id
        packet[6] = (byte)0x3f;
        // amtype
        packet[7] = (byte)0xBB; 

		int i=0;
		
        for (i = 0; i < dataLen-8 && i < realLen-8; i++) {
            packet[i+8] = (byte)Integer.parseInt(argv[i+dataOff], 16);
            System.out.println(argv[i+dataOff]+"\n");
        }
		if (dataLen < realLen) {
          for ( ; i<realLen-8; i++) {
            packet[i+8] = 0;
          }
		}
		
		//Dump.printPacket(System.out, packet);
//        try {
//            sfw.writePacket(packet);
//        }
//        catch (IOException e) {
//            System.exit(2);
//        }
        Dump.printPacket(System.out, packet);
        System.out.println();
        // A close would be nice, but javax.comm's close is deathly slow
        sfw.close();
    }
    
    public static void main(String[] args){
    	String[] para = {"-comm","serial@COM30:telosb","d1","00","00"};
    	//openSerial(para);
		try {
			SendPkt.sendPacket(para);
			Thread.sleep(5000);
			String[] para1 = {"-comm","serial@COM30:telosb","d2","00","00"};
			SendPkt.sendPacket(para1);
			
			Thread.sleep(5000);
			String[] para2 = {"-comm","serial@COM30:telosb","d3","00","00"};
			SendPkt.sendPacket(para2);
			
			
			Thread.sleep(5000);
			String[] para3 = {"-comm","serial@COM30:telosb","d4","00","00"};
			SendPkt.sendPacket(para3);
			
			Thread.sleep(5000);
			String[] para4 = {"-comm","serial@COM30:telosb","d5","00","00"};
			SendPkt.sendPacket(para4);
			
			Thread.sleep(5000);
			String[] para5 = {"-comm","serial@COM30:telosb","d6","00","00"};
			SendPkt.sendPacket(para5);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}    
