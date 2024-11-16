#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
    int c;
    struct sockaddr_in server;
    char sir[101], sir_oglindit[101];
    socklen_t server_len;

    c = socket(AF_INET, SOCK_DGRAM, 0);
    if (c < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    printf("Introduceți un șir de caractere (max 100): ");
    fgets(sir, 101, stdin);

    sendto(c, sir, strlen(sir) + 1, 0, (struct sockaddr*)&server, sizeof(server));

    server_len = sizeof(server);
    recvfrom(c, sir_oglindit, sizeof(sir_oglindit), MSG_WAITALL, (struct sockaddr*)&server, &server_len);

    printf("Șirul oglindit primit de la server: %s\n", sir_oglindit);

    close(c);

    return 0;
}
