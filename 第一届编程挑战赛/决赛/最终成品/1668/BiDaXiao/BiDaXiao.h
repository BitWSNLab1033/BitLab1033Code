#ifndef BIDAXIAO_H
#define BIDAXIAO_H
#include <AM.h>
enum{
  COLLECTION_ID = 0xEE,
  AM_TYPE = 0xC8,
};
typedef nx_struct NetworkMsg{
  nx_uint16_t nodeid;
  nx_uint16_t data;
  nx_uint16_t data_max;
  nx_uint16_t data_min;
}NetworkMsg_t;
typedef nx_struct SerialMsg{
  nx_uint16_t nodeid;
  nx_uint16_t data;
}SerialMsg_t;
#endif
