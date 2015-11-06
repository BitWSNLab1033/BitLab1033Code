#include "CarSendMsg.h"
#include "printf.h"

module ConInTagC @safe(){
  uses interface Timer<TMilli> as Timer0;
  uses interface Leds;
  uses interface Boot;
  uses interface SplitControl as RadioControl;

  uses interface AMSend as AMSendControl;
  uses interface Receive as AMReceiveControl;

  uses interface AMSend as AMSendTag;
  uses interface Receive as AMReceiveTag;
}

implementation{

  message_t tag_pkt;
  message_t car_pkt;

  uint16_t SendA = 0;
  uint16_t SendB = 0;
  uint16_t SendC = 0;
  uint16_t SendD = 0;
  uint16_t SendE = 0;

  event void Boot.booted(){
    call Timer0.startPeriodic( 3000 ); 
    call RadioControl.start();
  }

  event void RadioControl.startDone(error_t err){
    if(err!=SUCCESS)
    {call RadioControl.start();}
  }

  event void RadioControl.stopDone(error_t err){}

  event message_t*  AMReceiveControl.receive(message_t* msg,void* p,uint8_t len){

      //printf("AMReceiveControl len = %u \n",len);
     // printfflush();

    if(len == sizeof(car_send_msg_t)){
      car_send_msg_t* car_payload=(car_send_msg_t*)p;
      car_send_msg_t* car_send_msg=(car_send_msg_t*)call AMSendControl.getPayload(&car_pkt,sizeof(car_send_msg_t));
      car_send_msg->control = car_payload->control;
      car_send_msg->value = car_payload->value;

        //printf("control = %u value = %u \n",car_send_msg->control,car_send_msg->value);
        //printfflush();

      //send to the command to car 
         /* if(call AMSendControl.send(10,&car_pkt,sizeof(car_send_msg_t))!=SUCCESS){
             call Leds.led0Toggle();
          }else{
            //printf("Node send to car carPkt\n");
           //printfflush();
             call Leds.led1Toggle();
          }*/

      //send to the 2 node 
      if(TOS_NODE_ID==1){
         SendD++;
          if(call AMSendControl.send(2,&car_pkt,sizeof(car_send_msg_t))!=SUCCESS){
             call Leds.led0Toggle();
          }else{
             //printf("Node 1 send to 2 car carPkt\n");
             //printfflush();
             call Leds.led1Toggle();
          }
       }else if(TOS_NODE_ID == 2){
           if(call AMSendControl.send(10,&car_pkt,sizeof(car_send_msg_t))!=SUCCESS){
             call Leds.led0Toggle();
          }else{
            //printf("Node send to car carPkt\n");
           //printfflush();
             call Leds.led1Toggle();
          }
       }
    }
    return msg;
  }

  event message_t*  AMReceiveTag.receive(message_t* msg,void* p,uint8_t len){

     // printf("AMReceiveTag len = %u \n",len);
      //printfflush();

    if(len == sizeof(TagMsg)){
      TagMsg* payload=(TagMsg*)p;
      TagMsg* tag_msg=(TagMsg*)call AMSendTag.getPayload(&tag_pkt,sizeof(TagMsg));
      tag_msg->tagid=payload->tagid;
      tag_msg->tagdata=payload->tagdata;
      tag_msg->hint = payload->hint;

     // printf("tagid = %u value = %u \n",tag_msg->tagid,tag_msg->value);
    //  printfflush();

   //send to the tagdata to node 1 
    if(TOS_NODE_ID == 2){
    SendE++;
      if(call AMSendTag.send(1,&tag_pkt,sizeof(TagMsg))!=SUCCESS){
        call Leds.led0Toggle();
      }else{
       // printf("Node 2 send to 1 tagPkt\n");
      // printfflush();
        call Leds.led2Toggle();
      }
    }
   //send to the tagdata to pc and target receive
      /*if(call AMSendTag.send(0,&tag_pkt,sizeof(tag_msg_t))!=SUCCESS){
        call Leds.led0Toggle();
      }else{
        printf("Node send to PC tagPkt\n");
        printfflush();
        call Leds.led2Toggle();
      }*/

      if(call AMSendTag.send(9,&tag_pkt,sizeof(TagMsg))!=SUCCESS){
        call Leds.led0Toggle();
      }

    }
    return msg;
  }

  event void AMSendTag.sendDone(message_t* m,error_t err){}
  event void AMSendControl.sendDone(message_t* m,error_t err){}

  event void Timer0.fired(){
   //  printf("SendA = %u;SendB = %u;SendC = %u;SendD = %u;SendE = %u;\n",SendA,SendB,SendC,SendD,SendE);
   //  printfflush();
  }
}
