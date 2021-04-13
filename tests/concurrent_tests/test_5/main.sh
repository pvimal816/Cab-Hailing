#! /bin/sh
# One cab, 3 customers. All the rides should be finished by this cab and the number of rides should reflect this.

# Color
RED='\033[0;31m';
GREEN='\033[0;32m';
YELLOW='\033[0;33m';
NC='\033[0m'; # No Color

echo "${GREEN}==== Test pb_test_01 ====${NC}";

# reset RideService and Wallet.
# every test case should begin with these two steps
curl -s http://10.108.209.222:8081/reset;
curl -s http://10.106.181.133:8082/reset;

#cab 101 signs in
resp=$(curl -s "http://10.97.69.1:8080/signIn?cabId=101&initialPos=0")
if [ "$resp" = "true" ];
then
	echo "Cab 101 signed in"
else
	echo "${RED}Cab 101 could not sign in${NC}"
	testPassed="no"
    exit 0;
fi

sh cust_201.sh & sh cust_202.sh & sh cust_203.sh & sh cust_201.sh & sh cust_202.sh

wait

numRides=$(curl -s "http://10.97.69.1:8080/numRides?cabId=101")

echo "Number of rides:" $numRides

if [ "$numRides" = "5" ];
then
    echo "Correct number of rides"
else
    echo "Wrong number of rides"
fi


