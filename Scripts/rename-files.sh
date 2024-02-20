#!/bin/sh
folder=.
start=1
digits=3
while test "$#" -gt 0
do
    case "$1" in
    --folder=*|-f=*)
	folder=$(echo "$1" | cut -d = -f 2)
	;;
    --extension=*|-e=*)
	extension=$(echo "$1" | cut -d = -f 2)
	;;
    --name=*|-n=*)
	name=$(echo "$1" | cut -d = -f 2)
	;;
    --start=*|-s=*)
	start=$(echo "$1" | cut -d = -f 2)
	;;
    --digits=*|-d=*)
	digits=$(echo "$1" | cut -d = -f 2)
	;;
    esac
    shift
done
if test ! -z "$folder" \
    -a ! -z "$extension" \
    -a ! -z "$name" \
    -a ! -z "$start" \
    -a ! -z "$digits"
then
    if test -d "$folder"
    then
	count=0
	ls -A "$folder" | (while read entry
	do
	    if test -f "$folder/$entry"
	    then
		case "$entry" in
		*.$extension)
		    index="$start"
		    while test "${#index}" -lt "$digits"
		    do
			index="0$index"
		    done
		    mv "$folder/$entry" "$folder/$name$index.$extension"
		    start=$(expr "$start" + 1)
		    count=$(expr "$count" + 1)
		    ;;
		esac
	    fi
	done
	echo "$count files were renamed.")
    else
	echo "$folder not found."
	exit 1
    fi
else
    echo "Usage: $0 <options>"
    echo "Options:"
    echo "   --folder|-f=<folder> (default .)"
    echo "   --name|-n=<name>"
    echo "   --extension|-e=<extension>"
    echo "   --start|-s=<start> (default 1)"
    echo "   --digits|-d=<digits> (default 3)"
    exit 2
fi


