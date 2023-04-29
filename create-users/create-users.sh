#!/bin/bash
home="/home"
group="students"
user="student"
administrator="student"
digits=3
input="input.csv"
output="output.csv"
while test "$#" -gt 0
do
    case "$1" in
    -h=*|--home=*)
        home=$(echo "$1" | cut -d = -f 2)
        ;;
    -g=*|--group=*)
        group=$(echo "$1" | cut -d = -f 2)
        ;;
    -a=*|--administrator=*)
        administrator=$(echo "$1" | cut -d = -f 2)
        ;;
    -d=*|--digits=*)
        digits=$(echo "$1" | cut -d = -f 2)
        ;;
    -i=*|--input=*)
        input=$(echo "$1" | cut -d = -f 2)
        ;;
    -o=*|--output=*)
        home=$(echo "$1" | cut -d = -f 2)
        ;;
    esac
    shift
done
if test ! -z "$home" \
    -a ! -z "$group" \
    -a ! -z "$user" \
    -a ! -z "$administrator" \
    -a ! -z "$digits" \
    -a ! -z "$input" \
    -a ! -z "$output"
then
    echo "Begin creating users."
    echo "NAME,USER,PASSWORD" > "$output"
    if !(cat /etc/group | grep "^$group:" > /dev/null)
    then
        groupadd "$group"
    fi
    if test ! -d "$home/$group"
    then
        mkdir "$home/$group"
    fi
    chgrp "$group" "$home/$group"
    chown "$administrator" "$home/$group"
    start=0
    cat "$input" | while read line
    do
        if test ! -z "$line"
        then
            start=$(expr "$start" + 1)
            index="$start"
            while test "${#index}" -lt "$digits"
            do
                index="0$index"
            done
            userName="$user$index"
            if id -u "$userName" > /dev/null 2>&1
            then
                deluser "$userName" > /dev/null 2>&1
            fi
            password=$(date +%N | md5sum | cut -c 5-7)
            encryptedPassword=$(openssl passwd -1 "$password")
            echo -n "Creating user $userName..."
            if useradd -p "$encryptedPassword" \
                -g "$group" \
                -d "$home/$group/$userName" "$userName" > /dev/null 2>&1
            then
                mkdir -p "$home/$group/$userName"
                find "$home/$group/$userName" | while read entry
                do
                    chgrp "$group" "$entry"
                    chown "$userName" "$entry"
                    chmod 711 "$entry"
                done
                echo "Name: $line" > "$home/$group/$userName/.id"
                echo "User: $userName" >> "$home/$group/$userName/.id"
                echo "Password: $password" > "$home/$group/$userName/.id"
                chgrp "$group" "$home/$group/$userName/.id"
                chown "$adminstrator" "$home/$group/$userName/.id"
                chmod 600 "$home/$group/$userName/.id"
                echo "$line,$userName,$password"
                echo "done"
            else
                echo "failed"
            fi
        fi
    done
    echo "End creating users."
else
    echo "Usage: $0 <arguments>"
	echo "\t[home|h=/home]"
	echo "\t[group|g=students]"
	echo "\t[user\u=student]"
	echo "\t[administrator|a=student]"
	echo "\t[digits|d=3]"
	echo "\t[input|i=input.csv]"
	echo "\t[output|o=output.csv]"
    exit 1
fi
