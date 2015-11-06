#include "printf.h"
#include "Beacon.h"

module problemA_M {
  uses {
    interface Boot;
    interface SplitControl;
    interface Timer<TMilli> as OutTimer;    
    interface Timer<TMilli> as HelloTimer;
    interface Timer<TMilli> as MessageTimer;
    interface Timer<TMilli> as RouteTimer;
    interface AMSend;  //任务包的发送接收
    interface Receive;
    interface AMSend as SendHello;  //hello帧的发送与接收
    interface Receive as ReceiveHello;
    interface AMSend as RouteSend;  //发送本地路由跳数
    interface Receive as RouteReceive;
    interface AMPacket;
    interface Random;
  }
}

implementation {
  
 message_t pkt;
 message_t* p_pkt;
 //消息队列，记录本节点要发送的消息
 message_t* msgQueue[QUEUE_SIZE];

 uint8_t head,tail;
 uint16_t src;
 uint16_t dest;
 uint16_t serialnumber;
 uint16_t oldserialnumber = 0;
 uint16_t newserialnumber = 0;

 uint16_t seq = 0;
 uint16_t seqs[NODE_SIZE];  //其他节点数据包的序号

//cache只是记录已经发送过的帧，防止重复，形成环
 BeaconMsg cache_table[CACHE_SIZE];

 //本地路由表
 Route_table route_table[NODE_SIZE];

 bool busy = FALSE;

 uint16_t i;
 uint16_t j;
 uint8_t k;

 void sendHello(am_addr_t addr);

 bool enQueue(message_t* msg);
 message_t* deQueue();
 bool isQueueFull();
 bool isQueueEmpty();
  
  event void Boot.booted() {
   
    call SplitControl.start();

    for(i = 0; i < CACHE_SIZE; i++){
      cache_table[i].nodeidi = INVALIDATE_NODE_ID;
      cache_table[i].nodeidk = INVALIDATE_NODE_ID;
      cache_table[i].serialnumber = -1; 
    }


    j = 0;
    k = 0;

    head=tail=0;

    //初始化本地路由表
    for(i = 1; i < NODE_SIZE; i++){
      if(i == TOS_NODE_ID ){  //目的地址是本节点
        route_table[i].nexthop = i;
        route_table[i].hops = 0;  
      }
     
      route_table[i].nexthop = INVALIDATE_NODE_ID;
      route_table[i].hops = INVALIDATE_HOPS;  
    }

    for(i = 1 ;i < NODE_SIZE; i++){
      seqs[i] = -1;
    }
  
  }

  void sendHello(am_addr_t addr){
    send_hello* sendhello = (send_hello*) (p_pkt->data);
    sendhello->hello = HELLO;

    call SendHello.send(addr,p_pkt,sizeof(send_hello));
  }
  
  event void SplitControl.startDone(error_t err) {
    uint8_t startTime = call Random.rand16() % 5; //控制发送hello的起始时间

    if (startTime == 0) startTime = 1;

    if (err == SUCCESS) {
      p_pkt = &pkt;
      call OutTimer.startPeriodicAt(9000,30);
      call HelloTimer.startOneShot(startTime * 1000);  //这里要设置时间在10秒内
      call RouteTimer.startPeriodicAt(6000, 800);
      call MessageTimer.startPeriodicAt(9000,60);   //定期扫描
    } else {
      call SplitControl.start();
    }
  }
  
  event void SplitControl.stopDone(error_t err) {}

  bool isCached(am_addr_t src,am_addr_t dest){
    for(i=0;i<CACHE_SIZE;i++){
      if((cache_table[i].nodeidi == src ) && (cache_table[i].nodeidk == dest))
        return TRUE;
    }
    return FALSE;
  }
 
  void add_cache(am_addr_t src,am_addr_t dest, uint16_t serialnumber){
    cache_table[j].nodeidi = src;
    cache_table[j].nodeidk = dest;
    cache_table[j].serialnumber = serialnumber;
    j++; 
  }

 void update_route_item(am_addr_t dest,am_addr_t nexthop, uint16_t hops){
   uint8_t i = dest;
   route_table[i].nexthop = nexthop;
   route_table[i].hops = hops;
 }

 uint8_t get_nexthop(am_addr_t dest){
  uint8_t i = dest;
  return route_table[i].nexthop;
 }

 bool isQueueFull(){
  if(head == (tail+1)%QUEUE_SIZE){
    return TRUE;
  }
  return FALSE;
 }

 bool isQueueEmpty(){
  if(head == tail)
    return TRUE;
  return FALSE;
 }

 bool enQueue(message_t* msg){
  if(!isQueueFull()){
    msgQueue[tail] = msg;
    tail = (tail + 1) % QUEUE_SIZE;
    return TRUE;
    }
  return FALSE;
  }

//出队得到的元素要判断下是否为NULL
message_t* deQueue(){
  message_t* message;
  if(!isQueueEmpty()){
    message = msgQueue[head];
    head = (head+1) % QUEUE_SIZE;
  }
  return message;
}
event void OutTimer.fired(){
  if((newserialnumber - oldserialnumber) != 0){
    printf("GreenOrbs  %x\n",newserialnumber);
    printfflush();
    oldserialnumber = newserialnumber;
  }
}

  event void HelloTimer.fired(){
     //初始时每个节点广播一个hello帧
    sendHello(TOS_BCAST_ADDR);
  }

event void RouteTimer.fired(){

   if(TOS_NODE_ID % 2 == 0){  //奇数和偶数节点轮流广播
    send_route_table* send_route = (send_route_table*) p_pkt->data;
    send_route->seq = seq++;
    for(i=1;i<NODE_SIZE;i++){
      send_route->hops[i] = route_table[i].hops;
    }

    call RouteSend.send(TOS_BCAST_ADDR,p_pkt,sizeof(send_route_table));
  }

}
  //扫描消息队列 
  event void MessageTimer.fired(){
     BeaconMsg* msg;
    if(!isQueueEmpty()){
      if(!busy){
        p_pkt = deQueue();
        msg = (BeaconMsg*) p_pkt -> data;
        dest = msg->nodeidk;
        
        call AMSend.send(get_nexthop(dest),p_pkt,sizeof(BeaconMsg));
        busy = TRUE;
      }
    }
  }


  event message_t* ReceiveHello.receive(message_t* msg, void* payload, uint8_t len){
    if(len == sizeof(send_hello)){
      send_hello* receiveHello_msg = (send_hello*) payload;
      am_addr_t src = call AMPacket.source(msg);

      if(receiveHello_msg->hello == HELLO){
         // add_neighboor(src);  //接收到邻居发来的hello帧，添加到邻居表中
         update_route_item(src,src,1);
      } 
    }

    return msg;
  }
  
  //接收到邻接节点的路由表信息
  event message_t* RouteReceive.receive(message_t* msg, void* payload, uint8_t len){
    if(len == sizeof(send_route_table)){
        send_route_table* rec_table = (send_route_table*) payload;
        am_addr_t src = call AMPacket.source(msg);
        i = src;
        if(rec_table->seq > seqs[i]){ //收到的是最新的路由信息
          for(i = 1;i < NODE_SIZE; i++){
            if(route_table[i].hops > (rec_table->hops[i] + 1)){  //本地的跳数大于接收的包中的跳数
              update_route_item(i,src,rec_table->hops[i] + 1);
            }
          }
          seqs[i] = rec_table[i].seq;
        }
    }

    return msg;
  }

  event message_t* Receive.receive(message_t* bufPtr, void* payload, uint8_t len) {
   
   if(len == sizeof(BeaconMsg)){
      BeaconMsg* bMsg = (BeaconMsg*) payload;
     // am_addr_t me = call AMPacket.address();
      src = bMsg->nodeidi;
      dest = bMsg->nodeidk;
      serialnumber = bMsg->serialnumber;

      if(isCached(src,dest)){  //如果消息已经接收过，再次接收不做任何处理
          return bufPtr; 
      }else{
        add_cache(src,dest,serialnumber); 
      }

      if(TOS_NODE_ID == src){

        if(!busy){
         call AMSend.send(get_nexthop(dest), bufPtr, sizeof(BeaconMsg));
          busy = TRUE;
        }else{  //发送未完成，入队稍后发送
          enQueue(bufPtr);
        }
      
      }else if(TOS_NODE_ID == dest){
        newserialnumber = bMsg->serialnumber;
    
      }
  }
    return bufPtr;
  }

  event void AMSend.sendDone(message_t* msg, error_t error){
    busy = FALSE;
  }

  event void SendHello.sendDone(message_t* msg, error_t err){}

  event void RouteSend.sendDone(message_t* msg, error_t err){}
}

