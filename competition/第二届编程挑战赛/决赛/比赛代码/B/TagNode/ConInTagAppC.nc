configuration ConInTagAppC{}
implementation
{
  components MainC,ConInTagC,LedsC;
  components ActiveMessageC;
  components new TimerMilliC() as Timer0;

  components new AMSenderC(AM_TYPE_C) as AMSenderControl;
  components new AMReceiverC(AM_TYPE_C) as AMReceiverControl;

  components new AMSenderC(AM_TYPE_T) as AMSenderTag;
  components new AMReceiverC(AM_TYPE_T) as AMReceiverTag;

  ConInTagC->MainC.Boot;
  ConInTagC.Leds->LedsC;
  ConInTagC.Timer0 -> Timer0;

  ConInTagC.RadioControl->ActiveMessageC;

  ConInTagC.AMSendControl->AMSenderControl;
  ConInTagC.AMReceiveControl->AMReceiverControl;

  ConInTagC.AMSendTag->AMSenderTag;
  ConInTagC.AMReceiveTag->AMReceiverTag;
}
