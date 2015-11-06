#ifndef CarSendMsg_H_
#define CarSendMsg_H_

enum
{
   AM_TYPE_C = 0xBB,
   AM_TYPE_T = 0x99,
};

typedef nx_struct car_send_msg {
  nx_uint8_t  control;
  nx_uint16_t  value;
}car_send_msg_t;

typedef nx_struct tag_msg {
  nx_uint16_t  tagid;
  nx_uint16_t  value;
}tag_msg_t;

typedef nx_struct TagMsg {
  nx_uint16_t  tagid;
  nx_uint16_t  tagdata;
  nx_uint16_t  hint;
}TagMsg;

#endif
