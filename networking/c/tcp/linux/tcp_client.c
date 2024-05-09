#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

int main() {
    int clientSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (clientSocket == -1) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }
    struct sockaddr_in serverAddr;
    int port = 6969;
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    struct hostent *host = gethostbyname("server_hostname_or_ip");
    if (host == NULL) {
        fprintf(stderr, "Unknown host\n");
        close(clientSocket);
        exit(EXIT_FAILURE);
    }
    memcpy(&serverAddr.sin_addr.s_addr, host->h_addr, host->h_length);
    if (connect(clientSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) == -1) {
        perror("Connection failed");
        close(clientSocket);
        exit(EXIT_FAILURE);
    }
    char buffer[1024];
    printf("Enter message to send: ");
    fgets(buffer, sizeof(buffer), stdin);
    ssize_t bytesSent = send(clientSocket, buffer, strlen(buffer), 0);
    if (bytesSent == -1) {
        perror("Send failed");
        close(clientSocket);
        exit(EXIT_FAILURE);
    }
    ssize_t bytesReceived = recv(clientSocket, buffer, sizeof(buffer), 0);
    if (bytesReceived == -1) {
        perror("Receive failed");
        close(clientSocket);
        exit(EXIT_FAILURE);
    }
    buffer[bytesReceived] = '\0';
    printf("Received message from server: %s\n", buffer);
    return 0;
}