#!/bin/bash

# Implementati un script care afiseaza numarul de fisiere asupra carora utilizatorul curent are drept de executie dintr-un director dat si din subdirectorele acestuia.

if test "$#" -eq 1
then
    if test -d "$1"
    then
	declare -A v
	ls -lR "$1" | (while read line
	do
	    case "$line" in
	    -*)
		user=$(echo "$line" | tr -s ' ' | cut -d ' ' -f 3)
		if test -z "${v[$user]}"
		then
		    v["$user"]=1
		else
		    v["$user"]=$(expr "${v[$user]}" + 1)
		fi
	    esac
	done
	for user in "${!v[@]}"
	do
	    echo "$user:${v[$user]}"
	done | sort -nr -t: -k2)
    else
	exit 2
    fi
else
    exit 1
fi