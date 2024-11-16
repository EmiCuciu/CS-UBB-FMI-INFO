#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(int argc, char** argv){
        int a[4] = {1,2,3,4};

	int p[2];				// p pentru pipe, pentru a comunica tata-fiu , pipe face legatura

	pipe(p);

	if(fork()==0){				//procesul fiu incepe de la ==0 incolo si se termina cand se inchide } ,fork nu face din nou if-ul
		close(p[0]);
		a[2] += a[3];
		write(p[1], &a[2], sizeof(int));
		close(p[1]);
	        exit(0);
        }
	close(p[1]);
	a[0] += a[1];
        read(p[0], &a[2], sizeof(int));
	close(p[0]);
	wait(0);	// pun wait ca sa astept terminarea procesului fiu
        a[0] += a[2];
	printf("%d\n", a[0]);
        return 0;
}

/*
	afiseaza 6 pt ca adunarea se intampla in procesul fiu de la fork
	rezultatul din fiu nu ajunge in parinte
	numai tatal calculeaza efectiv
	a[2]+a[3] se intampla altundeva in memorie

	REGULA: inchide pipe-urile cat mai repede posibil

*/
