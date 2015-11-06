#ifndef BLINK_H
#define BLINK_H

enum {
  	AM_UART_MSG = 0x89, //串口通信组别，用于49号节点发送最终数据给PC端，请勿修改
	AM_Radio_MSG= 6     //无线通信组别，可自主修改,但发送和接收必须一致，建议默认
};


//数据包格式
typedef nx_struct UartMsg{  
 	nx_uint16_t serialnumber;   //序号
 	nx_uint8_t data[75];     //数值(25个像素点的RGB信息)
}UartMsg;

#endif


