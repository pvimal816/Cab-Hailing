#customer 203 requests a ride
rideId=$(curl -s "http://localhost:8081/requestRide?custId=203&sourceLoc=2&destinationLoc=10")
if [ "$rideId" != "-1" ];
then
	echo "Ride by customer 203 started"
else
	echo "Ride to customer 203 denied"
fi