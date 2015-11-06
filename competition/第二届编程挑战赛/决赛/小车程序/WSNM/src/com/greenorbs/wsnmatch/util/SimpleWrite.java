package com.greenorbs.wsnmatch.util;

/*
 * @(#)SimpleWrite.java	1.12 98/06/25 SMI
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license 
 * to use, modify and redistribute this software in source and binary
 * code form, provided that i) this copyright notice and license appear
 * on all copies of the software; and ii) Licensee does not utilize the
 * software in a manner which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THE
 * SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS
 * BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING
 * OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control
 * of aircraft, air traffic, aircraft navigation or aircraft
 * communications; or in the design, construction, operation or
 * maintenance of any nuclear facility. Licensee represents and
 * warrants that it will not use or redistribute the Software for such
 * purposes.
 */

import java.io.*;
import java.util.*;

import javax.comm.*;

public class SimpleWrite {
    static Enumeration portList;
    static CommPortIdentifier portId;
    static String messageString = "Hello, world!\n";
    static SerialPort serialPort;
    static OutputStream outputStream;
    public static void InitialDriver() {
  		javax.comm.CommDriver commDriver = null;
  		//tttt = System.currentTimeMillis();
  		String driverName = null;
  		try {
  		//	System.loadLibrary("win32com");
  		//	driver = (CommDriver) Class.forName(driverName).newInstance();
  		//	driver.initialize();
  		//	javax.comm.CommDriver commDriver = (javax.comm.CommDriver) Class.forName( "com.sun.comm.Win32Driver" ).newInstance(); 
  		//       commDriver.initialize(); 
  		 //      为了维护程序的移值性,可以根据系统类型来判断使用何种驱动名: 
  		       String osName = System.getProperty( "os.name" ); 
  		     // driverName = null; 

  		       if ( osName.startsWith("Windows") ) 
  		       { 
  		           //   System.out.println( "当前操作系统是Windows操作系统......" ); 
  		              driverName = "com.sun.comm.Win32Driver"; 
  		       } 
  		       else {
  		    	 //  System.out.println( "当前操作系统是Linux操作系统......" ); 
  		              driverName = "com.sun.comm.LinuxDriver";
  		              } 
  		       commDriver = (javax.comm.CommDriver) Class.forName( driverName ).newInstance(); 
  		       commDriver.initialize(); 
  		} catch (InstantiationException e1) {
  			//System.out.println(e1);

  		} catch (IllegalAccessException e1) {
  			//System.out.println(e1);

  		} catch (ClassNotFoundException e1) {
  			//System.out.println(e1);

  		}
  		
  	}

    
    
  //中继,解析数据通过串口发送
  	public static boolean sendData(byte[] data,OutputStream os){
  		boolean flag = false;
  		//00 FF FF 00 0F 0D 3F EE D1 00 00 00 00
  		byte[] temp = { 0x44, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, 0x00, 0x00, 9, 0x00,(byte) 137};
  		byte[] sendData = new byte[temp.length+9];
  		for (int i = 0; i < sendData.length; i++) {
  			if(i<temp.length){
  				sendData[i] = temp[i];
  			}else if((i-temp.length)<data.length){
  				sendData[i] = data[i-temp.length];
  			}
  		}
  		for (int i = 0; i < sendData.length; i++) {
  			String hex = Integer.toHexString(sendData[i] & 0xFF); 
  		     if (hex.length() == 1) { 
  		       hex = '0' + hex; 
  		     } 
  		     System.out.print(hex.toUpperCase()+" "); 
  		   } 
  		System.out.println();
  		byte[] commad;
  		commad = CRC_16.getSenderData(sendData);
  		for (int i = 0; i < commad.length; i++) {
  			String hex = Integer.toHexString(commad[i] & 0xFF); 
  		     if (hex.length() == 1) { 
  		       hex = '0' + hex; 
  		     } 
  		     System.out.print(hex.toUpperCase()+" "); 
  		   } 
  		System.out.println();
  		try {
  			os.write(commad);
  			flag = true;
  		} catch (IOException e) {
  			e.printStackTrace();
  			return false;
  		}
  		return flag;
  	}
    
    public static void main(String[] args) {
    	InitialDriver();
    	
    	int realLen = 9; // sizeof(setting_t)+8 in TestNetwork.h
  		int dataLen = 9;
        int dataOff = 2;
    	byte[] packet = new byte[realLen];

 
        // set the preamble: see TinyOS tutorials: Mote-PC communications
        packet[0] = (byte)0x00; // start of message
        // destination address
        packet[1] = (byte)0xff; packet[2] = (byte)0xff;
        // link source address
        packet[3] = (byte)0x00; packet[4] = (byte)0x00;
        // message length
        packet[5] = (byte)realLen; //(byte)0x17;
        // group id
        packet[6] = (byte)0x00;
        // amtype
        packet[7] = (byte)0x09; 
		int i=0;
        for (i = 0; i < dataLen-8 && i < realLen-8; i++) {
            packet[i+8] = (byte)Integer.parseInt("33", 16);
        }
		if (dataLen < realLen) {
          for ( ; i<realLen-8; i++) {
            packet[i+8] = 0;
          }
		}
        portList = CommPortIdentifier.getPortIdentifiers();
        byte[] data = {1,2,3};
		
        
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals("COM3")) {
               // if (portId.getName().equals("/dev/term/a")) {
                    try {
                        serialPort = (SerialPort)
                            portId.open("SimpleWriteApp", 2000);
                    } catch (PortInUseException e) {}
                    try {
                        outputStream = serialPort.getOutputStream();
                    } catch (IOException e) {}
                    try {
                        serialPort.setSerialPortParams(115200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {}
                    System.out.println("write message");

                    byte[] pack = {0x7E,0x44,0x0E,0x00,(byte) 0xFF,(byte) 0xFF,0x00,0x0F,0x0D,0x3F,(byte) 0xEE,(byte) 0xD1,0x00,0x00,0x00,0x00,0x32,0x5E,0x7E};
                    try {
						outputStream.write(pack);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					for(;;){
						//sendData(data,outputStream);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
                  // outputStream.write(packet);
                }
            }
        }
    } 
}
