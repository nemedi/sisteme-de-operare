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
    serverSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (serverSocket == INVALID_SOCKET) {
        fprintf(stderr, "Socket creation failed.\n");
        WSACleanup();
        return 1;
    }
    struct sockaddr_in serverAddr;
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);
    serverAddr.sin_port = htons(6969);

    if (bind(serverSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        fprintf(stderr, "Bind failed.\n");
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }
    if (listen(serverSocket, SOMAXCONN) == SOCKET_ERROR) {
        fprintf(stderr, "Listen failed.\n");
        closesocket(serverSocket);
        WSACleanup();
        return 1;
    }
    SOCKET clientSocket;
    struct sockaddr_in clientAddr;
    int clientAddrLen = sizeof(clientAddr);
    while (1) {
        clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddr, &clientAddrLen);
        if (clientSocket == INVALID_SOCKET) {
            fprintf(stderr, "Accept failed.\n");
            closesocket(serverSocket);
            WSACleanup();
            return 1;
        }
        char buffer[1024];
        int bytesReceived = recv(clientSocket, buffer, sizeof(buffer), 0);
        if (bytesReceived > 0) {
            buffer[bytesReceived] = '\0';
            printf("Received message from client: %s\n", buffer);
            send(clientSocket, buffer, bytesReceived, 0);
        }
        closesocket(clientSocket);
    }
    closesocket(serverSocket);
    WSACleanup();
    return 0;
}
