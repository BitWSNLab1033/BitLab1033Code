// $Id: Sender.java,v 1.4 2006/12/12 18:22:59 vlahan Exp $

/*									tab:4
 * "Copyright (c) 2000-2003 The Regents of the University  of California.  
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 * 
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
 * CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS."
 *
 * Copyright (c) 2002-2003 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704.  Attention:  Intel License Inquiry.
 */
/* Authors:  David Gay  <dgay@intel-research.net>
 *           Intel Research Berkeley Lab
 *
 */

/**
 * @author David Gay <dgay@intel-research.net>
 * @author Intel Research Berkeley Lab
 */

package net.tinyos.message;

import java.io.*;

import net.tinyos.packet.*;
import net.tinyos.util.*;

/**
 * Sender class (send tinyos messages).<p>
 *
 * A sender class provides a simple interface built on Message for
 * sending tinyos messages to a SerialForwarder
 *
 * @version	2, 24 Jul 2003
 * @author	David Gay
 */
public class tinyosSender {
    // If true, dump packet contents that are sent
    private static final boolean VERBOSE = false;

    PhoenixSource sender;

    /**
     * Create a sender talking to PhoenixSource forwarder. The group id of
     * sent packets is not set.
     * @param forwarder PhoenixSource with which we wish to send packets
     */
    public tinyosSender(PhoenixSource forwarder) {
	sender = forwarder;
    }

    /**
     * Send m to moteId via this Sender's SerialForwarder
     * @param moteId message destination
     * @param m message
     * @exception IOException thrown if message could not be sent
     */
    synchronized public void send(int moteId, byte[] fullPacket) throws IOException {
	
    	sender.writePacket(fullPacket);

    }
}
