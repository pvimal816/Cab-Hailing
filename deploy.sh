#! /bin/sh

cd ../cab
chmod +x mvnw
./mvnw -DskipTests package
docker build -t cab_service .
docker run --rm=true --network=host --env HOST_URL=localhost -d -name cab_service cab_service

cd ../ride_service
chmod +x mvnw
./mvnw -DskipTests package
docker build -t ride_service .
docker run --rm=true --network=host --env HOST_URL=localhost -d  -name ride_service ride_service

cd ../wallet
chmod +x mvnw
./mvnw -DskipTests package
docker build -t wallet_service .
docker run --rm=true --network=host --env HOST_URL=localhost -d -name wallet_service wallet_service

# sleep 100 second before running tests
sleep 50

# Run tests
cd ../tests
for f in *.sh; do
  sh "$f" || break
  echo ""
done
