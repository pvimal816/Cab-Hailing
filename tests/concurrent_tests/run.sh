#!/bin/bash
for i in {1..5};
do
	cd "test_$i";
	echo "===================TEST $i===================="
	sh main.sh
	cd ..;
done
