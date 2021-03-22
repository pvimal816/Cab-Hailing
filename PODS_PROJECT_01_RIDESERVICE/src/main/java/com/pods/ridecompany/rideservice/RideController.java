package com.pods.ridecompany.rideservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@Transactional
public class RideController {
    private final ActiveRideRepository activeRideRepo;
    private final ActiveCabsRepository activeCabsRepo;
    private final CabRepository cabRepo;
    private static final String CAB_SERVICE_URL = "http://localhost:8080";
    private static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public RideController(ActiveRideRepository activeRideRepo, ActiveCabsRepository activeCabsRepo, CabRepository cabRepo) {
        this.activeRideRepo = activeRideRepo;
        this.activeCabsRepo = activeCabsRepo;
        this.cabRepo = cabRepo;
    }

    @GetMapping(value = "requestRide")
    @ResponseBody
    @Transactional
    public Integer requestRide(@RequestParam Integer custId, @RequestParam Integer sourceLoc, @RequestParam Integer destinationLoc){
        List<ActiveCab> nearestCabs = activeCabsRepo.findNearestThreeCabs(sourceLoc);
        for(ActiveCab cab: nearestCabs){
            if(!cab.isInterested){
                continue;
            }

            //TODO: deduct the fare from customer's wallet

            //TODO: if payment is successful then forward request to cab service

            cab.isInterested = false;
            cab.isAvailable = false;
            activeCabsRepo.save(cab);
            ActiveRide ride = new ActiveRide(cab.cabId, sourceLoc, destinationLoc, ActiveRide.CAB_STATE_COMMITTED, custId);
            ride = activeRideRepo.save(ride);
            return ride.rideId;
        }
        return -1;
    }

    @GetMapping(value="/rideEnded")
    @ResponseBody
    @Transactional
    public boolean rideEnded(@RequestParam Integer rideId){
        try{
            ActiveRide ride = activeRideRepo.findRidesByRideId(rideId).get(0);
            if(!ride.cabState.equals(ActiveRide.CAB_STATE_GIVING_RIDE)){
                //ride is still in COMMITTED state
                return false;
            }
            ActiveCab cab = activeCabsRepo.findActiveCabsByCabId(ride.cabId).get(0);
            cab.lastStableLocation = ride.dstLoc;
            cab.rideCnt += 1;
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
    public boolean cabSignsIn(@RequestParam Integer cabId, @RequestParam Integer initialPos){
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
    public boolean cabSignsOut(@RequestParam Integer cabId){
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
    public String getCabStatus(@RequestParam Integer cabId){

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
            return "giving-ride " + ride.getSrcLoc() + ride.getCustId() + ride.getDstLoc();
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
                        Integer.class);
                ActiveCab cab = activeCabsRepo.findActiveCabsByCabId(ride.cabId).get(0);
                cab.isAvailable = true;
                cab.lastStableLocation = ride.dstLoc;
                activeCabsRepo.save(cab);
                activeRideRepo.removeActiveRidesByCabIdAndRideId(ride.cabId, ride.rideId);
            }else if(ride.cabState.equals(ActiveRide.CAB_STATE_COMMITTED)){
                restTemplate.getForObject(
                        CAB_SERVICE_URL+ "/rideCanceled?cabId=" + ride.cabId + "&rideId=" + ride.rideId,
                        Integer.class);
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