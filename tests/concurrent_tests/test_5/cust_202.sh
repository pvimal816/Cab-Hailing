#Cust 202 trying to get a ride.

rideDetails="-1"

while [ "$rideDetails" = "-1" ]; 
do
	rideDetails=$(curl -s "http://10.108.209.222:8081/requestRide?custId=202&sourceLoc=0&destinationLoc=10")
done

rideId=$(echo $rideDetails | cut -d' ' -f 1)
cabId=$(echo $rideDetails | cut -d' ' -f 2)
fare=$(echo $rideDetails | cut -d' ' -f 3)

rideEnd=$(curl -s "http://10.97.69.1:8080/rideEnded?cabId=101&rideId=${rideId}")
