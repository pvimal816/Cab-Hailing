#! /bin/sh
# cab 101 signs in. Then 3 customers 
# concurrently request a ride.

# Color
RED='\033[0;31m';
GREEN='\033[0;32m';
YELLOW='\033[0;33m';
NC='\033[0m'; # No Color

echo "${GREEN}==== Test pb_test_01 ====${NC}";

# reset RideService and Wallet.
# every test case should begin with these two steps
curl -s http://10.109.206.190:8081/reset;
curl -s http://10.99.78.76:8082/reset;

testPassed="yes";

#cab 101 signs in
resp=$(curl -s "http://10.97.17.224:8080/signIn?cabId=101&initialPos=0")
if [ "$resp" = "true" ];
then
	echo "Cab 101 signed in"
else
	echo "${RED}Cab 101 could not sign in${NC}"
	testPassed="no"
    exit 0;
fi

# Now, 3 customer concurrently requests a ride
sh cust_201.sh & sh cust_202.sh & sh cust_203.sh;

wait;

#Status of a cab after a ride
resp=$(curl -s "http://10.109.206.190:8081/getCabStatus?cabId=101")
echo "Status for the cab 101: $resp"
