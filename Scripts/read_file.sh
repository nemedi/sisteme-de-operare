#!/bin/bash

# Method 1: cat $FILENAME | while read LINE
method1()
{
	echo "while_read_line"
	cat $1 | while read LINE
	do
		echo "$LINE" > /dev/null
	done
}

# Method 2: while read $FILENAME from Bottom
method2()
{
	echo "while_read_LINE_bottom"
	while read LINE
	do
		echo "$LINE" > /dev/null
	done < $1
}


# Method 3: cat $FILENAME | while LINE=`read`
method3()
{
	echo "cat_while_LINE_read"
	cat $1 | while LINE=`read`
	do
		echo "$LINE" > /dev/null
	done
}

# Method 4: while LINE=`read` from the Bottom
method4()
{
	echo "while_LINE_read_bottom"
	while LINE=`read`
	do
		echo "$LINE" > /dev/null
	done < $1
}

# Method 5: cat $FILENAME | while LINE=$(read)
method5()
{
	echo "while_LINE_read_cmdsub2"
	cat $1 | while LINE=$(read)
	do
		echo "$LINE" > /dev/null
	done
}

# Method 6: while LINE=$(line) from the Bottom
method6()
{
	echo "while_LINE_read_bottom_cmdsub2"
	while LINE=$(read)
	do
		echo "$LINE" > /dev/null
	done < $1
}

# Method 7: while read LINE Using File Descriptors
method7()
{
	echo "while_read_LINE_FD"
	exec 3<&0
	exec 0< $1
	while read LINE
	do
		echo "$LINE" > /dev/null
	done
	exec 0<&3
}

# Method 8: while LINE=’read’ Using File Descriptors
method8()
{
	echo "while_LINE_read_FD"
	exec 3<&0
	exec 0< $1
	while LINE=`read`
	do
		echo "$LINE" > /dev/null
	done
	exec 0<&3
}

# Method 9: while LINE=$(read) Using File Descriptors
method9()
{
	echo "while_LINE_read_cmdsub2_FD"
	exec 3<&0
	exec 0< $1
	while LINE=$(read)
	do
		echo "$LINE" > /dev/null
	done
	exec 0<&3
}

# Method 12: while line LINE Using File Descriptors
method10()
{
	echo "while_read_LINE_FD"
	exec 3<&0
	exec 0< $1
	while read LINE
	do
		echo "$LINE" > /dev/null
	done
	exec 0<&3
}

usage()
{
	echo "Usage:" $(basename "$0") "<inputFile>"
	exit 1
}

run()
{
	echo "Starting file processing of each method"
	i=1
	while test "$i" -le 10
	do
		echo -n "Method $i: "
		time method"$i" $1
		i=$(expr "$i" + 1)
		echo
	done
}
test "$#" -eq 1 || usage
run $1
