#!/bin/bash
if test "$#" = 2
then
    if test -d "$1"
    then
	total=0
	total=$(find "$1" -user "$2" -type "f" | (while read line
	do
	    size=$(stat -c %s "$line")
	    total=$(expr "$total" + "$size")
	done
	echo "$total"))
	echo "$total"
    else
	echo "Folder $1 not found."
	exit 2
    fi
else
    echo "Usage: $0 <folder> <user>"
    exit 1
fi