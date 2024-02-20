#!/bin/sh
hanoi()
{
    local n="$1"
    local a="$2"
    local b="$3"
    local c="$4"
    if test "$n" -eq 1
    then
	echo "$a -> $b"
    else
	n=$(expr "$n" - 1)
	hanoi "$n" "$a" "$c" "$b"
	echo "$a -> $b"
	hanoi "$n" "$c" "$b" "$a"
    fi
}
if test "$#" -eq 1
then
    hanoi "$1" "a" "b" "c"
fi