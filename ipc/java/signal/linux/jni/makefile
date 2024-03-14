JAVA_HOME := $(shell echo $$JAVA_HOME)
receiver:
	gcc -fPIC -shared -o ./bin/receiver.so -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux ./src/receiver.c
sender:
	gcc -fPIC -shared -o ./bin/sender.so -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux ./src/sender.c
all: receiver sender