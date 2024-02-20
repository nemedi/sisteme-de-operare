#!/bin/bash
output=$(ps -ax -o user)
lines=$(echo "$output" | wc -l)
lines=$(expr "$lines" - 1)
declare -A v
echo "$output" | tail -"$lines" | (while read user
do
    if test -z "${v[$user]}"
    then
	v["$user"]=1
    else
	v["$user"]=$(expr "${v[$user]}" + 1)
    fi
done
for user in "${!v[@]}"
do
    echo "$user:${v[$user]}"
done | sort -nr -t: -k2)