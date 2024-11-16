#include <stdio.h>
#include <unistd.h>

int main(int argc, char** argv){
	int i;
	for(i=0;i<3;i++){
		printf("inainte %d %d\n",getpid(),getppid());
		fork();
		printf("dupa %d %d\n",getpid(),getppid());
	}
	return 0;
}
