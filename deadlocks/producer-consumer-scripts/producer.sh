#!/bin/bash
if test -p "$1"
then
    rm "$1"
fi
mkfifo "$1"
while read line
do
    echo "$line"
done > "$1"
rm "$1"