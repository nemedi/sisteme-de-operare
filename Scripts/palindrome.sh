test $# -eq 1 \
&& s=$1 \
&& echo -n ${s:$(expr ${#s} - 1):1} \
&& $0 ${s:0:$(expr ${#s} - 1)} \
|| echo