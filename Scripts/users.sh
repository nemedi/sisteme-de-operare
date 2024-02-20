#!/bin/sh
create_users()
{
	input="users.txt"
	output="users.csv"
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
	if test \( ! -z "$input" \) \
		-a \( ! -z "$output" \) \
		-a \( ! -z "$group" \) \
		-a \( ! -z "$home" \) \
		-a \( ! -z "$user" \) \
		-a \( ! -z "$admin" \) \
		-a \( -f "$input" \) \
		-a \( ! -z "$digits" \)
	then
		echo "Begin creating users"
		echo "NUME SI PRENUME,GRUPA,CONT,PAROLA" > "$output"
		if !(cat /etc/group | grep "^$group:" > /dev/null)
		then
	    	groupadd "$group"
		fi
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
			if test ! -z "$line"
			then
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
	    			echo -n "Creating user $userName..."
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
			fi
		done
		echo "End creating users"
	fi	
}

remove_users()
{
	home="/students"
	user="stud"
	digits=3
	begin=1
	end=0
	while test "$#" -gt 0
	do
		case "$1" in
		home=*)
			home=$(echo "$1" | cut -d = -f 2)
			;;
		user=*)
			user=$(echo "$1" | cut -d = -f 2)
			;;
		digits=*)
			digits=$(echo "$1" | cut -d = -f 2)
			;;
		begin=*)
			begin=$(echo "$1" | cut -d = -f 2)
			;;
		end=*)
			end=$(echo "$1" | cut -d = -f 2)
			;;
		esac
		shift
	done
	if test \( ! -z "$home" \) \
		-a \( ! -z "$user" \) \
		-a \( ! -z "$digits" \) \
		-a \( ! -z "$begin" \) \
		-a \( ! -z "$end" \)
	then
		echo "Begin removing users"
		index="$begin"
		while test \( "$end" -eq 0 \) -o \( "$index" -le "$end" \)
		do
			number="$index"
			while test "${#number}" -lt "$digits"
			do
				number="0$number"
			done
			userName="$user$number"
			if id -u "$userName" > /dev/null 2>&1
			then
				echo -n "Removing user $userName..."
				if deluser "$userName" > /dev/null 2>&1
				then
					echo "done"
				else
					echo "failed"
					if test "$end" -eq 0
					then
						break
					fi
				fi
			fi
			index=$(expr "$index" + 1)
		done
		if test -d "$home"
		then
			rm -rf "$home"
		fi
		echo "End removing users"
	fi
}

if (test "$#" -eq 1 -a "$1" = "action=create") \
	|| (echo "$@" | sed -n 's/\s\+/\n/gp' | grep -q "^action=create$")
then
	create_users "$@"
elif (test "$#" -eq 1 -a "$1" = "action=remove") \
	||(echo "$@" | sed -n 's/\s\+/\n/gp' | grep -q "^action=remove$")
then
	remove_users "$@"
else
	echo "Missing argument action=create|remove"
fi
