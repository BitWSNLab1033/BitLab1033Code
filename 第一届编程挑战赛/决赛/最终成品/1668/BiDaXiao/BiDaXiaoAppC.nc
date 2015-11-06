#include "BiDaXiao.h"
configuration BiDaXiaoAppC{}
implementation{
  components BiDaXiaoC,MainC,LedsC;
  components new TimerMilliC() as Timer0;

  components ActiveMessageC;
  components SerialActiveMessageC;
  components new SerialAMReceiverC(AM_TYPE);
  components new AMReceiverC(COLLECTION_ID);
  components new AMSenderC(COLLECTION_ID);
 
  BiDaXiaoC.Boot -> MainC;
  BiDaXiaoC.Timer0 -> Timer0;
  BiDaXiaoC.AMPacket -> ActiveMessageC;
  BiDaXiaoC.SerialPacket -> SerialActiveMessageC;

  BiDaXiaoC.AMControl -> ActiveMessageC;
  BiDaXiaoC.AMReceive -> AMReceiverC;
  BiDaXiaoC.AMSend -> AMSenderC;

  BiDaXiaoC.SerialControl -> SerialActiveMessageC;
  BiDaXiaoC.SerialReceive -> SerialAMReceiverC;
  
}
