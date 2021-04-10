package com.pods.ridecompany.rideservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.lang.Math;

@Controller
@Transactional
public class RideController {
    private final ActiveRideRepository activeRideRepo;
    private final ActiveCabsRepository activeCabsRepo;
    private final CabRepository cabRepo;
    private static String CAB_SERVICE_URL;
    private static String WALLET_SERVICE_URL;
    private static RestTemplate restTemplate = new RestTemplate();


    @Autowired
    public RideController(ActiveRideRepository activeRideRepo, ActiveCabsRepository activeCabsRepo, CabRepository cabRepo) {
        CAB_SERVICE_URL = "http://cab-service-cluster:8080";
        WALLET_SERVICE_URL = "http://wallet-service-cluster:8082";

        this.activeRideRepo = activeRideRepo;
        this.activeCabsRepo = activeCabsRepo;
        this.cabRepo = cabRepo;
    }

    @GetMapping(value = "requestRide")
    @ResponseBody
    @Transactional
    public Long requestRide(@RequestParam Long custId, @RequestParam Long sourceLoc, @RequestParam Long destinationLoc){
        List<ActiveCab> nearestCabs = activeCabsRepo.findNearestThreeCabs(sourceLoc);
        for(ActiveCab cab: nearestCabs){
            if(!cab.isInterested){
                // cab will be interested in next request
                cab.isInterested = true;
                activeCabsRepo.save(cab);
                // notify cab service about this request
                restTemplate.getForObject(CAB_SERVICE_URL+"requestRide?cabId="+cab.cabId
                                    +"&rideId=0&sourceLoc="+sourceLoc+"&destinationLoc="+destinationLoc, Boolean.class);
                continue;
            }
            //generate a rideId
            ActiveRide ride = new ActiveRide(cab.cabId, sourceLoc, destinationLoc, ActiveRide.CAB_STATE_COMMITTED, custId);
            ride = activeRideRepo.save(ride);
            // forward requestRide to cab
            boolean cabResponse = restTemplate.getForObject(CAB_SERVICE_URL+"requestRide?cabId="+cab.cabId
                                    +"&rideId="+ride.rideId+"&sourceLoc="+sourceLoc+"&destinationLoc="+destinationLoc, Boolean.class);                                
            // now ride will not be interested in next ride request
            cab.isInterested = false;
            cab.isAvailable = false;
            activeCabsRepo.save(cab);

            if(!cabResponse){
                // cab has denied a ride request
                // so delete this entry from rideService database also
                activeRideRepo.removeActiveRidesByRideId(ride.rideId);
                cab.isAvailable = true;
                cab.isInterested = true;
                activeCabsRepo.save(cab);
                continue;
            }
            
            // cab has accepted a ride request and now it is in committed state
            Long fare = (Math.abs(cab.lastStableLocation - sourceLoc) + Math.abs(sourceLoc - destinationLoc)) * 10;
            boolean paymentSuccess = restTemplate.getForObject( WALLET_SERVICE_URL+"/deductAmount?custId="+custId+"&amount="+fare, Boolean.class);
            if(paymentSuccess){
                // payment successfull, so inform a cab to start a ride
                restTemplate.getForObject(CAB_SERVICE_URL+"/rideStarted?cabId="+cab.cabId+"&rideId="+ride.rideId, Boolean.class);
                ride.cabState = ActiveRide.CAB_STATE_GIVING_RIDE;
                activeRideRepo.save(ride);
                return ride.rideId;
            }else{
                //insufficient balance so cancel this ride
                //Now, cab is available so reflect it into db
                cab.isAvailable = true;
                activeCabsRepo.save(cab);
                //Notify cab service for cancelation
                restTemplate.getForObject(CAB_SERVICE_URL+"/rideCanceled?cabId="+cab.cabId+"&rideId="+ride.rideId, Boolean.class);
                //remove this ride entry from db
                activeRideRepo.removeActiveRidesByRideId(ride.rideId);
                return -1L;
            }
        }
        // No cab found to satisfy request
        return -1L;
    }

    @GetMapping(value="/rideEnded")
    @ResponseBody
    @Transactional
    public boolean rideEnded(@RequestParam Long rideId){
        try{
            ActiveRide ride = activeRideRepo.findRidesByRideId(rideId).get(0);
            if(!ride.cabState.equals(ActiveRide.CAB_STATE_GIVING_RIDE)){
                //ride is still in COMMITTED state
                return false;
            }
            ActiveCab cab = activeCabsRepo.findActiveCabsByCabId(ride.cabId).get(0);
            cab.lastStableLocation = ride.dstLoc;
            cab.rideCnt += 1;
            cab.isAvailable = true;
            activeCabsRepo.save(cab);
            activeRideRepo.removeActiveRidesByRideId(rideId);
            return true;
        }catch(IndexOutOfBoundsException e){
            //No such rideId is associated in any ongoing ride
            return false;
        }
    }

    @GetMapping(value="/cabSignsIn")
    @ResponseBody
    @Transactional
    public boolean cabSignsIn(@RequestParam Long cabId, @RequestParam Long initialPos){
        boolean cabIdState = cabRepo.existsByCabId(cabId);
        boolean isSignedIn = activeCabsRepo.existsByCabId(cabId);
        if(cabIdState && !isSignedIn){
            ActiveCab cab = new ActiveCab(cabId,initialPos);
            activeCabsRepo.save(cab);
            return true;
        }
        return false;
    }

    @GetMapping(value="/cabSignsOut")
    @ResponseBody
    @Transactional
    public boolean cabSignsOut(@RequestParam Long cabId){
        boolean inSignedIn = activeCabsRepo.existsByCabId(cabId);
        boolean isAvailable = !activeRideRepo.existsByCabId(cabId);

        if(inSignedIn && isAvailable){
            activeCabsRepo.removeActiveCabByCabId(cabId);
            return true;
        }
        return false;
    }

    @GetMapping(value="/getCabStatus")
    @ResponseBody
    @Transactional
    public String getCabStatus(@RequestParam Long cabId){

        boolean isValidCabId = cabRepo.existsByCabId(cabId);

        if(!isValidCabId){
            return "InvalidCabId";
        }

        List<ActiveCab> cabs = activeCabsRepo.findActiveCabsByCabId(cabId);
        boolean isSignedOut = cabs.isEmpty();
        if(isSignedOut){
            return "signed-out " + "-1";
        }

        if(cabs.get(0).isAvailable){
            return "available " + cabs.get(0).lastStableLocation;
        }

        ActiveRide ride = activeRideRepo.findActiveRideByCabId(cabId).get(0);
        if(ride.cabState.equals(ActiveRide.CAB_STATE_GIVING_RIDE)){
            return "giving-ride " + ride.getSrcLoc() + " " + ride.getCustId() + " " + ride.getDstLoc();
        }

        return "committed " + cabs.get(0).lastStableLocation + " " + ride.getCustId() + " " + ride.getDstLoc();
    }

    @GetMapping(value="/reset")
    @ResponseBody
    @Transactional
    void reset(){
        List<ActiveRide> rides = activeRideRepo.findAll();
        for(ActiveRide ride : rides){
            if(ride.cabState.equals(ActiveRide.CAB_STATE_GIVING_RIDE)){
                restTemplate.getForObject(
                        CAB_SERVICE_URL+ "/rideEnded?cabId=" + ride.cabId + "&rideId=" + ride.rideId,
                        Boolean.class);
                ActiveCab cab = activeCabsRepo.findActiveCabsByCabId(ride.cabId).get(0);
                cab.isAvailable = true;
                cab.lastStableLocation = ride.dstLoc;
                activeCabsRepo.save(cab);
                activeRideRepo.removeActiveRidesByCabIdAndRideId(ride.cabId, ride.rideId);
            }else if(ride.cabState.equals(ActiveRide.CAB_STATE_COMMITTED)){
                restTemplate.getForObject(
                        CAB_SERVICE_URL+ "/rideCanceled?cabId=" + ride.cabId + "&rideId=" + ride.rideId,
                        Boolean.class);
                ActiveCab cab = activeCabsRepo.findActiveCabsByCabId(ride.cabId).get(0);
                cab.isAvailable = true;
                activeCabsRepo.save(cab);
                activeRideRepo.removeActiveRidesByCabIdAndRideId(ride.cabId, ride.rideId);
            }
        }

        List<ActiveCab> signedInCabs = activeCabsRepo.findAll();
        for(ActiveCab cab: signedInCabs){
            restTemplate.getForObject(
                    CAB_SERVICE_URL+ "/signOut?cabId=" + cab.cabId,
                    Boolean.class);
            activeCabsRepo.removeActiveCabByCabId(cab.cabId);
        }
    }
}