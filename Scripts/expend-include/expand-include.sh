#!/bin/bash
declare -A included_files
process_file()
{
    local file="$1"
    file="$(realpath "$file" 2>/dev/null)"
    if [ ! -f "$file" ]
    then
        echo "// Error: $file does not exist" >&2
        return
    fi
    if [[ ${included_files["$file"]} ]]
    then
        return
    fi
    included_files["$file"]=1
    local dir
    dir="$(dirname "$file")"
    while IFS= read -r line; do
        line="${line%$'\r'}"
        if [[ $line =~ ^[[:space:]]*#include[[:space:]]+\"([^\"]+)\" ]]
        then
            local header="${BASH_REMATCH[1]}"
            process_file "$dir/$header"
        else
            echo "$line"
        fi
    done < "$file"
}
if [ $# -ne 1 ]
then
    echo "Usage: $0 <source-file.c>"
    exit 1
fi
process_file "$1"
