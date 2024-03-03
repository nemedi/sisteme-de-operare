#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/un.h>

#define SOCKET_PATH "/tmp/ping_pong_socket"
#define BUFFER_SIZE 1024

int main() {
    int server_socket, client_socket;
    socklen_t client_address_len;
    struct sockaddr_un server_address, client_address;
    char buffer[BUFFER_SIZE];
    if ((server_socket = socket(AF_UNIX, SOCK_STREAM, 0)) == -1) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }
    server_address.sun_family = AF_UNIX;
    strcpy(server_address.sun_path, SOCKET_PATH);
    unlink(SOCKET_PATH);
    if (bind(server_socket, (struct sockaddr *)&server_address, sizeof(server_address)) == -1) {
        perror("Socket binding failed");
        exit(EXIT_FAILURE);
    }
    if (listen(server_socket, 1) == -1) {
        perror("Socket listening failed");
        exit(EXIT_FAILURE);
    }
    printf("Server listening on socket: %s\n", SOCKET_PATH);
    client_address_len = sizeof(client_address);
    if ((client_socket = accept(server_socket, (struct sockaddr *)&client_address, &client_address_len)) == -1) {
        perror("Client connection failed");
        exit(EXIT_FAILURE);
    }
    printf("Client connected\n");
    while (1) {
        ssize_t bytes_received = recv(client_socket, buffer, sizeof(buffer), 0);
        if (bytes_received == -1) {
            perror("Error receiving message from client");
            break;
        } else if (bytes_received == 0) {
            printf("Client disconnected\n");
            break;
        }
        buffer[bytes_received] = '\0';
        if (strcmp(buffer, "ping") == 0) {
            printf("Received 'ping' from client, sending 'pong'\n");
            send(client_socket, "pong", 4, 0);
        } else {
            printf("Received unexpected message: %s\n", buffer);
        }
    }
    close(client_socket);
    close(server_socket);
    unlink(SOCKET_PATH);
    return 0;
}
