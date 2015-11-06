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
   uint16_t data[15];  //记录15个RFID标签中的数值
   uint16_t hint = 0;  //存储本队的编号
   uint8_t i = 0;
   uint8_t j=0;    //记录已经发送的位置
   uint8_t index;

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
      case 255:  //FF,send to node
      {
        //tagid = value;
        //call Car.QuiryReader(tagid);
        call Timer0.startPeriodic(500);  //每隔500ms发送一个数据包
        call Leds.led2Toggle();       //开始发送数据，第二个灯闪烁
  break;
      }
	default:
        break;
    }

  }

       event void Boot.booted()
       {
          //call Timer0.startPeriodic( 2000 ); 

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

              TagMsg* msg;

              msg = (TagMsg*)call AMSend.getPayload(&tag_pkt, sizeof(TagMsg));

              msg->tagid = tagid;
              msg->tagdata = value;
              msg->hint = 0;
 
              pr("Tag Data %u\n",value);

              index = tagid - 100;
              data[index] = value;
              call Leds.led2Toggle();   //数据收到，led 2 闪烁。

              if (call AMSend.send(0xffff,&tag_pkt, sizeof(TagMsg)) == SUCCESS){
                    call Leds.led0Toggle();   //发送成功闪烁
              }


       
              
              //读取了数据之后存在本地
              //index = tagid - 100;
              //data[index] = value;
	            //call Leds.led2Toggle();
              //call Leds.led0Toggle();
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
                  //call Leds.led2Toggle();
                 pr("AMSend Success!\n");
              }
       }

  event void Timer0.fired(){

    if(j<15){
      TagMsg* msg = (TagMsg*)call AMSend.getPayload(&tag_pkt, sizeof(TagMsg));

      msg->tagid = 100+j;
      msg->tagdata = data[j++];
      msg->hint = hint;

      pr("Tag Data %u\n",msg->tagdata);

       if (call AMSend.send(AM_BROADCAST_ADDR,&tag_pkt, sizeof(TagMsg)) != SUCCESS){
             call Leds.led0Toggle();
        }
    }
    
  }
}

