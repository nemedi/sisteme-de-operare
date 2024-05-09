#include <stdio.h>
#include <stdlib.h>
#include <winsock2.h>

#pragma comment(lib, "ws2_32.lib")

int main() {
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        fprintf(stderr, "WSAStartup failed.\n");
        return 1;
    }
    SOCKET clientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (clientSocket == INVALID_SOCKET) {
        fprintf(stderr, "Socket creation failed.\n");
        WSACleanup();
        return 1;
    }
    struct sockaddr_in serverAddr;
    int port = 6969;
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    serverAddr.sin_addr.s_addr = inet_addr("server_ip_address");
    if (connect(clientSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        fprintf(stderr, "Connection failed.\n");
        closesocket(clientSocket);
        WSACleanup();
        return 1;
    }
    char buffer[1024];
    printf("Enter message to send: ");
    fgets(buffer, sizeof(buffer), stdin);
    int bytesSent = send(clientSocket, buffer, strlen(buffer), 0);
    if (bytesSent == SOCKET_ERROR) {
        fprintf(stderr, "Send failed.\n");
        closesocket(clientSocket);
        WSACleanup();
        return 1;
    }
    int bytesReceived = recv(clientSocket, buffer, sizeof(buffer), 0);
    if (bytesReceived == SOCKET_ERROR) {
        fprintf(stderr, "Receive failed.\n");
        closesocket(clientSocket);
        WSACleanup();
        return 1;
    }
    buffer[bytesReceived] = '\0';
    printf("Received message from server: %s\n", buffer);
    return 0;
}
