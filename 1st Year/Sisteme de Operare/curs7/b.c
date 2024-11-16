#include "h.h"
 
int main(int argc,char** argv){
	int shmid;
	struct absp* x;

	shmid = shmget(1234, 0, 0);

	x = shmat(shmid ,0 ,0);
	while(1){
		x->s = x->a + x->b;
		x->p = x->a * x->b;
		if(x->s == x->p){
			break;
		}
	}
	shmdt(x);
	return 0;
}
