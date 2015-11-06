

#include "Timer.h"
#include "pr.h"
module BlinkC @safe()
{
  uses interface Timer<TMilli> as Timer0;
  uses interface Timer<TMilli> as Timer1;
  uses interface Timer<TMilli> as Timer2;
  uses interface Leds;
  uses interface Boot;
#ifdef EN_CAR
  uses interface Car;
  uses interface SplitControl as CarControl;
#endif
}
implementation
{
 uint8_t option = 0;
 event void Boot.booted()
  {
    call Timer0.startPeriodic( 2000 ); //just for test without pc control

    call Timer1.startPeriodic( 500 );
    call Timer2.startPeriodic( 500 );
	#ifdef EN_CAR
	call CarControl.start();
	#endif
  }
#ifdef EN_CAR 
  	event void CarControl.startDone(error_t error)
	{

	}
    
	event void Car.readDone(error_t err,uint16_t data )
	{
  
		pr("tag data :%x",data);
		//return the tag data 

	}
	event void CarControl.stopDone(error_t error)
	{

	}
	
#endif
	//参数需要根据实际调节，参考小车说明书。
  event void Timer0.fired()
  {
    // test car options
	option ++;
	switch (option) {
      case 0:  //0xd0
      {
        call Leds.set(0);
        break;
      }
	  case 1: 
      {
		call CarControl.start();
		call Leds.set(1);
		break;
      }
      case 2:
      {
			call Car.Forward(400); 
			call Leds.set(2);
			break;
      }
      case 3:
      {
			call Car.Angle(1800);
			call Leds.set(3);
		break;
      }
	  case 4:
      {
			call Car.Angle(3800);
			call Leds.set(4);
		
        break;
      }
	  case 5:
      {
			call Car.Back(400);
			call Leds.set(5);
   
		break;
      }
	  case 6:
      {
			call Car.Pause(); 
 
			call Leds.set(6);
      
		break;
      }
	  
	  case 7:
      {
      
		call Car.QuiryReader(100);
		call Leds.set(7);
	
		option = 0;
		break;
      }
	  default:
        break;
    }
	
  }
  
  event void Timer1.fired()
  {
    dbg("BlinkC", "Timer 1 fired @ %s \n", sim_time_string());
	
  }
  
  event void Timer2.fired()
  {
    dbg("BlinkC", "Timer 2 fired @ %s.\n", sim_time_string());
  }
  
}

