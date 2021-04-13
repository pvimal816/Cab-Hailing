cabLoc=$(curl -s "http://10.108.209.222:8081/getCabStatus?cabId=101")

# echo "cabLoc: $cabLoc"

cabLoc=$(echo "$cabLoc" | awk '{print $2}')


rideReqStatus="-1"

while [ "$rideReqStatus" = "-1" ];
do
   rideReqStatus=$(curl -s \
                   "http://10.108.209.222:8081/requestRide?custId=201&sourceLoc=${cabLoc}&destinationLoc=10")
done

# echo "[sh1.sh] ride status ${rideReqStatus}"

rideId=$(echo $rideReqStatus | awk '{print $1;}')

rideEndResp=$(curl -s "http://10.97.69.1:8080/rideEnded?cabId=101&rideId=${rideId}")

# echo "[sh1.sh] $rideId ended with response $rideEndResp"

# _____________________________________________________________________________

cabLoc=$(curl -s "http://10.108.209.222:8081/getCabStatus?cabId=101")
# echo "cabLoc: $cabLoc"
cabLoc=$(echo "$cabLoc" | awk '{print $2}')


rideReqStatus="-1"

while [ "$rideReqStatus" = "-1" ];
do
    rideReqStatus=$(curl -s \
                         "http://10.108.209.222:8081/requestRide?custId=201&sourceLoc=${cabLoc}&destinationLoc=10")
done

# echo "[sh1.sh] ride status ${rideReqStatus}"

rideId=$(echo $rideReqStatus | awk '{print $1;}')

rideEndResp=$(curl -s "http://10.97.69.1:8080/rideEnded?cabId=101&rideId=${rideId}")

# echo "[sh1.sh] $rideId ended with response $rideEndResp"

#______________________________________________________________________________


cabLoc=$(curl -s "http://10.108.209.222:8081/getCabStatus?cabId=101")
# echo "cabLoc: $cabLoc"
cabLoc=$(echo "$cabLoc" | awk '{print $2}')


rideReqStatus="-1"

while [ "$rideReqStatus" = "-1" ];
do
    rideReqStatus=$(curl -s \
                    "http://10.108.209.222:8081/requestRide?custId=201&sourceLoc=${cabLoc}&destinationLoc=10")
done

# echo "[sh1.sh] ride status ${rideReqStatus}"

rideId=$(echo $rideReqStatus | awk '{print $1;}')

rideEndResp=$(curl -s "http://10.97.69.1:8080/rideEnded?cabId=101&rideId=${rideId}")

# echo "[sh1.sh] $rideId ended with response $rideEndResp"

# ______________________________________________________________________________
