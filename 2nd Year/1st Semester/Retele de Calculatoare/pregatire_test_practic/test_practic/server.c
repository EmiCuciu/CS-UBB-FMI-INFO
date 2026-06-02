#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
    int s;
    struct sockaddr_in server, client;
    int l;
    uint32_t numar, suma = 0;

    s = socket(AF_INET, SOCK_DGRAM, 0);
    if (s < 0) {
        printf("Eroare la crearea socketului server\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s, (struct sockaddr*)&server, sizeof(server)) < 0) {
        printf("Eroare la bind\n");
        return 1;
    }

    l = sizeof(client);
    memset(&client, 0, sizeof(client));
    recvfrom(s, &numar, sizeof(numar), MSG_WAITALL, (struct sockaddr*)&client, &l);
    numar = ntohl(numar);

    while (numar > 0) {
        suma += numar % 10;
        numar /= 10;
    }

    suma = htonl(suma);
    sendto(s, &suma, sizeof(suma), 0, (struct sockaddr*)&client, l);

    return 0;
}
