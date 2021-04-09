cd ride_service_db
docker build -t ride_db .
docker run --network=host -p 9092:9092 -d  -name ride_db ride_db