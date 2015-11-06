#include "Blink.h"
configuration BlinkAppC {}
implementation {

  	components BlinkC, MainC, LedsC;
 	BlinkC.Boot -> MainC;
 	BlinkC.Leds -> LedsC;


	//无线通信模块组件
  	components ActiveMessageC;
  	BlinkC.RadioControl -> ActiveMessageC;
  	BlinkC.RadioPacket -> ActiveMessageC;
  	BlinkC.RadioReceive-> ActiveMessageC.Receive[AM_Radio_MSG];  //接收

	//串口通信模块组件	
  	components SerialActiveMessageC as SAM;
  	BlinkC.UARTControl -> SAM;
  	BlinkC.UARTSend -> SAM.AMSend[AM_UART_MSG];  //发送
  	BlinkC.UARTPacket -> SAM.Packet;

}
