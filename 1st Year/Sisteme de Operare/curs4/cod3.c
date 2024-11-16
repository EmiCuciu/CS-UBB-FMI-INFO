#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>


int main(int argc, char** argv){
        int pid;
	pid = fork();
        if(pid == 0){
		printf("fiu fork return=%d  pid=%d\n",pid,getpid());
		exit(0);
	}
	printf("parinte fork return=%d \n",pid);
	wait(0);
        return 0;
}
