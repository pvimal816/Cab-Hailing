#! /bin/sh
# this test case checks whether a customer's request
# gets rejected if only one cab has signed in but it is busy.

# Color
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo "${GREEN}==== Test pb_test_01 ====${NC}"

# reset RideService and Wallet.
# every test case should begin with these two steps
curl -s http://10.109.206.190:8081/reset
curl -s http://10.99.78.76:8082/reset

echo "Reset done."

testPassed="yes"

#cab 101 signs in
resp=$(curl -s "http://10.97.17.224:8080/signIn?cabId=101&initialPos=0")
if [ "$resp" = "true" ];
then
	echo "Cab 101 signed in"
else
	echo "Cab 101 could not sign in"
	testPassed="no"
fi

#customer 201 requests a ride
rideId=$(curl -s "http://10.109.206.190:8081/requestRide?custId=201&sourceLoc=2&destinationLoc=10")
if [ "$rideId" != "-1" ];
then
	echo "Ride by customer 201 started"
else
	echo "Ride to customer 201 denied"
	testPassed="no"
fi

#customer 202 requests a ride
rideId=$(curl -s "http://10.109.206.190:8081/requestRide?custId=202&sourceLoc=1&destinationLoc=11")
if [ "$rideId" != "-1" ];
then
	echo "Ride by customer 202 started"
	testPassed="no"
else
	echo "Ride to customer 202 denied"
fi

if [ "$testPassed" = "yes" ];
then
	echo "${YELLOW}Test Passing Status: ${GREEN}$testPassed${NC}"
else
	echo "${YELLOW}Test Passing Status: ${RED}$testPassed${NC}"
fi
