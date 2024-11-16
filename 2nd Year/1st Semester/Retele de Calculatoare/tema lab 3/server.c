#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdlib.h>

int main() {
  int s;
  struct sockaddr_in server, client;
  int c;
  socklen_t l;

  s = socket(AF_INET, SOCK_STREAM, 0);
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

  listen(s, 5);

  l = sizeof(client); 
  memset(&client, 0, sizeof(client));   

  while (1) {
    uint16_t n1, n2, sir1[100], sir2[100], rezultat[100]; 
    int i, j, k = 0;  

    c = accept(s, (struct sockaddr*)&client, &l); 
    printf("S-a conectat un client.\n");

    recv(c, &n1, sizeof(n1), MSG_WAITALL);
    recv(c, &n2, sizeof(n2), MSG_WAITALL);
    n1 = ntohs(n1);   
    n2 = ntohs(n2);

    for (i = 0; i < n1; i++) {
      recv(c, &sir1[i], sizeof(sir1[i]), MSG_WAITALL);
      sir1[i] = ntohs(sir1[i]);
    }

    for (i = 0; i < n2; i++) {
      recv(c, &sir2[i], sizeof(sir2[i]), MSG_WAITALL);
      sir2[i] = ntohs(sir2[i]);
    }

    for (i = 0; i < n1; i++) {
      for (j = 0; j < n2; j++) {
        if (sir1[i] == sir2[j]) {
          rezultat[k++] = htons(sir1[i]); 
          break;
        }
      }
    }

    uint16_t n_comun = htons(k);
    send(c, &n_comun, sizeof(n_comun), 0);
    for (i = 0; i < k; i++) {
      send(c, &rezultat[i], sizeof(rezultat[i]), 0);
    }

    close(c);
  }
}
