#!/bin/sh
# Displays the commands run by given process and all children
# Usage: $0 <pid>
process_and_children()
{
    echo -n "$2"
    ps -ax -o pid,ppid,comm | sed -n "s/^\s*$1\s\+[0-9]\+\s\+\(.\+\)\$/\1/p"
    ps -ax -o pid,ppid,comm | sed -n "s/^\s*\([0-9]\+\)\s\+$1\s\+.\+\$/\1/p" | while read pid
    do
	process_and_children "$pid" "$2$2"
    done
}
if test "$#" -eq 1
then
    process_and_children $1 "--"
fi