#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

///     MKFIFO A2B,B2A 

int main(int argc,char ** argv){
	int a2b,b2a,n;

	a2b = open("a2b",O_RDONLY);
	b2a = open("b2a",O_WRONLY);

	n=7;
	write(a2b, &n, sizeof(int));
	while(1){
		if(read(b2a, &n, sizeof(int) < 0){
			break;
		}
		if(n<=0){
			break;
		}
		printf("A: %d -> %n",n,n-1);
		n--;
		write(a2b, &n, sizeof(int));
	}
	close(a2b),close(b2a);
	return 0;
}
