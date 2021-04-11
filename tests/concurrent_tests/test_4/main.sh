curl -s "http://10.109.206.190:8081/reset"
curl -s "http://10.99.78.76:8082/reset"

# total distance travelled:
# 1. InitialPos to sourceDes of any one ride = 10
# sourceDes to final des of one ride = 90
# Destination of first ride to source of second ride = 90
# second ride source to destination = 90
# final pos would be 100
signInStatus=$(curl -s "http://10.97.17.224:8080/signIn?cabId=101&initialPos=0")

if [ "$signInStatus" = "true" ];
then
    echo "[main.sh] cab 101 signed in"
else
    echo "[main.sh] cab 101 could not signed in"
    exit
fi

sh sh1.sh & sh sh1.sh & sh sh1.sh & sh sh1.sh & sh sh1.sh & sh sh1.sh

wait
cabStatus=$(curl -s "http://10.109.206.190:8081/getCabStatus?cabId=101")

echo "[main.sh] $cabStatus"

numRides=$(curl -s "http://10.97.17.224:8080/numRides?cabId=101")

if [ "$numRides" = "18" ];
then
    echo "[main.sh] Correct number of rides $numRides"
else
    echo "[main.sh] Wrong number of rides $numRides"
fi
