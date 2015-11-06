configuration ConInCarAppC
{
}
implementation
{
  components MainC, ConInCarC, LedsC;
  components new TimerMilliC() as Timer0;
  components CarC;  

  components ActiveMessageC;
  components new AMSenderC(AM_TYPE_T);
  components new AMReceiverC(AM_TYPE_C);

  ConInCarC.Car -> CarC.Car;
  ConInCarC.CarControl -> CarC.CarControl; 

  ConInCarC -> MainC.Boot;
  ConInCarC.Timer0 -> Timer0;
  ConInCarC.Leds -> LedsC;

  ConInCarC.RadioControl -> ActiveMessageC;
  ConInCarC.AMSend -> AMSenderC;
  ConInCarC.AMReceive -> AMReceiverC;

}

