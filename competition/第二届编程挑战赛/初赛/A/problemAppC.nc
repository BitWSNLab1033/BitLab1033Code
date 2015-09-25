#include "Beacon.h"

configuration problemAppC {
}
implementation {
  components MainC, problemA_M, RandomC;
  components new AMSenderC(AM_BEACON);
  components new AMReceiverC(AM_BEACON);
  components new AMSenderC(HELLO_NEIGHBOOR) as SendHello;
  components new AMReceiverC(HELLO_NEIGHBOOR) as ReceiveHello ;
  components new AMSenderC(BROADCAST_ROUTE) as RouteSend;
  components new AMReceiverC(BROADCAST_ROUTE) as RouteReceive;
  components ActiveMessageC;
  
  problemA_M.Boot -> MainC.Boot;
  problemA_M.SplitControl -> ActiveMessageC;

  problemA_M.AMSend -> AMSenderC;
  problemA_M.Receive -> AMReceiverC;
  problemA_M.SendHello -> SendHello;
  problemA_M.ReceiveHello -> ReceiveHello;
  problemA_M.RouteSend -> RouteSend;
  problemA_M.RouteReceive -> RouteReceive;

  problemA_M.AMPacket -> AMSenderC;
  problemA_M.Random -> RandomC;
  
  components new TimerMilliC() as OutTimer;
  components new TimerMilliC() as HelloTimer;
  components new TimerMilliC() as MessageTimer;
  components new TimerMilliC() as RouteTimer;

  problemA_M.HelloTimer -> HelloTimer;
  problemA_M.OutTimer -> OutTimer;
  problemA_M.MessageTimer -> MessageTimer;
  problemA_M.RouteTimer -> RouteTimer;
}

