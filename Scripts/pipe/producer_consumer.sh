#!/bin/bash

# Function for the producer
producer() {
    for i in {1..5}; do
        echo "Producing item $i"
        sleep 1
    done
}

# Function for the consumer
consumer() {
    while read -r item; do
        echo "Consuming item: $item"
    done
}

# Create a named pipe
pipe="/tmp/my_pipe"
mkfifo "$pipe"

# Run producer function in the background, sending output to the pipe
producer > "$pipe" &

# Run consumer function, reading from the pipe
consumer < "$pipe"

# Cleanup: remove the named pipe
rm "$pipe"
