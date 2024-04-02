#!/bin/bash
while test 1 -eq 1
do
    if read line
    then
        echo "$line"
    fi
done < "$1"