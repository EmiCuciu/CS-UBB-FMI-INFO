#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>


int main(int argc, char** argv){
	for(int i=0;i<3;i++){
		if(fork()==0){
			sleep(10);
			exit(0);
		}
	}
	sleep(20);
	for(int i=0;i<3;i++){
		wait(0);
	}
	sleep(5);
        return 0;
}
