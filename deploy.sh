#! /bin/sh

cd cab
./mvnw -DskipTests clean package
docker build -t cab_service .
docker run -d -p 127.0.0.1:8080:8080 cab_service

cd ../ride_service
./mvnw -DskipTests clean package
docker build -t ride_service .
docker run -d -p 127.0.0.1:8081:8081 ride_service

cd ../wallet
./mvnw -DskipTests clean package
docker build -t wallet_service .
docker run -d -p 127.0.0.1:8082:8082 wallet_service

# sleep 100 second before running tests
sleep 100

# Run tests
cd ../tests
for f in *.sh; do
  sh "$f" || break
done