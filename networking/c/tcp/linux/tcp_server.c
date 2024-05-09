#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

int main() {
    int serverSocket = socket(AF_INET, SOCK_STREAM, 0);
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
    if (listen(serverSocket, 10) == -1) {
        perror("Listen failed");
        close(serverSocket);
        exit(EXIT_FAILURE);
    }
    struct sockaddr_in clientAddr;
    socklen_t clientAddrLen = sizeof(clientAddr);
    int clientSocket;
    while (1) {
        clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddr, &clientAddrLen);
        if (clientSocket == -1) {
            perror("Accept failed");
            close(serverSocket);
            exit(EXIT_FAILURE);
        }
        char buffer[1024];
        ssize_t bytesReceived = recv(clientSocket, buffer, sizeof(buffer), 0);
        if (bytesReceived == -1) {
            perror("Receive failed");
            close(clientSocket);
            continue;
        }
        buffer[bytesReceived] = '\0';
        printf("Received message from client: %s\n", buffer);
        ssize_t bytesSent = send(clientSocket, buffer, bytesReceived, 0);
        if (bytesSent == -1) {
            perror("Send failed");
        }
        close(clientSocket);
    }
    close(serverSocket);
    return 0;
}