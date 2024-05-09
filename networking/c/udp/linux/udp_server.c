#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

int main() {
    int serverSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (serverSocket == -1) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }
    struct sockaddr_in serverAddr;
    int port = 6969;
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    serverAddr.sin_port = htons(port);
    if (bind(serverSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) == -1) {
        perror("Socket bind failed");
        close(serverSocket);
        exit(EXIT_FAILURE);
    }
    struct sockaddr_in clientAddr;
    socklen_t clientAddrLen = sizeof(clientAddr);
    char buffer[1024];
    while (1) {
        ssize_t bytesReceived = recvfrom(serverSocket, buffer, sizeof(buffer), 0, (struct sockaddr *)&clientAddr, &clientAddrLen);
        if (bytesReceived == -1) {
            perror("Receive failed");
            continue;
        }
        buffer[bytesReceived] = '\0';
        printf("Received message from client: %s\n", buffer);
        const char *responseBuffer = "Hello from server!";
        sendto(serverSocket, responseBuffer, strlen(responseBuffer), 0, (struct sockaddr *)&clientAddr, clientAddrLen);
    }
    close(serverSocket);
}