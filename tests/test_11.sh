#! /bin/sh
# This test case checks the functionality of requestRide

# This test asserts that the cab driver must be interested in every 
# alternative ride request

# Color
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo "${GREEN}==== Test test_11 ====${NC}"

testPassed="yes"

# reset RideService and Wallet.
curl -s http://10.108.209.222:8081/reset
curl -s http://10.106.181.133:8082/reset

# cab 101 signs in
resp=$(curl -s "http://10.97.69.1:8080/signIn?cabId=101&initialPos=0")
if [ "$resp" = "true" ];
then
	echo "Cab 101 signed in"
else
	echo "${RED}Cab 101 could not sign in${NC}"
	testPassed="no"
fi

# cab 101 signs in
resp=$(curl -s "http://10.97.69.1:8080/signIn?cabId=102&initialPos=0")
if [ "$resp" = "true" ];
then
	echo "Cab 102 signed in"
else
	echo "${RED}Cab 102 could not sign in${NC}"
	testPassed="no"
fi

# customer 201 sends a request for ride
rideDetails=$(curl -s "http://10.108.209.222:8081/requestRide?custId=201&sourceLoc=14&destinationLoc=10")
rideId=$(echo $rideDetails | cut -d' ' -f 1)
if [ ! "$rideId" = "-1" ];
then
	echo "Customer 201 alloted a ride number " $rideId
else
	echo "${RED}Customer 201 is not alloted a ride${NC}"
	testPassed="no"
fi

# customer 201 again sends a request for ride. 
# This request must be denied.
rideDetails=$(curl -s "http://10.108.209.222:8081/requestRide?custId=201&sourceLoc=10&destinationLoc=14")
rideId=$(echo $rideDetails | cut -d' ' -f 1)
if [ ! "$rideId" = "-1" ];
then
	echo "Customer 201 alloted a ride"
else
	echo "${RED}Customer 201 is not alloted a ride${NC}" 
	testPassed="no"
fi

if [ "$testPassed" = "yes" ];
then
	echo "${YELLOW}Test Passing Status: ${GREEN}$testPassed${NC}"
else
	echo "${YELLOW}Test Passing Status: ${RED}$testPassed${NC}"
fi
