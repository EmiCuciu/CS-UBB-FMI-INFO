#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char** argv){
	if(fork()==0){
		if(execlp("/bin/echo","echo","asdf",NULL) == -1){
			perror("n-a mers");
			exit(0);
		}
	}
	wait(0);
	printf("gata\n");
	return 0;
}
