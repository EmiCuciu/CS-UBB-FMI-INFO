#include <stdio.h>
#include <pthread.h>

int n=100;

void *f(void *a){
	int i;
	for(i=0;i<n;i++){
		printf("%s\n", (char*)a);
	}

	return NULL;
}


int main(int argc,char** argv){
	int i;
	pthread_t ta,tb;
	pthread_create(&ta, NULL, f, "fa");
	pthread_create(&tb, NULL, f, "fb");
	for(i=0;i<n;i++){
		printf("main\n");
	}
	pthread_join(ta, NULL);
	pthread_join(tb, NULL);
	return 0;
}

/* gcc -Wall -g -o thrd thrd.c -lpthread
   pt examen -lpthread ca gcc de pe puttynu stie sa adauge biblioteca automat

ex: lib"abcd" (fara "").a sau .so
	-l abcd
*/
