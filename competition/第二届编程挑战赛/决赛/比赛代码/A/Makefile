COMPONENT=BlinkAppC
CFLAGS += -DCC2420_DEF_CHANNEL=20
CFLAGS += -DCC2420_DEF_RFPOWER=1
CFLAGS +=-I$(TOSDIR)/lib/printf
CFLAGS +=-I$(TOSDIR)/lib/net \
         -I$(TOSDIR)/lib/net/drip \
         -I$(TOSDIR)/lib/net/4bitle \
         -I$(TOSDIR)/lib/net/ctp

TFLAGS += -I$(TOSDIR)/../apps/tests/TestDissemination \
          -I$(TOSDIR)/../support/sdk/c \
          -I$(TOSDIR)/types \
          -I.
LIBMOTE = $(TOSDIR)/../support/sdk/c/libmote.a

CFLAGS += -DTOSH_DATA_LENGTH=114
include $(MAKERULES)
