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
curl -s http://localhost:8081/reset;
curl -s http://localhost:8082/reset;

# Now, 6 concurrent sign in request for cab 101
sh signer_1.sh & sh signer_1.sh & sh signer_1.sh & \
sh signer_1.sh & sh signer_1.sh & sh signer_1.sh;

wait;

#Status of a cab
resp=$(curl -s "http://localhost:8081/getCabStatus?cabId=101")
echo "Status for the cab 101: $resp"
