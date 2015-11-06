#include "CarSendMsg.h"
#include "Timer.h"
#include "pr.h"

module ConInCarC @safe()
{
  uses interface Timer<TMilli> as Timer0;
  uses interface Leds;
  uses interface Boot;

  uses interface Car;
  uses interface SplitControl as CarControl;
  uses interface SplitControl as RadioControl;
  uses interface AMSend;
  uses interface Receive as AMReceive;

}

implementation
{

   message_t tag_pkt; 
   uint16_t tagid;

  void CarCommand(uint8_t control,uint16_t value) 
  {
    switch (control) {
      case 209:  //D1,start
      {
        call CarControl.start();
	break;
      }
      case 210:  //D2,Back
      {
        call Car.Back(value);
	break;		      
      }
      case 211:  //D3,Angle
      {
	call Car.Angle(value);
	break;     
      }

      case 213:  //D5,Forward
      {
        call Car.Forward(value); 
	break;
      }
      case 214:  //D6,Pause
      {
	call Car.Pause(); 
	break;
      }
      case 215:  //D7,QuiryReader
      {
        tagid = value;
        call Car.QuiryReader(tagid);
	break;
      }
	default:
        break;
    }

  }

       event void Boot.booted()
       {
          call Timer0.startPeriodic( 2000 ); 

	  call CarControl.start();
          call RadioControl.start();
        }

       event void RadioControl.startDone(error_t err) 
       {
         if (err != SUCCESS) 
           call RadioControl.start();
       }

       event void RadioControl.stopDone(error_t err) {}

       event void CarControl.startDone(error_t error)
       {
		pr("car startdone\n");
       }
    
       event void Car.readDone(error_t err,uint16_t value){
 
            if(err == SUCCESS){

              tag_msg_t* msg;

              msg = (tag_msg_t*)call AMSend.getPayload(&tag_pkt, sizeof(tag_msg_t));

              msg->tagid = tagid;
              msg->value = value;
 
              pr("Tag Data %u\n",value);

               if (call AMSend.send(AM_BROADCAST_ADDR,&tag_pkt, sizeof(tag_msg_t)) != SUCCESS){
                     call Leds.led0Toggle();
                }
            }

       }

       event void CarControl.stopDone(error_t error){}
  
       event message_t* AMReceive.receive(message_t* msg, void* p, uint8_t len)
       {
            //Control msg
            if(len == sizeof(car_send_msg_t)){

              car_send_msg_t* radio_payload = (car_send_msg_t*)p;  
 
              pr("car control %u %u\n",radio_payload->control,radio_payload->value);
 
              CarCommand(radio_payload->control,radio_payload->value);           

              call Leds.led1Toggle();
            }

            return msg;
       }

       event void AMSend.sendDone(message_t* m, error_t err) {
              if(err == SUCCESS){
                  call Leds.led2Toggle();
                 pr("AMSend Success!\n");
              }
       }

  event void Timer0.fired(){}
}

