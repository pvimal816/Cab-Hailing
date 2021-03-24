#! /bin/sh
# This test case checks the sign-in and sign-out 
# functionality of cab

# reset RideService and Wallet.
# every test case should begin with these two steps
curl -s http://localhost:8081/reset
curl -s http://localhost:8082/reset

testPassed="yes"

# cab 101 signs in
resp=$(curl -s "http://localhost:8080/signIn?cabId=101&initialPos=0")
if [ "$resp" = "true" ];
then
	echo "Cab 101 signed in"
else
	echo "Cab 101 could not sign in"
	testPassed="no"
fi

# cab 101 signs out
resp=$(curl -s "http://localhost:8080/signOut?cabId=101")
if [ "$resp" = "true" ];
then
	echo "Cab 101 signed out"
else
	echo "Cab 101 could not sign out"
	testPassed="no"
fi

echo "Test Passing Status: " $testPassed