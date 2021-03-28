cd cab
chmod +x mvnw
./mvnw -DskipTests package
docker build -t cab_service .
docker run --rm=true -d -p 127.0.0.1:8080:8080 --env HOST_URL=host.docker.internal cab_service

cd ../ride_service
chmod +x mvnw
./mvnw -DskipTests package
docker build -t ride_service .
docker run --rm=true -d -p 127.0.0.1:8081:8081 --env HOST_URL=host.docker.internal ride_service

cd ../wallet
chmod +x mvnw
./mvnw -DskipTests package
docker build -t wallet_service .
docker run --rm=true -d -p 127.0.0.1:8082:8082 --env HOST_URL=host.docker.internal wallet_service

# sleep 100 second before running tests
sleep 50

# Run tests
cd ../tests
for f in *.sh; do
  sh "$f" || break
  echo ""
done