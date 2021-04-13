#! /bin/sh
# every test case should begin with these two steps

# This test case checks the isolation property of wallet by adding and deducting the same amount parallely.

curl -s http://10.108.209.222:8081/reset
curl -s http://10.106.181.133:8082/reset

echo "TEST CASE NUMBER : 3"

balanceBefore=$(curl -s "http://10.106.181.133:8082/getBalance?custId=201")

sleep 5

echo "Balance Before:" $balanceBefore

sh add.sh & sh add.sh & sh deduct.sh & sh deduct.sh

wait

balanceAfter=$(curl -s "http://10.106.181.133:8082/getBalance?custId=201")

echo "Balance After:" $balanceAfter

if [ "$balanceBefore" = "$balanceAfter" ];
then
	echo "Test passed."
else
	echo "Test failed."
fi
