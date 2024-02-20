#!/bin/bash
change_me()
{
    local -n ref="$1"
    ref="$2"
}
if test "$#" -eq 2
then
    n="$1"
    echo "before calling change_me n: $n" 
    change_me n "$2"
    echo "after calling change_me n: $n"
fi