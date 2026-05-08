#!/bin/bash

# Declarare array asociativ
declare -A raport

# Parcurgere argumente
for cale in "$@"; do
    # Verificare existență
    if [[ ! -e "$cale" ]]; then
        echo "Eroare: '$cale' nu există." >&2
        continue
    fi

    # Extrage numele scurt
    nume=$(basename "$cale")

    # Determinare dimensiune
    if [[ -f "$cale" ]]; then
        # Fișier → dimensiune directă în bytes
        dim=$(stat -c %s "$cale")
    elif [[ -d "$cale" ]]; then
        # Director → dimensiune totală recursivă în bytes
        dim=$(du -sb "$cale" | cut -f1)
    else
        # Alte tipuri (link etc.)
        dim=$(stat -c %s "$cale")
    fi

    # Salvare în array
    raport["$nume"]=$dim
done

# Generare raport și sortare descrescătoare după dimensiune
for nume in "${!raport[@]}"; do
    echo "$nume:${raport[$nume]}"
done | sort -t: -k2,2nr

# Explicație rapidă
# declare -A raport → creează array asociativ (cheie: nume, valoare: dimensiune)
# basename → extrage numele fără cale
# stat -c %s → dimensiunea fișierului în bytes
# du -sb → dimensiunea totală a directorului (recursiv, în bytes)
# cut -f1 → extrage doar dimensiunea din output-ul du
# sort -t: -k2,2nr → sortează descrescător numeric după dimensiune

# Rulare:
# ./raport.sh /etc/passwd /var/log /bin/bash