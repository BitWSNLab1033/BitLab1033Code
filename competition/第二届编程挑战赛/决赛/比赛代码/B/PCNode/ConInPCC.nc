
#include "CarSendMsg.h"
#include "Timer.h"
#include "pr.h"

module ConInPCC @safe()
{
  uses interface Timer<TMilli> as Timer0;
  uses interface Leds;
  uses interface Boot;

  uses interface SplitControl as RadioControl;
  uses interface AMSend;
  uses interface Receive as AMReceive;

  uses interface SplitControl as SerialControl;
  uses interface AMSend as SerialAMSend;
  uses interface Receive as SerialAMReceive;

}

implementation
{

       message_t car_pkt;
       message_t tag_pkt;

       event void Boot.booted()
       {
          call Timer0.startPeriodic( 2000 ); 

          call RadioControl.start();
          call SerialControl.start();
        }

       event void RadioControl.startDone(error_t err) 
       {
         if (err != SUCCESS) 
           call RadioControl.start();
       }

       event void RadioControl.stopDone(error_t err) {}

       event void SerialControl.startDone(error_t err) 
       {
         if (err != SUCCESS) 
           call SerialControl.start();
       }

       event void SerialControl.stopDone(error_t err) {}

       //Serial Receive msg from PC
       event message_t* SerialAMReceive.receive(message_t* msg, void* p, uint8_t len)
       {
            pr("xxx data :%u   %u",len,sizeof(car_send_msg_t));

            if(len == sizeof(car_send_msg_t)){
              car_send_msg_t* payload = (car_send_msg_t*)p;  
              car_send_msg_t* car_msg = (car_send_msg_t*)call AMSend.getPayload(&car_pkt, sizeof(car_send_msg_t));

              car_msg->control = payload->control;
              car_msg->value = payload->value;
 
               if (call AMSend.send(AM_BROADCAST_ADDR,&car_pkt, sizeof(car_send_msg_t)) != SUCCESS){
                     call Leds.led0Toggle();
                }
             }

            return msg;
       }

       //AMReceive msg from Radio
       event message_t* AMReceive.receive(message_t* msg, void* p, uint8_t len)
       {
            pr("xxx data :%u   %u",len,sizeof(tag_msg_t));

            if(len == sizeof(tag_msg_t)){
              tag_msg_t* payload = (tag_msg_t*)p;  
              tag_msg_t* tag_msg = (tag_msg_t*)call SerialAMSend.getPayload(&tag_pkt, sizeof(tag_msg_t));

              tag_msg-> tagid = payload->tagid;
              tag_msg-> value = payload->value;
 
               if (call SerialAMSend.send(AM_BROADCAST_ADDR,&tag_pkt, sizeof(tag_msg_t)) != SUCCESS){
                     call Leds.led0Toggle();
                }
             }

             if(len == sizeof(TagMsg)){
              TagMsg* payload = (TagMsg*)p;  
              tag_msg_t* tag_msg = (tag_msg_t*)call SerialAMSend.getPayload(&tag_pkt, sizeof(tag_msg_t));

              tag_msg-> tagid = payload->tagid;
              tag_msg-> value = payload->tagdata;
 
               if (call SerialAMSend.send(AM_BROADCAST_ADDR,&tag_pkt, sizeof(tag_msg_t)) != SUCCESS){
                     call Leds.led0Toggle();
                }
             }
            return msg;
       }

       event void AMSend.sendDone(message_t* m, error_t err) {
              if(err == SUCCESS){
               call Leds.led1Toggle();
              }
       }
       event void SerialAMSend.sendDone(message_t* m, error_t err) {
              if(err == SUCCESS){
               call Leds.led2Toggle();
              }
          pr("Serial Send Success\n"); 
       }		

  event void Timer0.fired(){}
}

