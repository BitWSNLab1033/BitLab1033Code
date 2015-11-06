#include "Blink.h"
module BlinkC {
  	uses interface Boot;
  	uses interface Leds;

	//无线通信模块用的接口
  	uses interface SplitControl as RadioControl;
  	uses interface Packet as RadioPacket;
  	uses interface Receive as RadioReceive;

	//串口通信模块用的接口
  	uses interface SplitControl as UARTControl;
	uses interface AMSend as UARTSend;
   	uses interface Packet  as UARTPacket;
}


implementation {
 

	message_t uart_packet;   //串口接受的数据包

    bool uartbusy = FALSE;   //串口发送锁

	uint8_t i;

  	event void Boot.booted() {
    	call UARTControl.start(); //开启串口通信模块
  	}

  	event void UARTControl.startDone(error_t err) {
    	if (err != SUCCESS) {
      		call RadioControl.start();//开启无线通信模块
    	}
		else{
			call RadioControl.start();
		}
 	}
  	
	event void RadioControl.startDone(error_t err) {
    	if (err != SUCCESS) {
      		call RadioControl.start();
    	}
  	}

  	event void RadioControl.stopDone(error_t err) { }

  	event void UARTControl.stopDone(error_t err) { }	

 	//当收到广播的数据包时，触发此事件
    event message_t* RadioReceive.receive(message_t* msg, void* payload, uint8_t len) {
 		call Leds.led0Toggle();
 		if(len!=sizeof(UartMsg)) //判断包的负载长度
		{
			return msg;
		}
		else
		{
			UartMsg* rcm = (UartMsg*)payload;  //定义rcm指针指向传递过来的包负载
      		UartMsg* send = (UartMsg*)call UARTSend.getPayload(&uart_packet,sizeof(UartMsg)); //定义send指针指向待发包uart_packet的负载      

			//将收到的串口数据赋值给到uart_packet的负载
			send->serialnumber  = rcm->serialnumber;	
			for(i=0;i<=74;i++){
				send->data[i] = rcm->data[i];
			}
			
			if(!uartbusy)	
			{			
				if(call UARTSend.send(0xFFFF,&uart_packet, sizeof(UartMsg))==SUCCESS)//将uart_packet数据包发送到串口	  
				{				
					uartbusy = TRUE;					 
				}					
			}
		}
 		return msg;
 	}

  	event void UARTSend.sendDone(message_t* msg, error_t err) {
		if (&uart_packet == msg) {
			uartbusy = FALSE;
		}
  	}

}
