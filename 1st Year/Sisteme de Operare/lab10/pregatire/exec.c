#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

// int main(int argc, char **argv)
// {
//     execlp("grep", "grep", "-E", "gr212", "passwd", NULL);
//     printf("If grep is in the PATH, then execlp succeeds, and this will never be printed.\n");
//     return 0;
// }


int main(int argc, char** argv) {
    int pid = fork();
    if (pid == 0) {
        execlp("grep", "grep", "-E", "gr212", "passwd", NULL);
        perror("If grep is in the PATH, then execlp succeeds, and this will never be printed.\n");
        exit(1);    /// indica ca execlp a dat nu a functionat
    }
    printf("Parent\n");
    wait(0);
    return 0;
}