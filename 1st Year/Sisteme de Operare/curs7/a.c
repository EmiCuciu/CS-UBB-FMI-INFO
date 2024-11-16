#include "h.h"
 
int main(int argc,char** argv){
	int shmid;
	struct absp* x;

	srand(time());	//pt nr aleatorii

	shmid = shmget(1234/*nr matricol*/ , sizeof(struct absp), IPC_CREAT|0600);
	x = shmat(shmid ,0 ,0);
	while(1){
		x->a = rand() % 100;
		x->b = rand() % 100;
		if(x->s == x->p){
			break;
		}
	}
	shmdt(x);
	shmctl(shmid, IPC_RMID, NULL);
	return 0;
}
