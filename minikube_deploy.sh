#! /bin/sh

eval $(minikube docker-env)

cd ride_service_db
docker build -t ride_db .

cd ../cab
chmod +x mvnw
./mvnw -DskipTests package
docker build -t cab_service .
# docker run --env WALLET_SERVICE_URL=wallet-service --env RIDE_SERVICE_URL=ride-service --env WALLET_SERVICE_URL=wallet-service -d --name cab_service cab_service

cd ../ride_service
chmod +x mvnw
./mvnw -DskipTests package
docker build -t ride_service .
# docker run --env WALLET_SERVICE_URL=wallet-service --env RIDE_SERVICE_URL=ride-service --env WALLET_SERVICE_URL=wallet-service -d --name ride_service ride_service

cd ../wallet
chmod +x mvnw
./mvnw -DskipTests package
docker build -t wallet_service .
# docker run --env WALLET_SERVICE_URL=wallet-service --env RIDE_SERVICE_URL=ride-service --env WALLET_SERVICE_URL=wallet-service -d --name wallet_service wallet_service

# create deployments
minikube kubectl -- create deployment ride-db --image=ride_db;
minikube kubectl -- create deployment ride-service --image=ride_service;
minikube kubectl -- create deployment cab-service --image=cab_service;
minikube kubectl -- create deployment wallet-service --image=wallet_service;

# create services
minikube kubectl -- expose deployment ride-db --name=ride-db-cluster --type=ClusterIP --port=9092;

minikube kubectl -- expose deployment cab-service --name=cab-service-cluster --type=ClusterIP --port=8080;
minikube kubectl -- expose deployment cab-service --name=cab-service-loadbalancer --type=LoadBalancer --port=8080;

minikube kubectl -- expose deployment ride-service --name=ride-service-cluster --type=ClusterIP --port=8081;
minikube kubectl -- expose deployment ride-service --name=ride-service-loadbalancer --type=LoadBalancer --port=8081;

minikube kubectl -- expose deployment wallet-service --name=wallet-service-cluster --type=ClusterIP --port=8082;
minikube kubectl -- expose deployment wallet-service --name=wallet-service-loadbalancer --type=LoadBalancer --port=8082;

kubectl scale -n default deployment ride-service --replicas=4;

minikube tunnel;

# sleep 100 second before running tests
# sleep 50

# Run tests
# cd ../tests
# for f in *.sh; do
#   sh "$f" || break
#   echo ""
# done
