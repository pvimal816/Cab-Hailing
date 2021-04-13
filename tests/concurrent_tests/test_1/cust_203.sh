#customer 203 requests a ride
rideDetails=$(curl -s "http://10.108.209.222:8081/requestRide?custId=203&sourceLoc=2&destinationLoc=10")
rideId=$(echo $rideDetails | cut -d' ' -f 1)
if [ "$rideId" != "-1" ];
then
	echo "Ride by customer 203 started"
else
	echo "Ride to customer 203 denied"
fi