#ifndef BEACON_H
#define BEACON_H 

#define HELLO_NEIGHBOOR 10
#define BROADCAST_ROUTE     11
enum {
  AM_BEACON = 6,
  CACHE_SIZE = 200,
  INVALIDATE_NODE_ID = 0xffff,
  INVALIDATE_HOPS = 0Xff,
  NODE_SIZE = 50, 
  QUEUE_SIZE = 50,
  HELLO = 0xaa
};

typedef nx_struct BeaconMsg {
  nx_uint16_t nodeidi;
  nx_uint16_t nodeidk;
  nx_uint16_t serialnumber;
} BeaconMsg;


typedef nx_struct{
	nx_uint16_t hello;
} send_hello;

typedef nx_struct{
  nx_uint16_t seq;
  nx_uint8_t hops[NODE_SIZE];  //一个节点与其他48个节点间的跳数
} send_route_table;

typedef struct 
{
  //uint8_t dest; //到目的节点，按照1-49确定的目的节点，之后试用数组下标
  uint16_t nexthop;
  uint16_t hops;
} Route_table;

#endif
