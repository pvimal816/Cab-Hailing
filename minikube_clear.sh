kubectl delete deployments/ride-db
kubectl delete deployments/ride-service
kubectl delete deployments/cab-service
kubectl delete deployments/wallet-service

kubectl delete service/ride-db-cluster

kubectl delete service/cab-service-cluster
kubectl delete service/cab-service-loadbalancer

kubectl delete service/ride-service-cluster
kubectl delete service/ride-service-loadbalancer

kubectl delete service/wallet-service-cluster
kubectl delete service/wallet-service-loadbalancer