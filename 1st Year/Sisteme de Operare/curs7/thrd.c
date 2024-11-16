#include <stdio.h>
#include <pthread.h>

void *f(void *a){
	printf("f\n");
	return NULL;
}

int main(int argc,char** argv){
	pthread_t t;
	pthread_create(&t, NULL, f, NULL);
	printf("main\n");
	pthread_join(t, NULL);
	return 0;
}

/* gcc -Wall -g -o thrd thrd.c -lpthread
   pt examen -lpthread ca gcc de pe puttynu stie sa adauge biblioteca automat

ex: lib"abcd" (fara "").a sau .so
	-l abcd
*/
