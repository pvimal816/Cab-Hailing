test () {
  if [ "$(eval "$1")" != "$2" ]; then
    echo "FAILED";
    exit 0
  fi
}

test "curl -s \"http://localhost:8080/signIn?cabId=0&loc=0\"" "1";

#now, ride request should return 1
test "curl -s \"http://localhost:8080/requestRide?cabId=0&rideId=0&sourceLoc=0&destinationLoc=10\"" "1";

#now, should not be able to sign out
test "curl -s \"http://localhost:8080/signOut?cabId=0\"" "-1";

#now, ride start request should return 1
test "curl -s \"http://localhost:8080/rideStarted?cabId=0&rideId=0\"" "1";

#now, should not be able to cancel a ride
test "curl -s \"http://localhost:8080/rideCanceled?cabId=0&rideId=0\"" "-1";

#now, ride end request should return 1
test "curl -s \"http://localhost:8080/rideEnded?cabId=0&rideId=0\"" "1";

#now, should be able to sign out
test "curl -s \"http://localhost:8080/signOut?cabId=0\"" "1";

echo "PASSED"
