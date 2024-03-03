#!/bin/bash
if test "$#" -eq  1
then
    if ./create_semaphore "$1" 1 > /dev/null
    then
        echo -n "Trying to acquire semaphore $1..."
        if ./wait_semaphore "$1" > /dev/null
        then
            echo "done."
            echo "Type any command and I'll do it, or 'quit' to stop me from work."
            while read command
            do
                if test "$command" = "quit"
                then
                    break
                else
                    echo -n "Doing $command..."
                    sleep 3
                    echo "done."
                fi
            done
            echo -n "Trying to release semaphore $1..."
            if ./post_semaphore "$1" > /dev/null
            then
                echo "done."
            else
                echo "failed."
                exit 4
            fi
            echo -n "Trying to remove semaphore $1..."
            if ./unlink_semaphore "$1" > /dev/null
            then
                echo "done."
                exit 0
            else
                echo "failed."
                exit 5
            fi
        else
            echo "failed."
            exit 3
        fi
    else
        echo "Smaphore $1 could not be created."
        exit 2
    fi
else
    echo "Usage: $0 <name>"
    exit 1
fi