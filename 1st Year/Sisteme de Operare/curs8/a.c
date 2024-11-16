#include <stdio.h>
#include <pthread.h>
int count = 0;
pthread_mutex_t m;

void *f(void *a)
{
	int i;
	for (i = 0; i < *(int *)a; i++)
	{
		pthread_mutex_lock(&m);
		count++;
		pthread_mutex_unlock(&m);
	}
	return NULL;
}

int main(int argc, char **argv)
{
	int n, i;
	pthread_t t[5];

	sscanf(argv[1], "%d", &n);

	pthread_mutex_init(&m, NULL);

	for (i = 0; i < 5; i++)
	{
		pthread_create(&t[i], NULL, f, &n);
	}
	for (i = 0; i < 5; i++)
	{
		pthread_join(t[i], NULL);
	}

	pthread_mutex_destroy(&m);

	printf("%d\n", count);
	return 0;
}
