# pods project 01

A web app for cab hailing company.

## How to deploy in Ubuntu

Just clone this repo and run deploy.sh present in project root directory.
Docker daemon must be running. Then script will create 3 containers and map them to host 
on port 8080, 8081, and 8082 and deploy cab, rideService and wallet service on those 3 containers.
Finally tests in ./tests directorty will also be executed.
