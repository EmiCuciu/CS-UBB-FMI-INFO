#include <stdio.h>
#include <pthread.h>

int n=100;

void *f(void *a){
	int i;
	for(i=0;i<n;i++){
		printf("%d\n", *(int*)a);
	}

	return NULL;
}


int main(int argc,char** argv){
	int i;
	pthread_t t[10];
	int a[10];
	for(i=0;i<10;i++){
		a[i]=i;
		pthread_create(&t[i], NULL, f, &a[i]);
	}
	for(i=0;i<n;i++){
		printf("main\n");
	}
	for(i=0;i<10;i++){
		pthread_join(t[i],NULL);
	}
	return 0;
}

/* gcc -Wall -g -o thrd thrd.c -lpthread
   pt examen -lpthread ca gcc de pe puttynu stie sa adauge biblioteca automat

ex: lib"abcd" (fara "").a sau .so
	-l abcd
*/
