#!/bin/sh
if test "$#" -eq 1 -a -d "$1"
then
    total=0
    find "$1" | (while read entry
    do
	if test -f "$entry" -a -x "$entry"
	then
	    total=$(expr "$total" + 1)
	fi
    done
    echo "$total")
fi