#ifndef TEST_NETWORK_H
#define TEST_NETWORK_H

#include <AM.h>
#include "TestNetworkC.h"

typedef nx_struct BeaconMsg {
	nx_uint16_t serialnumber;
	nx_uint8_t data[75];

} BeaconMsg;

#endif
