#include "TestNetworkC.h"
#include "Blink.h"

configuration BlinkAppC {}

implementation {
	
	components BlinkC, MainC, LedsC;
	BlinkC.Boot -> MainC;
	BlinkC.Leds -> LedsC;
	// CTP component
	components CollectionC as Collector;
	components new CollectionSenderC(CL_TEST);
	BlinkC.RoutingControl -> Collector;
	BlinkC.Send -> CollectionSenderC;
	BlinkC.RootControl -> Collector;
	BlinkC.Receive -> Collector.Receive[CL_TEST];
	
	BlinkC.CollectionPacket -> Collector;
	BlinkC.CtpInfo -> Collector;
	BlinkC.CtpCongestion -> Collector;

	//
	components ActiveMessageC;
	BlinkC.RadioControl -> ActiveMessageC;

	//serial receive
	components SerialActiveMessageC as SAM;
	BlinkC.SerialControl -> SAM;
	BlinkC.UARTReceive -> SAM.Receive[AM_UART_MSG];
	BlinkC.UARTPacket -> SAM.Packet;

	BlinkC.UARTSend -> SAM.AMSend[AM_UART_MSG];
}
