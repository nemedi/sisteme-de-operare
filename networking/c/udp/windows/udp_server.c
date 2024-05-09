#include <stdio.h>
#include <winsock2.h>

#pragma comment(lib, "ws2_32.lib")

int main() {
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        fprintf(stderr, "WSAStartup failed.\n");
        return 1;
    }
    SOCKET serverSocket;
    serverSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (serverSocket == INVALID_SOCKET) {
        fprintf(stderr, "Socket creation failed.\n");
        WSACleanup();
        return 1;
    }
    struct sockaddr_in serverAddr;
    int port = 6969;
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);
    if (bind(serverSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        fprintf(stderr, "Bind failed.\n");
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }
    while (1) {
        char buffer[1024];
        struct sockaddr_in clientAddr;
        int clientAddrLen = sizeof(clientAddr);
        int bytesReceived = recvfrom(serverSocket, buffer, sizeof(buffer), 0, (struct sockaddr *)&clientAddr, &clientAddrLen);
        if (bytesReceived == SOCKET_ERROR) {
            fprintf(stderr, "Receive failed.\n");
            return 1;
        }
        buffer[bytesReceived] = '\0';
        printf("Received message: %s\n", buffer);
        const char *responseBuffer = "Hello from server!";
        sendto(serverSocket, responseBuffer, strlen(responseBuffer), 0, (struct sockaddr *)&clientAddr, clientAddrLen);
    }
    closesocket(serverSocket);
    WSACleanup();
    return 0;
}
