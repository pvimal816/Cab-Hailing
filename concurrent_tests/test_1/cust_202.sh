#customer 202 requests a ride
rideId=$(curl -s "http://localhost:8081/requestRide?custId=202&sourceLoc=2&destinationLoc=10")
if [ "$rideId" != "-1" ];
then
	echo "Ride by customer 202 started"
else
	echo "Ride to customer 202 denied"
fi