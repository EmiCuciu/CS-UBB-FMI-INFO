#include <stdio.h>
#include <signal.h>
#include <stdlib.h>
#include <string.h>

int children[3];
void hp(int sgn)
{
    int i;
    if (sgn == SIGUSR1)
    {
        printf("%d: sending SIGUSR2\n", getpid());
        for (i = 0; i < 3; i++)
        {
            kill(children[i], SIGUSR2);
        }
    }
    else if (sgn == SIGUSR2)
    {
        printf("%d: sending SIGKILL\n", getpid());
        for (i = 0; i < 3; i++)
        {
            kill(children[i], SIGKILL);
        }
    }
}
void hc(int sgn)
{
    if (sgn == SIGUSR2)
    {
        printf("%d: Received SIGUSR2\n", getpid());
    }
}
int main(int argc, char **argv)
{
    int i;
    signal(SIGUSR1, hp);
    signal(SIGUSR2, hp);
    for (i = 0; i < 3; i++)
    {
        children[i] = fork();
        if (children[i] == 0)
        {
            signal(SIGUSR2, hc);
            while (1)
                ;
            exit(0);
        }
    }
    wait(0);
    wait(0);
    wait(0);
    return 0;
}