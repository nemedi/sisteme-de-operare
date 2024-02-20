#!/bin/sh
add()
{
    if test "$#" -eq 0
    then
	exit 1
    fi
    total=0
    while test "$#" -gt 0
    do
	total=$(expr "$total" + "$1")
	shift
    done
    echo "$total"
}
result=$(add "$@")
echo "$result"