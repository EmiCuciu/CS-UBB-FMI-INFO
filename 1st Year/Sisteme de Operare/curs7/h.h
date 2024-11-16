#include <stdio.h>
#include <stdlib.h>
#include <sys/ipc.h>
#include <sys/shm.h>

struct absp{
	int a;
	int b;
	int s;
	int p;
};
