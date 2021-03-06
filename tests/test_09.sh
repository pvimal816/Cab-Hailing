#! /bin/sh

# This test checks whether the getBalance works correctly or not.

# Color
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo "${GREEN}==== Test test_09 ====${NC}"

testPassed="yes"

# reset RideService and Wallet.
curl -s http://10.108.209.222:8081/reset
curl -s http://10.106.181.133:8082/reset


# Step 0: cab 101 signs in at location 100.
resp=$(curl -s "http://10.97.69.1:8080/signIn?cabId=101&initialPos=100")
if [ "$resp" = "true" ];
then
	echo "Cab 101 signed in"
else
	echo "Cab 101 could not sign in"
	testPassed="no"
fi


# Step 1: Get the balance of the customer 201.

balance_old=$(curl -s "http://10.106.181.133:8082/getBalance?custId=201")
echo "The balance before taking the ride:" $balance_old

# Step 2: Take a ride from location 100 (cab's location) to 200.

rideDetails=$(curl -s "http://10.108.209.222:8081/requestRide?custId=201&sourceLoc=100&destinationLoc=200")
rideId=$(echo $rideDetails | cut -d' ' -f 1)
if [ "$rideId" != "-1" ];
then
    echo "Ride by customer 201 started"
else
    echo "Ride to customer 201 denied"
    testPassed="no"
fi

#Step 3: Check if the balance of customer 201 has been deducted appropriately.
balance_new=$(curl -s "http://10.106.181.133:8082/getBalance?custId=201")
echo "The balance after taking the ride:" $balance_new

diff=`expr $balance_old - $balance_new`
if [ "$diff" != "1000" ];
then
    echo "${RED}Incorrect balance${NC}"
    testPassed="no"
else
    echo "Correct balance"
fi

if [ "$testPassed" = "yes" ];
then
	echo "${YELLOW}Test Passing Status: ${GREEN}$testPassed${NC}"
else
	echo "${YELLOW}Test Passing Status: ${RED}$testPassed${NC}"
fi
