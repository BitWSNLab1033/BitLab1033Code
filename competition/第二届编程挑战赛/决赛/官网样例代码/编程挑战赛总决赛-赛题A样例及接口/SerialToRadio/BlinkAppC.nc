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
  	BlinkC.AMSend-> ActiveMessageC.AMSend[AM_Radio_MSG];//发送

	//串口通信模块组件	
  	components SerialActiveMessageC as SAM;
  	BlinkC.UARTControl -> SAM;
  	BlinkC.UARTReceive -> SAM.Receive[AM_UART_MSG];//接收
  	BlinkC.UARTPacket -> SAM.Packet;

}
