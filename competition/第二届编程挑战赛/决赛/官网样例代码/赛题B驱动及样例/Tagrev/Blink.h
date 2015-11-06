#ifndef BLINK_H
#define BLINK_H

enum {
  	AM_UART_MSG = 0x89, //串口通信组别
	AM_Radio_MSG= 6     //无线通信组别
};


//数据包格式
typedef nx_struct TagMsg{  
 	nx_uint16_t tagid;   	//标签id
 	nx_uint16_t tagdata;	//标签数值
 	nx_uint16_t hint;       //参赛队标志项，用于区分其他队伍
}TagMsg;

#endif


