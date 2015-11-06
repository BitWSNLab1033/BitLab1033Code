configuration ConInPCAppC
{
}
implementation
{
  components MainC, ConInPCC, LedsC;
  components new TimerMilliC() as Timer0;
 
  components ActiveMessageC;
  components SerialActiveMessageC;
  components new AMSenderC(AM_TYPE_C);
  components new AMReceiverC(AM_TYPE_T);
  components new SerialAMReceiverC(AM_TYPE_C);
  components new SerialAMSenderC(AM_TYPE_T);

  ConInPCC -> MainC.Boot;
  ConInPCC.Timer0 -> Timer0;
  ConInPCC.Leds -> LedsC;

  ConInPCC.RadioControl -> ActiveMessageC;
  ConInPCC.AMSend -> AMSenderC;
  ConInPCC.AMReceive -> AMReceiverC;

  ConInPCC.SerialControl -> SerialActiveMessageC;
  ConInPCC.SerialAMReceive -> SerialAMReceiverC;
  ConInPCC.SerialAMSend -> SerialAMSenderC;

}

