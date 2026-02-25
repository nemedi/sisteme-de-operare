#!/bin/bash
# Afiseaza dimensiunea totala a zonelor de memorie
# alocate dinamic de procesele care ruleaza un program
# dat ca argument.
# Alternativ, se poate rula comanda:
# echo $(pidof "$1" | tr ' ' '\n' | xargs -I% sh -c "pmap % | tail -n +2 | head -n -1 | grep '\[ anon \]$' | tr -s ' ' | cut -d ' ' -f 2 | cut -d K -f 1" | tr '\n' '+')0 | bc

if test "$#" -eq 1
then
	total=0
	for pid in $(pidof "$1")
	do
		total=$(pmap "$pid" | head -n -1 | tail -n +2 | tr -s ' ' | (while read line
		do
			if test "$(echo "$line" | cut -d ' ' -f 4-)" = "[ anon ]"
			then
				size=$(echo "$line" | cut -d ' ' -f 2 | cut -d K -f 1)
				total=$(expr "$total" + "$size")
			fi
		done
		echo "$total"))
	done
	echo "Total amount of dynamic memory allocated to processes running '$1' is: ${total}K"
else
    echo "Usage: $0 <program>"
    exit 1
fi
