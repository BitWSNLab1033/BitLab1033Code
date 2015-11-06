#include "Timer.h"
#include "BiDaXiao.h"
#include "printf.h"
module BiDaXiaoC{
  uses interface Boot;
  uses interface Timer<TMilli> as Timer0;

  uses interface AMSend;
  uses interface Receive as AMReceive;
  uses interface Receive as SerialReceive;
  
  uses interface Packet as SerialPacket;
  uses interface Packet as AMPacket;

  uses interface SplitControl as AMControl;
  uses interface SplitControl as SerialControl;
}
implementation{
  message_t pkt;
  bool SendBusy = FALSE;
  bool flag = FALSE;
  uint16_t data_max;
  uint16_t data_min;
  uint16_t data;
  uint16_t counter=0;

  event void Boot.booted(){
    call SerialControl.start();
  }
  event void SerialControl.startDone(error_t err){
    if(err != SUCCESS){
    call SerialControl.start();
    }
    else{
      call AMControl.start();
    }
  }
  event void AMControl.startDone(error_t err){
    if(err != SUCCESS){
      call AMControl.start();
    }
    else{
      call Timer0.startOneShot(7000);
    }
  }
  event void AMControl.stopDone(error_t err){}
  event void SerialControl.stopDone(error_t err){}
  
  event message_t* SerialReceive.receive(message_t* msg,void* p,uint8_t len){
    if(len == sizeof(SerialMsg_t)){
      SerialMsg_t* serial_payload=(SerialMsg_t*)p;
      data_max=serial_payload->data;
      data_min=serial_payload->data;
      data=serial_payload->data;
    }
    return msg;
  }
  event message_t* AMReceive.receive(message_t* msg,void* p,uint8_t len){
    if(len == sizeof(NetworkMsg_t)){
      NetworkMsg_t* network_payload=(NetworkMsg_t*)p;
      if(data_max < network_payload->data_max){data_max = network_payload->data_max;}
      if(data_min > network_payload->data_min){data_min = network_payload->data_min;}
    }
    return msg;
  }
  event void Timer0.fired(){
    NetworkMsg_t* msg =(NetworkMsg_t*)call AMSend.getPayload(&pkt,sizeof(NetworkMsg_t));
    msg->nodeid=TOS_NODE_ID;
    msg->data=data;
    msg->data_max=data_max;
    msg->data_min=data_min;
    if(!SendBusy){
      if(call AMSend.send(AM_BROADCAST_ADDR,&pkt,sizeof(NetworkMsg_t))!=SUCCESS){
        SendBusy=FALSE;
      }
      else{
        SendBusy=TRUE;
      }
    }
    counter++;
    if(counter<3){
      call Timer0.startOneShot(10000);
    }
    else{
      if(TOS_NODE_ID ==0){
        printf("GreenOrbs %x %x\n",data_min,data_max);
        printfflush();
      }
    }
  }
  event void AMSend.sendDone(message_t* m,error_t err){
    SendBusy=FALSE;
  }

}



