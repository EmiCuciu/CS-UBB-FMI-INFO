#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>

int main(int argc, char **argv)
{
    int FtoP[2];
    pipe(FtoP);

    if (argc != 2)
    {
        printf("Trebuie exact un argument\n");
    }

    if (fork() == 0)
    { // Fiu
        int fd = open(argv[1], O_RDONLY);
        char f;
        int readBytes;
        int shouldConvert = 0;

        char newChar;

        do
        {
            readBytes = read(fd, &f, sizeof(f));
            if (readBytes <= 0)
            {
                break;
            }
            // De aici logica

            newChar = f;
            if (f == '.')
            {
                shouldConvert = 1;
            }
            else if ((f >= 'a' && f <= 'z') && shouldConvert == 1)
            {
                shouldConvert = 0;
                newChar = f - 32;
            }
            else if (f == ' ')
            {
                ; // Nimic
            }
            else
            {
                shouldConvert = 0;
            }
            write(FtoP[1], &newChar, sizeof(newChar));
            // printf("%c",newChar);
            close(FtoP[0]);
        } while (1);

        newChar = 127;
        write(FtoP[1], &newChar, sizeof(newChar));
        // printf("\n");
    }
    else
    { // Parinte
        char primit;
        do
        {
            read(FtoP[0], &primit, sizeof(primit));
            if (primit == 127)
            {
                // printf("\n");
                break;
            }
            else
            {
                printf("%c", primit);
            }
            close(FtoP[1]);
        } while (1);
    }
}
