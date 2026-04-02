#!/usr/bin/bash
set -e
script="/usr/local/setup/create-users.sh"
if test -f "$script"
then
    echo "Begin invoking script: $script"
    bash "$script"
    echo "End invoking script: $script"
else
    echo "Script $script not found."
    exit 1
fi
exec /usr/sbin/sshd -D