#!/bin/bash
if test "$#" -eq 1 -a -d "$1"
then
    declare -A v
    ls -lRA "$1" | tr -s ' ' | (while read line
    do
	case "$line" in
	-*)
	    user=$(echo "$line" | cut -d ' ' -f 3)
	    size=$(echo "$line" | cut -d ' ' -f 5)
	    if test -z "${v[$user]}"
	    then
		v["$user"]="$size"
	    else
		v["$user"]=$(expr "${v[$user]}" + "$size")
	    fi
	    ;;
	esac
    done
    for user in "${!v[@]}"
    do
	echo "$user ${v[$user]}"
    done | sort -nr -t' ' -k2)
fi