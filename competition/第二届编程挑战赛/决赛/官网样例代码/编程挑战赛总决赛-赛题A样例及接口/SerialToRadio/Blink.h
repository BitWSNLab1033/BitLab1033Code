#ifndef BLINK_H
#define BLINK_H

enum {
	AM_Radio_MSG= 6,     //无线发送组别，可自主修改
  	AM_UART_MSG = 0x89 //串口通信的组别，不可更改，用于接收PC端发送的图片数据，触发UARTReceive.receive必备！！

};


//数据包格式
typedef nx_struct UartMsg{  
 	nx_uint16_t serialnumber;   //序号
 	nx_uint8_t data[75];     //数值(25个像素点的RGB信息)
}UartMsg;

#endif


