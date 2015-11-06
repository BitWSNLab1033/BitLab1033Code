package net.tinyos.sf;

import java.util.*;
import java.lang.reflect.*;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;

class Node {
	public float xxxVoltage = 0;
	public float xxxTemperature = 0;
	public float Temperature = 0;
	public float Humidity = 0;
	public float PhotoActive = 0;
	public float TotalSolar = 0;
  public int parent = 0;
  public int metric = 0;
  public int msgs1 = 0;
  public int msgs2 = 0;
  public int msgs3 = 0;
  public int pathlen = 0;
  public int period1 = 0;
  public int period2 = 0;
  public int period3 = 0;
  public long task_time = 0; // 32khz
  public long last_task_time = 0;
  public long radio_time = 0;
  public long last_radio_time = 0;
  public long mac1_counter = 0;
  public long mac2_counter = 0;
  public long ro = 0; // receiveOverflowDropCounter
  public long noack = 0; // transmitNoACKDropCounter
 
	public Node () {
		this.clear();
	}
	public void clear() {
		xxxVoltage = 0;
		xxxTemperature = 0;
		Temperature = 0;
		Humidity = 0;
		PhotoActive = 0;
		TotalSolar = 0;
		parent = 0;
		metric = 0;
		msgs1 = 0; 
		msgs2 = 0; 
		msgs3 = 0;
		pathlen = 0;
		period1 = 0;
		period2 = 0;
		period3 = 0;
		task_time = 0;
		last_task_time = 0;
		radio_time = 0;
		last_radio_time = 0;
		mac1_counter = 0;
		mac2_counter = 0;
                ro = 0;
                noack = 0;
	}
}

public class ShowStatus implements net.tinyos.message.MessageListener {
	private tinyosMoteIF moteIF;
	private Node [] nodes;
	private final int nodeNum = 900;
	private final int lineNum = 5;
    private int lastLen = 0;
	
		
	public ShowStatus(String packetSource) throws Exception {
		if (packetSource != null) {
			moteIF = new tinyosMoteIF(BuildSource.makePhoenix(packetSource, PrintStreamMessenger.err));
		}
		else {
			moteIF = new tinyosMoteIF(BuildSource.makePhoenix(PrintStreamMessenger.err));
		}
		
		nodes = new Node[nodeNum+1];
		
		for (int i = 0; i <= nodeNum; i++) {
			nodes[i] = new Node();
		}
	}
	
	public void clear() {
    for (int i = 1; i <= nodeNum; i++) {
			nodes[i].clear();
		}
	}
	
	public void pr() {
/*		
		System.out.printf("Root: %.2fV, (%.1f, %.1f)^C, %.1fRH, (%.1f, %.1f)lux\n", 
		                            nodes[0].xxxVoltage,
		                            nodes[0].xxxTemperature, nodes[0].Temperature,
		                            nodes[0].Humidity,
		                            nodes[0].PhotoActive, nodes[0].TotalSolar);
*/		
		System.out.println(">>>>>Status: ");
		
		int n = 0;
		
		for (int i = 1; i <= nodeNum; i++) {
			if (nodes[i].msgs1 > 0) {
                float cpu = 0;
				if (nodes[i].period1 != 0) {
				  float p32 = nodes[i].period1 * 32768; // to ticks
				  cpu=(nodes[i].task_time - nodes[i].last_task_time)/p32;
				}
                float radio = 0;
				if (nodes[i].period3 != 0) {
                  float p3 = nodes[i].period3 * 1024; // to ms
                  radio = (nodes[i].radio_time - nodes[i].last_radio_time)/p3;
				}
				System.out.printf(" %3d(%d,%d,%d,(%d,%d))", 
					i, 
					//cpu*100,//nodes[i].xxxVoltage, 
					//radio*100,
					nodes[i].msgs1+nodes[i].msgs2+nodes[i].msgs3,
                                        nodes[i].parent,
					nodes[i].pathlen, 
					//nodes[i].mac1_counter,
					//nodes[i].mac2_counter,
                                        //nodes[i].ro, nodes[i].noack,
					nodes[i].ro,
					nodes[i].noack
					);
                n++;
                if ( (n != 0) && (n%lineNum == 0) ) {
                  System.out.println();
                }
            }
		}
		if (n%lineNum != 0) System.out.println();
        synchronized (this) {
            lastLen = 0;
        }
	}
	
	public void start() throws Exception {
				
		pr();
		
		while (true) {
			byte command[] = new byte[100];
		  int count = System.in.read(command);
		  
      System.out.printf("\n\n");
		  
		  switch (command[0]) {
		  	case 'q': 
		  	  System.exit(0);
		  	  break;
		  	case 'c':
		  	  clear(); pr();
		  	  break;
		  	default:
		  	  pr();
		  	  break; 
		  }
		}
	}
	
	// Vsensor is 1/2 of Vcc
	public double ToxxxVoltage(int adc) {
		return (float)adc/4096*1.5 *2;
	} 
	
	public double ToxxxTemperature(int adc) {
		// (Vtemp - 0.986)/0.00355
		return ( (float)adc/4096*1.5 - 0.986 ) / 0.00355;
	}
	
	public double ToTemperature(int val) {
		// temperature = -39.60 + 0.01*SOt
		return -39.60+0.01*val;
	}
	
	// tc is the temperature in C. (ref. ToTemperature)
	public double ToHumidity(int val, float tc) {
		// humidity = -4 + 0.0405*SOrh + (-2.8 * 10^-6)*(SOrh^2)
		double humidity = -4 + 0.0405*val + (-2.8 * 0.000001) * (val*val);
		// humidity_true = (Tc - 25) * (0.01 + 0.00008*SOrh) + humidity
    return (tc - 25) * (0.01 + 0.00008*val) + humidity;
	}
	
	// vol is the Vsensor in Volts (ref. ToxxxVoltage)
	public double ToPhotoActive(int val) {
		// I = Vsensor / 100,000
		// S1087    lx = 0.625 * 1e6 * I * 1000
		// S1087-01 lx = 0.769 * 1e5 * I * 1000
		float i = (float)ToxxxVoltage(val)/100000;
		
		return 0.625*1000000*i*1000;
	}
	
	public double ToTotalSolar(int val) {
		float i = (float)ToxxxVoltage(val)/100000;
		
		return 0.769*100000*i*1000;
	}
	
	public void messageReceived(int to, Message msg) {
		// update node status
		int from = 0;
		int type = 0;
		
		int RawxxxVoltage = 0;
		int RawxxxTemperature = 0;
		int RawTemperature = 0;
		int RawHumidity = 0;
		int RawPhotoActive = 0;
		int RawTotalSolar = 0;
		int parent = 0;
		int metric = 0;

		int pathlen = 0;
		int period = 0;
		long task_time = 0;
		long radio_time = 0;
		
		
    String s;
    String className;
	       
    
    try {
    	Class c = msg.getClass();
    	
    	Method get_type           = c.getMethod("get_type");
    	Method get_source         = c.getMethod("get_source");
    	
    	type = (Short)get_type.invoke(msg);
		  
		  from              = (Integer)get_source.invoke(msg);
    }
    catch (Exception e) {
	  	System.out.println(e);
	  }
	  
	  className = msg.getClass().getName();
	  //System.out.println(msg.getClass().getName()+" "+type);
	  
	  if (from >= nodeNum) return;
	  
	  if (className.equals("test_network_msg") && type == 0xc1) {
	    try {
    	  //Class c = Class.forName("test_network_msg");
    	  Class c = msg.getClass();

		    Method get_xxxVoltage     = c.getMethod("get_xxxVoltage");
		    Method get_xxxTemperature = c.getMethod("get_xxxTemperature");
		    Method get_Temperature    = c.getMethod("get_temperature");
		    Method get_Humidity       = c.getMethod("get_humidity");
		    Method get_PhotoActive    = c.getMethod("get_photo_active");
		    Method get_TotalSolar     = c.getMethod("get_total_solar");
		    Method get_parent         = c.getMethod("get_parent");
		    Method get_metric         = c.getMethod("get_metric");

		    Method get_pathlen      = c.getMethod("get_pathlen");
		    Method get_period       = c.getMethod("get_period");
		    Method get_task_time    = c.getMethod("get_task_time");
		    Method get_radio_time     = c.getMethod("get_radio_time");
		  

		  	RawxxxVoltage     = (Integer)get_xxxVoltage.invoke(msg);
			  RawxxxTemperature = (Integer)get_xxxTemperature.invoke(msg);
		    RawTemperature    = (Integer)get_Temperature.invoke(msg);
		    RawHumidity       = (Integer)get_Humidity.invoke(msg);
		    RawPhotoActive    = (Integer)get_PhotoActive.invoke(msg);
		    RawTotalSolar     = (Integer)get_TotalSolar.invoke(msg); 
		    parent            = (Integer)get_parent.invoke(msg);
		    metric            = (Integer)get_metric.invoke(msg);

		    pathlen = (Short)get_pathlen.invoke(msg);
		    period = (Integer)get_period.invoke(msg);
		  
		    task_time = (Long)get_task_time.invoke(msg);
		    radio_time = (Long)get_radio_time.invoke(msg);
		  	
	    }
	    catch (Exception e) {
	  	  System.out.println(e);
	    }


	  	nodes[from].xxxVoltage     = (float)ToxxxVoltage(RawxxxVoltage);
		  nodes[from].xxxTemperature = (float)ToxxxTemperature(RawxxxTemperature);
		  nodes[from].Temperature    = (float)ToTemperature(RawTemperature);
		
		  nodes[from].Humidity       = (float)ToHumidity(RawHumidity, nodes[from].Temperature);
		  nodes[from].PhotoActive    = (float)ToPhotoActive(RawPhotoActive);
		  nodes[from].TotalSolar     = (float)ToTotalSolar(RawTotalSolar);
		  nodes[from].parent         = parent;
		  nodes[from].metric         = metric;
			nodes[from].msgs1++;

		  nodes[from].pathlen = pathlen;
		  nodes[from].period1 = period;

      nodes[from].last_task_time = nodes[from].task_time;
		  nodes[from].task_time = task_time;
		  
		  nodes[from].last_radio_time = nodes[from].radio_time;
		  nodes[from].radio_time = radio_time;
		
		  // clear and dump Message
		  for (int i=0; i<lastLen; i++)
		    System.out.print('\b');
		  for (int i=0; i<lastLen; i++)
		    System.out.print(' ');
		  for (int i=0; i<lastLen; i++)
		    System.out.print('\b');


		  s = String.format("<<<<<Recv from %3d[%x]: %.2fV, (%.1f, %.1f)^C, %.1fRH, (%.1f, %.1f)lux, P=%d, ETX=%.2f", 
		                            from, 
		                            type,
		                            nodes[from].xxxVoltage,
		                            nodes[from].xxxTemperature, nodes[from].Temperature,
		                            nodes[from].Humidity,
		                            nodes[from].PhotoActive, nodes[from].TotalSolar,
		                            nodes[from].parent,
		                            nodes[from].metric/10.0);
      synchronized (this) {
    		System.out.print(s);
        lastLen=s.length();
      }
	  }
	  
	  else if (className.equals("neighbor_info_msg") && type == 0xc2) {
	    nodes[from].msgs2++;
	  }
	  else if (className.equals("node_status_msg") && type == 0xc3) {
	    try {
    	  //Class c = Class.forName("node_status_msg");
    	  Class c = msg.getClass();
            Method get_period       = c.getMethod("get_period");
			
		    Method get_mac1_counter   = c.getMethod("get_macIBackOffCounter");
		    Method get_mac2_counter   = c.getMethod("get_macCBackOffCounter");
                    Method get_ro_counter = c.getMethod("get_receiveOverflowDropCounter");
                    Method get_noack_counter = c.getMethod("get_transmitNoACKDropCounter");
		    
		    nodes[from].mac1_counter = (Long)get_mac1_counter.invoke(msg);
  	        nodes[from].mac2_counter = (Long)get_mac2_counter.invoke(msg);
	  	    nodes[from].msgs3++;
		    nodes[from].period3 = (Integer)get_period.invoke(msg);
                    nodes[from].ro = (Long)get_ro_counter.invoke(msg);
                    nodes[from].noack = (Long)get_noack_counter.invoke(msg);			
		  }
		  catch (Exception e) {
	  	  System.out.println(e);
	    }
	    
	  	
	    // clear and dump Message
		  for (int i=0; i<lastLen; i++)
		    System.out.print('\b');
		  for (int i=0; i<lastLen; i++)
		    System.out.print(' ');
		  for (int i=0; i<lastLen; i++)
		    System.out.print('\b');


		  s = String.format("<<<<<Recv from %3d[%x]: MAC (%d, %d)", 
		                            from, 
		                            type,
		                            nodes[from].mac1_counter,
		                            nodes[from].mac2_counter
		                            );
      synchronized (this) {
    		System.out.print(s);
        lastLen=s.length();
      }
	  	
	  }		
	}
	
	private static void usage() {
		System.err.println("usage: xxx");
	}
	
	private void addMsgType(Message msg) {
		moteIF.registerListener(msg, this);
	}
	
	public static void main(String[] args) throws Exception {
		String packetSource = null;
		//String className = null;
		Message m1 = null;
		Message m2 = null;
		Message m3 = null;
		
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-comm")) {
					packetSource = args[++i];
				}
				//else {
				//	className = args[i];
				//}
			}
		}
		else if (args.length != 0) {
			usage();
			System.exit(1);
		}
		
		ShowStatus ss = new ShowStatus(packetSource);
		
		//if (className != null) {
		  try {
		  	Class c1 = Class.forName("test_network_msg");
		  	Class c2 = Class.forName("neighbor_info_msg");
		  	Class c3 = Class.forName("node_status_msg");
		    		    		    
			  Object p1 = c1.newInstance();
			  Object p2 = c2.newInstance();
			  Object p3 = c3.newInstance();
			  
			  m1 = (Message)p1;
			  m2 = (Message)p2;
			  m3 = (Message)p3;
						
			  if (m1.amType() < 0) {
			    System.err.println(" does not have an AM type - ignored");
			  }
			  else {
			  }
		  }
		  catch (Exception e) {
			  System.err.println(e);
		  }
		  ss.addMsgType(m1); // c1 c2 c3 have the same AM type, so will listen all these msgs
		  ss.addMsgType(m2);
		  ss.addMsgType(m3);
		//}
		
		ss.start();
	}
}
