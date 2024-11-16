//send,recv,listen,---- recvfrom,sendto
#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
    int c;
    struct sockaddr_in server;
    uint16_t n1, n2, sir1[100], sir2[100], rezultat[100];
    int i;
    
    c = socket(AF_INET, SOCK_STREAM, 0);
    if (c < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }
    
    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("172.30.248.94");
    
    if (connect(c, (struct sockaddr*)&server, sizeof(server)) < 0) {
        printf("Eroare la conectarea la server\n");
        return 1;
    }
    
    printf("Introdu numarul de elemente pentru primul sir: ");
    scanf("%hu", &n1);  
    for (i = 0; i < n1; i++) {
        printf("sir1[%d] = ", i);
        scanf("%hu", &sir1[i]);
    }
    
    printf("Introdu numarul de elemente pentru al doilea sir: ");
    scanf("%hu", &n2);
    for (i = 0; i < n2; i++) {
        printf("sir2[%d] = ", i);
        scanf("%hu", &sir2[i]);
    }
    
    n1 = htons(n1);
    n2 = htons(n2);
    send(c, &n1, sizeof(n1), 0);
    send(c, &n2, sizeof(n2), 0);
    
    for (i = 0; i < ntohs(n1); i++) {
        sir1[i] = htons(sir1[i]);
        send(c, &sir1[i], sizeof(sir1[i]), 0);
    }
    
    for (i = 0; i < ntohs(n2); i++) {
        sir2[i] = htons(sir2[i]);
        send(c, &sir2[i], sizeof(sir2[i]), 0);
    }
    
    recv(c, &n1, sizeof(n1), 0);    
    n1 = ntohs(n1);
    for (i = 0; i < n1; i++) {
        recv(c, &rezultat[i], sizeof(rezultat[i]), 0);
        rezultat[i] = ntohs(rezultat[i]);
    }
    
    printf("Numerele comune sunt: ");
    for (i = 0; i < n1; i++) {
        printf("%hu ", rezultat[i]);
    }
    printf("\n");
    
    close(c);
}
