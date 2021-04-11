#cab 101 signs in
resp=$(curl -s "http://10.97.17.224:8080/signIn?cabId=101&initialPos=0")
if [ "$resp" = "true" ];
then
	echo "Cab 101 signed in"
else
	echo "Cab 101 could not sign in"
fi
