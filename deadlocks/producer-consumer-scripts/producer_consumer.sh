#!/bin/bash
producer() {
    item=1
    while test "$item" -le "$1"
    do
        echo "$item"
        item=$(expr "$item" + 1)
        sleep 1
    done
}
consumer() {
    while read -r item
    do
        echo "Consuming item: $item"
    done
}
pipe="/tmp/my_pipe"
if test -p "$pipe"
then
    rm "$pipe"
fi
mkfifo "$pipe"
producer 10 > "$pipe" &
consumer < "$pipe"
rm "$pipe"