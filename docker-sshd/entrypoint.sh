#!/usr/bin/bash
/usr/local/setup/create-users.sh &
exec /usr/sbin/sshd -D