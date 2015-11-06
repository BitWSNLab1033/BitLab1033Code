package com.greenorbs.wsnmatch.util;

/*									tab:4
 * "Copyright (c) 2005 The Regents of the University  of California.  
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and
 * its documentation for any purpose, without fee, and without written
 * agreement is hereby granted, provided that the above copyright
 * notice, the following two paragraphs and the author appear in all
 * copies of this software.
 * 
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
 * PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL
 * DAMAGES ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
 * DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS."
 *
 */

/**
 * Java-side application for testing serial port communication.
 * 
 *
 * @author Phil Levis <pal@cs.berkeley.edu>
 * @date August 12 2005
 */

import java.io.IOException;

import javafx.application.Platform;

import com.greenorbs.wsnmatch.ErrorDialog;
import com.greenorbs.wsnmatch.WSNMatch;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;

public class Serial implements MessageListener {

	private MoteIF moteIF;

	private PhoenixSource phoenix;

	public Serial() {

	}

	/**
	 * 发送数据包
	 * 
	 * @param data
	 */
	public void sendPackets(byte[] data) {
		// System.out.println("Sending packet " );

		try {
			moteIF.send(0, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err
					.println("Exception thrown when sending packets. Exiting.");
			System.err.println(e);
		}

	}

	/**
	 * 数据包解析
	 */
	public void messageReceived(int to, final Message message) {
		//数据包的字符信息
		Dump.dump("", message.getSerialPacket().dataGet());
		String tmp = Dump.returnPacket(System.out, message.getSerialPacket().dataGet(),0,message.getSerialPacket().dataGet().length);
		String data[] = tmp.split(" ");
        System.out.println(message.getSerialPacket().dataGet().length);
		System.err.println("in the xianshi -------------------");
		if(message.getSerialPacket().dataGet().length == 12){
			int tagid = Integer.parseInt(data[8] + data[9], 16);
			
			if(tagid>99&&tagid<115){
//			int tagdata = Integer.parseInt(data[10] + data[11], 16);	
//		    final String result = "    " + tagid + "      "+ tagdata;
			final String result = "    " + tagid + "      "+ data[10] + data[11];
			if (Platform.isFxApplicationThread()) {
				WSNMatch.getInstance().setTagInfo(tagid , result);
				WSNMatch.getInstance().setCurrentTagId(-1);
			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						WSNMatch.getInstance().setTagInfo(tagid,result);
						WSNMatch.getInstance().setCurrentTagId(-1);
					}
				}); 
			}
		  }
		}
	}

	private static void usage() {
		System.err.println("usage: TestSerial [-comm <source>]");
	}

	public void initPhoenixSource(String[] args) {
		// String[] args = {"-comm","serial@COM30:telosb"};
		String source = null;
		if (args.length == 2) {
			if (!args[0].equals("-comm")) {
				usage();
				System.exit(1);
			}
			source = args[1];
		} else if (args.length != 0) {
			usage();
			System.exit(1);
		}

		// PhoenixSource phoenix;
		if (source == null) {
			// System.out.println("TestSeroal:source==null");
			phoenix = BuildSource.makePhoenix(PrintStreamMessenger.err);
		} else {
			// System.out.println("TestSeroal:source!=null");
			phoenix = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
		}
		this.moteIF = new MoteIF(phoenix);
		this.moteIF.registerListener(new TestSerialMsg(), this);
		if (!phoenix.isopen) {
			ErrorDialog.loadErrorDialog(WSNMatch.getInstance().getStage(),
					"串口配置失败");

		}
	}

	public MoteIF getMoteIF() {
		return moteIF;
	}

	public void setMoteIF(MoteIF moteIF) {
		this.moteIF = moteIF;
	}

	public PhoenixSource getPhoenix() {
		return phoenix;
	}

	public void setPhoenix(PhoenixSource phoenix) {
		this.phoenix = phoenix;
	}

	public static void main(String[] argv) throws Exception {

		// MoteIF mif = new MoteIF(phoenix);
		Serial serial = new Serial();
		String[] args = { "-comm", "serial@COM20:telosb" };

		serial.initPhoenixSource(args);

		byte[] data = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D, 0x3F,
				(byte) 0xBB, (byte) 0xD1, 0x00, 0x00 };

		serial.sendPackets(data);

		Thread.sleep(5000);

		byte[] data1 = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
				0x3F, (byte) 0xBB, (byte) 0xD2, 0x00, 0x00 };

		serial.sendPackets(data1);

		Thread.sleep(5000);

		byte[] data2 = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
				0x3F, (byte) 0xBB, (byte) 0xD3, 0x00, 0x00 };

		serial.sendPackets(data2);

		Thread.sleep(5000);

		byte[] data3 = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
				0x3F, (byte) 0xBB, (byte) 0xD4, 0x00, 0x00 };

		serial.sendPackets(data3);

		Thread.sleep(5000);

		byte[] data4 = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
				0x3F, (byte) 0xBB, (byte) 0xD5, 0x00, 0x00 };

		serial.sendPackets(data4);

		Thread.sleep(5000);

		byte[] data5 = { 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x0F, 0x0D,
				0x3F, (byte) 0xBB, (byte) 0xD6, 0x00, 0x00 };

		serial.sendPackets(data5);

		Thread.sleep(5000);
	}

}
