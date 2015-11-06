#include "TestNetworkC.h"
#include "printf.h"

module BlinkC {

	uses interface Boot;
	uses interface SplitControl as RadioControl;
	uses interface SplitControl as SerialControl;
	uses interface StdControl as RoutingControl;

	uses interface Send;
	uses interface Leds;
	
	uses interface RootControl;
	uses interface Receive;

	uses interface CollectionPacket;
	uses interface CtpInfo;
	uses interface CtpCongestion;
	

	uses interface Receive as UARTReceive;
	uses interface Packet as UARTPacket;
	
	uses interface AMSend as UARTSend; 

}

implementation {
	
	message_t packet;
	message_t uart_packet;
	uint16_t i = 0;
	uint8_t length = 0;
	bool busy = FALSE;
	bool uartbusy = FALSE;

	event void Boot.booted() {
		call SerialControl.start();
	}

	event void SerialControl.startDone(error_t err) {
		call RadioControl.start();
	}

	event void RadioControl.startDone(error_t err) {
		if(err != SUCCESS) {
			call RadioControl.start();
		} else {
			call RoutingControl.start();
			if(TOS_NODE_ID == 49) {
				call RootControl.setRoot();
			}
		}
	}

	event void RadioControl.stopDone(error_t err) {}
	event void SerialControl.stopDone(error_t err) {}
	
	event message_t* Receive.receive(message_t* msg, void* payload, uint8_t len) {
		if(len != sizeof(BeaconMsg)) {
			return msg;
		}else {
			BeaconMsg * rcm = (BeaconMsg *)payload;
			BeaconMsg * send = (BeaconMsg *)call UARTSend.getPayload(&uart_packet,sizeof(BeaconMsg));
			send->serialnumber = rcm->serialnumber;
			
			for(i=0;i<=74;i++) {
				send->data[i] = rcm->data[i];
			}
			if(!uartbusy) {
				if(call UARTSend.send(AM_BROADCAST_ADDR,&uart_packet,sizeof(BeaconMsg))==SUCCESS) {
				uartbusy = TRUE;
			}
			}
		}
	return msg;
	}
	
	event void UARTSend.sendDone(message_t * msg, error_t error) {
		if(&uart_packet == msg) {
			uartbusy = FALSE;
		}
	}

	event message_t* UARTReceive.receive(message_t* msg, void* payload, uint8_t len) {
		if(len == sizeof(BeaconMsg)) {
			BeaconMsg* rcm = (BeaconMsg*)call Send.getPayload(&packet, sizeof(BeaconMsg));
			memcpy(rcm,payload,len);
			lenth = len;
			if(!busy) {
				if(call Send.send(&packet, lenGth) == SUCCESS) {
					busy = TRUE;
				}	
			}
		}
		return msg;
	} 

	event void Send.sendDone(message_t * m, error_t err) {
		busy = FALSE;
	}
}
