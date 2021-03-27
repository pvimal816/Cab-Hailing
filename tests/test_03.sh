#! /bin/sh
# This test case checks the functionality of requestRide

# first 4 cabs will sign in with location 0, 10, 20, 30.
# Then customer makes a request for a ride with a source location 14 
# which should be satisfied by cab at location 10.
# then second customer makes a request for a ride with a source location 26.
# then third customer makes a request for a ride with a source location 14
# which should be satisfied by 

# Color
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

echo "${GREEN}==== Test test_03 ====${NC}"

# reset RideService and Wallet.
# every test case should begin with these two steps
curl -s http://localhost:8081/reset
curl -s http://localhost:8082/reset

testPassed="yes"

if [ "$testPassed" = "yes" ];
then
	echo "${YELLOW}Test Passing Status: ${GREEN}$testPassed${NC}"
else
	echo "${YELLOW}Test Passing Status: ${RED}$testPassed${NC}"
fi
