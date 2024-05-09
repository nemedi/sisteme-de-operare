#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int main() {
    int clientSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (clientSocket == -1) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }
    struct sockaddr_in serverAddr;
    int port = 6969;
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    serverAddr.sin_addr.s_addr = inet_addr("server_ip_address");
    char buffer[1024];
    printf("Enter message to send: ");
    fgets(buffer, sizeof(buffer), stdin);
    ssize_t bytesSent = sendto(clientSocket, buffer, strlen(buffer), 0, (struct sockaddr *)&serverAddr, sizeof(serverAddr));
    if (bytesSent == -1) {
        perror("Sendto failed");
        close(clientSocket);
        exit(EXIT_FAILURE);
    }
    struct sockaddr_in serverResponseAddr;
    socklen_t serverResponseAddrLen = sizeof(serverResponseAddr);
    char responseBuffer[1024];
    ssize_t bytesReceived = recvfrom(clientSocket, responseBuffer, sizeof(responseBuffer), 0, (struct sockaddr *)&serverResponseAddr, &serverResponseAddrLen);
    if (bytesReceived == -1) {
        perror("Recvfrom failed");
        close(clientSocket);
        exit(EXIT_FAILURE);
    }
    responseBuffer[bytesReceived] = '\0';
    printf("Received message from server: %s\n", responseBuffer);
    close(clientSocket);
}