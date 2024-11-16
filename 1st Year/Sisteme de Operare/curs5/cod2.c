#include <stdio.h>
#include <signal.h>
#include <sys/wait.h>

void f(int sign){
	printf("ha ha \n");
	wait(0);
}

//signal(SIGCHLD ,f);
//signal(SIGCHLD,SIG_IGN);

int main(int argc,char** argv){	
	signal(SIGINT, f);
	while(1);
	return 0;
}

/*
*	nu se opreste pana nu ii dau kill -SIGINT (si dau ps -u emi ca 
*	sa aflu ce PID are)
	ruleaza sa vezi ce face
*/
