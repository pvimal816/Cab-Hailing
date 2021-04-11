#sign in should return 1
if [ "$(curl -s "http://10.105.20.56:8080/signIn?cabId=0&loc=0")" != "1" ]; then
  echo "FAILED"
  exit 0
fi

#now, sign out should return 1
if [ "$(curl -s "http://10.105.20.56:8080/signOut?cabId=0")" != "1" ]; then
  echo "FAILED"
  exit 0
fi

echo "PASSED"
