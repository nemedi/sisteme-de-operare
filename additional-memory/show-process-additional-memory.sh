#!/bin/bash
if test "$#" -eq 1
then
    total=0
    pmap "$1" | tr -s ' ' | (while read line
    do
    	if echo "$line" | cut -d ' ' -f 5 | grep "^anon$" > /dev/null
    	then
    	    size=$(echo "$line" | cut -d ' ' -f 2 | cut -d K -f 1)
    	    total=$(expr "$total" + "$size")
    	fi
    done
    echo "Additional memory of process $1 is: ${total}K.")
else
    echo "Usage: $0 <PID>"
    exit 1
fi
