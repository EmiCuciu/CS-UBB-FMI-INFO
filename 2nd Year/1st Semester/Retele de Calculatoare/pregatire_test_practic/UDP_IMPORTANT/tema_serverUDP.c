#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>

int main() {
  int s;
  struct sockaddr_in server, client;
  char sir[101], sir_oglindit[101];
  socklen_t client_len;

  s = socket(AF_INET, SOCK_DGRAM, 0);
  if (s < 0) {
    printf("Eroare la crearea socketului server\n");
    return 1;
  }

  memset(&server, 0, sizeof(server));
  server.sin_port = htons(1234);
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = INADDR_ANY;

  if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
    printf("Eroare la bind\n");
    return 1;
  }

  client_len = sizeof(client);
  recvfrom(s, sir, sizeof(sir), MSG_WAITALL, (struct sockaddr *) &client, &client_len);

  int len = strlen(sir);
  for (int i = 0; i < len; i++) {
    sir_oglindit[i] = sir[len - i - 1];
  }
  sir_oglindit[len] = '\0';

  sendto(s, sir_oglindit, strlen(sir_oglindit) + 1, 0, (struct sockaddr *) &client, client_len);

  close(s);

  return 0;
}
