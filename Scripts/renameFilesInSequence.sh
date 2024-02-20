#!/bin/sh
# Redenumeste in secventa fisierele cu o anumita extensie
while test $# -gt 0
do
	case "$1" in
		"-d")
			shift
			d="$1"
			shift
			;;
		"-n")
			shift
			n="$1"
			shift
			;;
		"-e")
			shift
			e="$1"
			shift
			;;
		"-s")
			shift
			s="$1"
			shift
			;;
		"-l")
			shift
			l="$1"
			shift
			;;
		"*")
			shift
			;;
	esac
done
if test \( ! -z "$d" \) \
	-a \( ! -z "$n" \) \
	-a \( ! -z "$e" \)
then
	if test -d "$d"
	then
		if test -z "$s"
		then
			s=1
		fi
		if test -z "$l"
		then
			l=4
		fi
		i="$s"
		ls -A "$d" "*.$e" | while read f
		do
			if test -f "$f"
			then
				t=""
				j=${#i}
				while test ${#t} -lt "$l"
				do
					t="0$t"
				done
				mv "$d/$f" "$d/$t$n.$e"
				i=`expr "$i" + 1`
			fi
		done
		i=`expr "$i" - "$s"`
		echo "There were $i files renamed as $n<counter of $l digits started from $s>.$e"
	else
		echo "Directory $d not found"
	fi
else
	echo "Usage: $0 -n <name> -e <extension> [-s <start>] [-l <length>]"
fi
