#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/wait.h>

void f(int sign){
	wait(0);
}

signal(SIGCHLD,SIG_IGN);

int main(int argc, char** argv){
	while(1){
		get req
		if(fork()==0){
			proces req
			send req
			exit(0);
		}
	}
}
