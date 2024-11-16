#include <stdio.h>     // pentru printf
#include <unistd.h>    // pentru fork
#include <sys/types.h> // pentru tipul pid_t
#include <sys/wait.h>  // pentru wait
#include <stdlib.h>    // pentru exit

int main(int argc, char **argv)
{
    for (int i = 0; i < 3; i++)
    {
        pid_t pid = fork();
        if (pid == 0)
        {
            printf("Child process: %d\n",i);
            // Acesta este un proces copil. Creeaza 3 procese copil
            for (int j = 0; j < 3; j++)
            {
                pid_t child_pid = fork();
                if (child_pid == 0)
                {
                    printf("GrandChild of process: %d, child: %d\n",i, j);
                    exit(0);
                }
            }

            // Asteapta terminarea celor 3 procese copil create
            for (int i = 0; i < 3; i++)
            {
                wait(0);
            }

            exit(0);
        }
        else{
            printf("Parent process created child: %d\n",i);
        }
    }

    // Asteapta termianrea celor 3 procese copil initiale
    for (int i = 0; i < 3; i++)
    {
        wait(0);
    }
    return 0;
}