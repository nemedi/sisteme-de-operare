#!/bin/sh
if test $# -eq 1
then
    total=0
    total=$(ps -ax -o user,pid | tr -s ' ' | (while read line
    do
	user=$(echo "$line" | cut -d ' ' -f 1)
	if test "$user" = "$1"
	then
	    pid=$(echo "$line" | cut -d ' ' -f 2)
	    total=$(pmap "$pid" | tr -s ' ' | (while read entry
	    do
		case "$entry" in
		*.so)
		    size=$(echo "$entry" | cut -d ' ' -f 2 | cut -d K -f 1)
		    total=$(expr "$total" + "$size")
		    ;;
		esac
	    done
	    echo "$total"))
	fi
    done
    echo "$total"))
    echo "Memory loaded with *.so by user $1: ${total}K"
else
    echo "Usage: $0 <user>"
    exit 1
fi