#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>

void deservire_client(int c) {
    uint16_t sir_length;
    
    if (recv(c, &sir_length, sizeof(sir_length), MSG_WAITALL) <= 0) {
        perror("Eroare la primirea lungimii sirului");
        close(c);
        return;
    }
    sir_length = ntohs(sir_length);
    printf("Lungimea sirului: %d\n", sir_length);

    char sir[sir_length + 1];
    if (recv(c, sir, sir_length, MSG_WAITALL) <= 0) {
        perror("Eroare la primirea sirului");
        close(c);
        return;
    }
    sir[sir_length] = '\0'; 
    printf("Sirul primit: %s\n", sir);

    char caracter;
    if (recv(c, &caracter, sizeof(caracter), MSG_WAITALL) <= 0) {
        perror("Eroare la primirea caracterului");
        close(c);
        return;
    }
    printf("Caracterul cautat: %c\n", caracter);

    int pozitii[sir_length];
    int count = 0;
    for (int i = 0; i < sir_length; i++) {
        if (sir[i] == caracter) {
            pozitii[count++] = i;
        }
    }

    uint16_t count_network = htons(count);
    send(c, &count_network, sizeof(count_network), 0);

    for (int i = 0; i < count; i++) {
        uint16_t pos_network = htons(pozitii[i]);
        send(c, &pos_network, sizeof(pos_network), 0);
    }

    close(c);
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Utilizare: %s <IP> <port>\n", argv[0]);
        return 1;
    }

    const char *ip = argv[1];
    int port = atoi(argv[2]);

    int s;
    struct sockaddr_in server, client;
    int c, l;

    s = socket(AF_INET, SOCK_STREAM, 0);
    if (s < 0) {
        perror("Eroare la crearea socketului server");
        exit(1);
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(port);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr(ip);

    if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
        perror("Eroare la bind");
        close(s);
        exit(1);
    }

    listen(s, 5);

    l = sizeof(client);
    while (1) {
        c = accept(s, (struct sockaddr *) &client, &l);
        if (c < 0) {
            perror("Eroare la accept");
            continue;
        }
        printf("S-a conectat un client.\n");
        
        if (fork() == 0) {
            close(s);
            deservire_client(c);
            exit(0);
        }
        
        close(c);
    }

    return 0;
}
