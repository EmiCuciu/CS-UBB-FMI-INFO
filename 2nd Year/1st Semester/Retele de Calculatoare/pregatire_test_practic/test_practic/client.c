#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>

int main() {
    int c;
    struct sockaddr_in server;
    unsigned long numar_lung;
    uint32_t numar;

    c = socket(AF_INET, SOCK_DGRAM, 0);
    if (c < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    printf("Introduceti un numar de cel mult 9 cifre\n");
    scanf("%lu", &numar_lung);
    if (numar_lung > 999999999) {
        printf("Numarul introdus este prea mare!\n");
        close(c);
        return 1;
    }

    numar = (uint32_t)numar_lung;
    numar = htonl(numar);
    sendto(c, &numar, sizeof(numar), 0, (struct sockaddr*)&server, sizeof(server));

    uint32_t suma;
    socklen_t server_len = sizeof(server);
    recvfrom(c, &suma, sizeof(suma), 0, (struct sockaddr*)&server, &server_len);
    suma = ntohl(suma);

    printf("Suma cifrelor este: %u\n", suma);

    close(c);
    return 0;
}
