Implementation of a web app for a cab hailing company.

## How to deploy in Ubuntu

* Clone this repo
* Run the deploy.sh present in project root directory(Docker daemon must be running)

Then script will create 3 containers and will map them to host port 8080, 8081, and 8082 and then it will deploy cab, rideService and wallet service on those 3 containers respectively. Finally the tests in ./tests directorty will also be executed by script itself.
