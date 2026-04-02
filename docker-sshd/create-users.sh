#!/usr/bin/bash
input="/usr/local/share/users.txt"
output="/usr/local/share/users.csv"
group="students"
home="/students"
user="stud"
admin="iulian"
digits=3
while test "$#" -gt 0
do
	case "$1" in
	input=*)
		input=$(echo "$1" | cut -d = -f 2)
		;;
	output=*)
		output=$(echo "$1" | cut -d = -f 2)
		;;
	group=*)
		group=$(echo "$1" | cut -d = -f 2)
		;;
	home=*)
		home=$(echo "$1" | cut -d = -f 2)
		;;
	user=*)
		user=$(echo "$1" | cut -d = -f 2)
		;;
	admin=*)
		admin=$(echo "$1" | cut -d = -f 2)
		;;
	digits=*)
		digits=$(echo "$1" | cut -d = -f 2)
		;;
	esac
	shift
done
if test ! -z "$input" \
	-a ! -z "$output" \
	-a ! -z "$group" \
	-a ! -z "$home" \
	-a ! -z "$user" \
	-a ! -z "$admin" \
	-a -f "$input" \
	-a ! -z "$digits"
then
	if cat /etc/group | grep "^$group:" > /dev/null
	then
		echo "Group '$group' already exists"
		exit
	else
		groupadd "$group"
	fi
	echo "Begin creating users:"
	echo "NUME SI PRENUME,GRUPA,CONT,PAROLA" > "$output"
	if test -d "$home"
	then
		rm -rf "$home"
	fi
	mkdir -p "$home"
	chgrp "$group" "$home"
	chown "$admin" "$home"
	index=0
	cat "$input" | while read line
	do
		if test -z "$line"
		then
			continue
		fi
		case "$line" in
		*:)
			class=$(echo "$line" | cut -d : -f 1)
			if test ! -d "$home/$class"
			then
				mkdir "$home/$class"
				chgrp "$group" "$home/$class"
				chown "$admin" "$home/$class"
			fi
			;;
		*)
			index=$(expr "$index" + 1)
			number="$index"
			while test "${#number}" -lt "$digits"
			do
				number="0$number"
			done
			userName="$user$number"
			if id -u "$userName" > /dev/null 2>&1
			then
				deluser "$userName" > /dev/null 2>&1
			fi
			password=$(date +%N | md5sum | cut -c 5-7)
			encryptedPassword=$(openssl passwd -1 "$password")
			echo -n "Creating user '$userName'..."
			if useradd -p "$encryptedPassword" -g "$group" -d "$home/$class/$userName" "$userName" > /dev/null 2>&1
			then
				mkdir -p "$home/$class/$userName"
				find "$home/$class/$userName" | while read entry
				do
					chgrp "$group" "$entry"
					chown "$userName" "$entry"
					chmod 711 "$entry"
				done
				echo "Name: $line" > "$home/$class/$userName/.id"
				echo "Class: $class" >> "$home/$class/$userName/.id"
				echo "User: $userName" >> "$home/$class/$userName/.id"
				echo "Password: $password" >> "$home/$class/$userName/.id"
				chgrp "$group" "$home/$class/$userName/.id"
				chown "$admin" "$home/$class/$userName/.id"
				chmod 600 "$home/$class/$userName/.id"
				echo "$line,$class,$userName,$password" >> "$output"
				echo "done"
			else
				echo "failed"
			fi
			;;
		esac
	done
	echo "End creating users."
else
	echo "Missing arguments:"
	echo "\tinput: '$input'"
	echo "\toutput: '$output'"
	echo "\tgroup: '$group'"
	echo "\thome: '$home'"
	echo "\tuser: '$user'"
	echo "\tadmin: '$admin'"
	echo "\tdigits: '$digits'"
	exit 1
fi	
