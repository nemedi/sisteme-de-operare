#!/bin/bash
# Inchide toate procesele care ruleaza un program dat ca argument
if test "$#" -eq 1
then
    if test -f "$1" -o -f "$(which "$1")"
    then
        for pid in $(pidof "$1")
        do
            kill -9 "$pid"
        done
    else
        echo "File '$1' not found."
        exit 2
    fi
else
    echo "Usage: $0 <program>"
    exit 1
fi