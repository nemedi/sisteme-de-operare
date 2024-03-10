#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/un.h>

#define SOCKET_PATH "/tmp/ping_pong_socket"
#define BUFFER_SIZE 1024

int main() {
    int client_socket;
    struct sockaddr_un server_address;
    char buffer[BUFFER_SIZE];
    if ((client_socket = socket(AF_UNIX, SOCK_STREAM, 0)) == -1) {
        perror("Socket creation failed");
        exit(EXIT_FAILURE);
    }
    server_address.sun_family = AF_UNIX;
    strcpy(server_address.sun_path, SOCKET_PATH);
    if (connect(client_socket, (struct sockaddr *)&server_address, sizeof(server_address)) == -1) {
        perror("Connection to server failed");
        exit(EXIT_FAILURE);
    }
    printf("Connected to server on socket: %s\n", SOCKET_PATH);
    send(client_socket, "ping", 4, 0);
    printf("Sent 'ping' to server, waiting for 'pong'\n");
    ssize_t bytes_received = recv(client_socket, buffer, sizeof(buffer), 0);
    if (bytes_received == -1) {
        perror("Error receiving message from server");
    } else if (bytes_received == 0) {
        printf("Server disconnected\n");
    } else {
        buffer[bytes_received] = '\0';
        printf("Received from server: %s\n", buffer);
    }
    close(client_socket);
    return 0;
}
