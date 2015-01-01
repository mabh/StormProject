#!/bin/bash

copies="$1"

echo "Making $copies copies"

for i in $(seq 1 $copies)
do
  cp books.xml books$i.xml
#  cp cd.xml cd$i.xml
done

