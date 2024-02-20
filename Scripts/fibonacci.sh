fibonacci()
{
    local n="$1"
    if test "$n" -eq 1
    then
	echo 1
    else
	if test "$n" -eq 2
	then
    	    echo "2"
	else
	    n=$(expr "$n" - 1)
	    local first=$(fibonacci "$n")
	    n=$(expr "$n" - 1)
	    local second=$(fibonacci "$n")
	    expr "$first" + "$second"
	fi
    fi
}
result=$(fibonacci 4)
echo "$result"