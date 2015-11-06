//author :rubin
//@greenorbs


#include "Timer.h"
#include "UART0MSG.h"
#define WARM_UP_PERIOD (100*1024UL)

#define CMD_LENTH   7      //定义发送命令长度


#define SERVO       1      //角度命令
#define FORWARD     2      //前进命令
#define BACK        3      //后退命令
#define STOP        4      //停止命令
#define QUIRYREADER 11     //读阅读器命令


#define INIT_SERVO_LEFT   5
#define INIT_SERVO_RIGHT  6
#define INIT_SERVO_MID   7
#define INIT_MOTOR_MAX   8
#define INIT_MOTOR_MIN   9

  


module CarP 
{
    provides interface SplitControl as CarControl;
    provides interface Car;
	uses
	{
	    interface Resource as uart0resource;  
        interface HplMsp430Usart as Usart;
        interface HplMsp430UsartInterrupts as Interrupts;
	    interface HplMsp430GeneralIO as P20;
        interface Leds;
	    interface Timer<TMilli> as Timer1;
    }
}

implementation 
{

	bool isStart = FALSE;        //启动标志位
	bool isSend = FALSE;
	error_t statetag = FAIL;
	uint8_t  CarCommand[CMD_LENTH] = {1,2,1,0,0,255,255};  //初始命令
	bool isUartDataOk = FALSE;
	uint8_t rcvSyncDataSum = 0;
	uint8_t SyncData[SYNC_DATA_SUM] = {255, 255};  //同步头
	uint8_t recdata[MAX_DATA]; 
	uint8_t nreading = 0; 
	bool isBufFull = FALSE;
	uint16_t tagdata = 0;
	uint32_t co2OnInstant = 0;
	uint32_t co2OnTime = 0;
    uint8_t cmdlength = 0;

	uint16_t noDataTime=0;

 	task void recvTask();
    //430 uart 配置
	msp430_uart_union_config_t config1 = 
	{
		{utxe : 1, 
		 urxe : 1, 
		 ubr : UBR_1MHZ_115200, //Baud rate (use enum msp430_uart_rate_t for predefined rates)
		 umctl : UMCTL_1MHZ_115200, //Modulation (use enum msp430_uart_rate_t for predefined rates)
		 ssel : 0x02,        //Clock source (00=UCLKI; 01=ACLK; 10=SMCLK; 11=SMCLK)
		 pena : 0,           //Parity enable (0=disabled; 1=enabled)
		 pev : 0,            //Parity select (0=odd; 1=even)
		 spb : 0,            //Stop bits (0=one stop bit; 1=two stop bits)
		 clen : 1,           //Character length (0=7-bit data; 1=8-bit data)
		 listen : 0,         //Listen enable (0=disabled; 1=enabled, feed tx back to receiver)
		 mm : 0,             //Multiprocessor mode (0=idle-line protocol; 1=address-bit protocol)
		 ckpl : 0,           //Clock polarity (0=normal; 1=inverted)
		 urxse : 0,           //Receive  -edge detection (0=disabled; 1=enabled)
		 urxeie : 0,          //Erroneous-character receive (0=rejected; 1=recieved and URXIFGx set)
		 urxwie : 0,          //Wake-up interrupt-enable (0=all characters set URXIFGx; 1=only address sets URXIFGx)     
		 utxe : 1,            // 1:enable tx module
		 urxe : 1             // 1:enable rx module
		}
    };
	 
	    
     void SendString(uint8_t*ptr, uint8_t len)
     {   
        uint8_t i = 0;   
        for(i =0;  i < len; i++,*ptr++)
        {
            call Usart.tx(*ptr);
            while(!call Usart.isTxEmpty()); //判断是否发送完毕
	   }
    }
	async event void Interrupts.txDone()
    {
    }  
	command error_t CarControl.start()
	{
		isStart = TRUE;							//标志小车控制开始
		signal CarControl.startDone(SUCCESS);	//往上层发送控制开始完成信号
		return SUCCESS;	
	}
	
	command error_t CarControl.stop()
	{
	    isStart = FALSE;        //标记小车控制关闭
	    isSend = FALSE;
		signal CarControl.stopDone(SUCCESS);
        return SUCCESS;	
	}
	
 	command error_t Car.Angle(uint16_t value)
	{
        cmdlength = 7;
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02;
		if(isStart == TRUE &&  isSend == FALSE)
        {	
			isSend = TRUE;
			CarCommand[2] = SERVO;   					//舵机角度选择位
			CarCommand[4] =  (uint8_t) (value & 0x00ff); 	
			value = value >> 8;
			CarCommand[3] =  (uint8_t)value;
    		call uart0resource.request();      	//申请uart总线资源
        }
        else
            return EBUSY;      
		return SUCCESS;
	}
	
	command error_t Car.Forward(uint16_t value)
	{
		
		cmdlength = 7;
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02;
		if(isStart == TRUE && isSend == FALSE)
		{
			isSend = TRUE;
			
			CarCommand[2] = FORWARD; //前进标志
			CarCommand[4] = (uint8_t)(value & 0x00ff); 
			value = value >> 8;
			CarCommand[3] = (uint8_t) value;
			call uart0resource.request(); //申请uart总线资源
		}
		else
			return EBUSY;
		return SUCCESS;
	}
	
	command error_t Car.Back(uint16_t value)
	{
		cmdlength = 7;
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02;
		if(isStart == TRUE && isSend == FALSE)
		{
			isSend = TRUE;
			CarCommand[2] = BACK; //后退标志
			CarCommand[4] = (uint8_t)(value & 0x00ff); 
			value = value >> 8;
			CarCommand[3] = (uint8_t) value;
			call uart0resource.request(); //申请uart总线资源
		}
		else
			return EBUSY;
		return SUCCESS;	
	}
	//读取阅读器命令
	
	command error_t Car.QuiryReader(uint8_t value)
	{
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02;
		cmdlength = 4;
		if(isStart == TRUE && isSend == FALSE)
		{
			isSend = TRUE;
			CarCommand[2] = QUIRYREADER; 
			CarCommand[3] =  value;
			call uart0resource.request();//申请uart总线资源
		}
		else
		{
			nreading=0;
			isBufFull=FALSE;
			isUartDataOk=FALSE;
			return EBUSY;
		}
		return SUCCESS;	
	}
	
	command error_t Car.Pause()
	{
		cmdlength = 7;
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02;
		if(isStart == TRUE && isSend == FALSE)
		{
			isSend = TRUE;
			CarCommand[2] = STOP; //停止命令
			CarCommand[3] = 0x00;
			CarCommand[4] = 0x00;
			call uart0resource.request(); //申请uart总线资源
		}
		else
			return EBUSY;
		return SUCCESS;
	}

    //初始化最大速度
	command error_t Car.InitMaxSpeed(uint16_t value)
    {
        cmdlength = 7;
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02;
	    if(isStart == TRUE &&  isSend == FALSE)
        {   
            isSend = TRUE;
            CarCommand[2] = INIT_MOTOR_MAX;                 //初始化最大速度边界命令
            CarCommand[4] =  (uint8_t) (value & 0x00ff);    
            value = value >> 8;
            CarCommand[3] =  (uint8_t)value;
            call uart0resource.request();       //申请uart总线资源
        }
        else
            return EBUSY;      
        return SUCCESS;
    }
    
   command error_t Car.InitMinSpeed(uint16_t value)
    {
        cmdlength = 7;
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02;
	    if(isStart == TRUE &&  isSend == FALSE)
        {   
            isSend = TRUE;
            CarCommand[2] = INIT_MOTOR_MIN;                 //初始化最小速度边界命令
            CarCommand[4] =  (uint8_t) (value & 0x00ff);    
            value = value >> 8;
            CarCommand[3] =  (uint8_t)value;
            call uart0resource.request();       //申请uart总线资源
        }
        else
            return EBUSY;      
        return SUCCESS;
    }
    
    command error_t Car.InitLeftServo(uint16_t value)
    {
       	cmdlength = 7;
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02; 
	    if(isStart == TRUE &&  isSend == FALSE)
        {   
            isSend = TRUE;
            CarCommand[2] = INIT_SERVO_LEFT;               //初始化最小左转命令
            CarCommand[4] =  (uint8_t) (value & 0x00ff);    
            value = value >> 8;
            CarCommand[3] =  (uint8_t)value;
            call uart0resource.request();       //申请uart总线资源
        }
        else
            return EBUSY;      
        return SUCCESS;
    }
    
    command error_t Car.InitRightServo(uint16_t value)
    {
        
		cmdlength = 7;
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02; 
	    if(isStart == TRUE &&  isSend == FALSE)
        {   
            isSend = TRUE;
            CarCommand[2] = INIT_SERVO_RIGHT;               //初始化最小右转命令
            CarCommand[4] =  (uint8_t) (value & 0x00ff);    
            value = value >> 8;
            CarCommand[3] =  (uint8_t)value;
            call uart0resource.request();      //申请uart总线资源
        }
        else
            return EBUSY;      
        return SUCCESS;
    }
    
    command error_t Car.InitMidServo(uint16_t value)
    {
      	cmdlength = 7;
		CarCommand[0] = 0x01;
		CarCommand[1] = 0x02; 
	    if(isStart == TRUE &&  isSend == FALSE)
        {   
            isSend = TRUE;
            CarCommand[2] = INIT_SERVO_MID;                 //初始化中间角度命令
            CarCommand[4] =  (uint8_t) (value & 0x00ff);    
            value = value >> 8;
            CarCommand[3] =  (uint8_t)value;
            call uart0resource.request();       //申请uart总线资源
        }
        else
            return EBUSY;      
        return SUCCESS;
    }
 
	
	event void uart0resource.granted()
	{
		call Usart.setModeUart(&config1);  	//载入uart模式
		call Usart.enableUart();   			//使能usart模式
		call Usart.enableRxIntr();   		//使能接收中断
		U0CTL &= ~SYNC;      				//切换到usart0模式
		SendString(CarCommand,CMD_LENTH);
		if(cmdlength==4)
			call Timer1.startOneShot(1000);
		else
		{	call uart0resource.release();   // 注意必须释放总线资源
			isSend = FALSE;
		}
	}
	
  	event void Timer1.fired()
    {
		statetag = FAIL;
		post recvTask();
	}
	//接收中断
    async event void Interrupts.rxDone(uint8_t data)
    {
		//判断 0xff 0xff
		
		if(isUartDataOk == FALSE &&(call uart0resource.isOwner() == TRUE))
		{
			if(SyncData[rcvSyncDataSum] == data)
			{
			   rcvSyncDataSum++;
			   if(rcvSyncDataSum == SYNC_DATA_SUM)
			   {
				  isUartDataOk = TRUE;
				  nreading = 0;
			   }
			}
			else
			{
				rcvSyncDataSum = 0;
			}
			return;
		}
	
		if(isBufFull == FALSE &&isUartDataOk == TRUE&&(call uart0resource.isOwner() == TRUE))
		{
			if(nreading < MAX_DATA)
			{
				recdata[nreading++] = data;
			}
			if(nreading == MAX_DATA)
			{             
				isBufFull = TRUE;
				call Timer1.stop();
				
				//匹配反馈的数据
				if((recdata[0] ==0x00) && (recdata[1] == 0x00))	
				{
					//通过阅读器读取标签信息 
					tagdata = recdata[MAX_DATA-2]*256 + recdata[MAX_DATA-1];
					statetag = SUCCESS;
					post recvTask();
				}
				else
				{
					statetag = FAIL;	
					post recvTask();	
				}
			}
		}
	}
  
	task void recvTask()
    {
		call Timer1.stop();
		isBufFull = FALSE;
		isUartDataOk = FALSE;
		rcvSyncDataSum = 0;
		nreading = 0;
		if (statetag == FAIL)
		{
			signal Car.readDone(statetag,0xFFFF);
			isSend = FALSE;
		}
		else
		{
			signal Car.readDone(statetag,tagdata);
			isSend = FALSE;
		}
		call uart0resource.release();
    }
}

