package net.tinyos.sf;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;


public class ListenTime {
    public static void main(String args[]) throws IOException {
        String source = null;
        PacketSource reader;
        if (args.length == 2 && args[0].equals("-comm")) {
            source = args[1];
        }
	    else if (args.length > 0) {
	        System.err.println("usage: java net.tinyos.tools.Listen [-comm PACKETSOURCE]");
	        System.err.println("       (default packet source from MOTECOM environment variable)");
	        System.exit(2);
	    }   
        if (source == null) {	
  	        reader = BuildSource.makePacketSource();
  	    }
        else {
  	        reader = BuildSource.makePacketSource(source);
        }
	    if (reader == null) {
	        System.err.println("Invalid packet source (check your MOTECOM environment variable)");
	        System.exit(2);
	    }

	    try {
	        reader.open(PrintStreamMessenger.err);
	        for (;;) {
	            byte[] packet = reader.readPacket();
                if (packet.length < 8) {
                }
                else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
                    String ts = dateFormat.format(new Date());
                    System.out.print(ts+": ");

                    Dump.printPacket(System.out, packet);
                    System.out.println();
                    System.out.flush();
                }
	        }
	    }
	    catch (IOException e) {
	        System.err.println("Error on " + reader.getName() + ": " + e);
	    }
    }
}

