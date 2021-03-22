package com.pods.ridecompany.cab;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Controller
public class RideController {
    public final ActiveRideRepository activeRideRepository;
    public final ActiveCabsRepository activeCabsRepository;
    private static final String RIDE_SERVICE_URL = "http://localhost:8081";
    private static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    ConsumeRestAPI consumeRestAPI;

    @Autowired
    public RideController(ActiveRideRepository activeRideRepository, ActiveCabsRepository activeCabsRepository){
        this.activeRideRepository = activeRideRepository;
        this.activeCabsRepository = activeCabsRepository;
    }

    @RequestMapping(value = "/requestRide", method = RequestMethod.GET)
    @ResponseBody
    public Integer requestRide(@RequestParam Integer cabId, @RequestParam Integer rideId,
                               @RequestParam Integer sourceLoc, @RequestParam Integer destinationLoc){
        ActiveCab cab;
        try {
            cab = activeCabsRepository.findActiveCabsByCabId(cabId).get(0);
        }catch (IndexOutOfBoundsException e){
            //No cab present with this cabId
            return -1;
        }

        if(activeRideRepository.existsActiveRideByCabId(cabId)){
            //the cab is busy
            return -1;
        }

        if(cab.isInterested==0){
            //cab is not interested in taking this ride.
            cab.isInterested = 1;
            activeCabsRepository.save(cab);
            return -1;
        }

        ActiveRide ride = new ActiveRide(cabId, rideId, sourceLoc, destinationLoc, ActiveRide.CAB_STATE_COMMITTED);
        activeRideRepository.save(ride);
        cab.isInterested = 0;
        activeCabsRepository.save(cab);
        return 1;
    }

    @RequestMapping(value="/rideStarted", method = RequestMethod.GET)
    @ResponseBody
    public Integer rideStarted(@RequestParam Integer cabId, @RequestParam Integer rideId){
        try {
            ActiveRide ride = activeRideRepository.findActiveRidesByCabIdAndRideId(cabId, rideId).get(0);
            if(!ride.cabState.equals(ActiveRide.CAB_STATE_COMMITTED)){
                //ride already started
                return -1;
            }
            ride.cabState = ActiveRide.CAB_STATE_GIVING_RIDE;
            activeRideRepository.save(ride);
        }catch (IndexOutOfBoundsException e){
            //no request received for this (cabId, rideId) pair.
            //probably /requestRide is bypassed
            return -1;
        }
        return 1;
    }

    @RequestMapping(value="/rideCanceled", method = RequestMethod.GET)
    @ResponseBody
    public Integer rideCanceled(@RequestParam Integer cabId, @RequestParam Integer rideId){
        try {
            ActiveRide ride = activeRideRepository.findActiveRidesByCabIdAndRideId(cabId, rideId).get(0);
            if(!ride.cabState.equals(ActiveRide.CAB_STATE_COMMITTED)){
                //ride already started. Can not cancel now.
                return -1;
            }
            activeRideRepository.removeActiveRidesByCabIdAndRideId(cabId, rideId);
        }catch (IndexOutOfBoundsException e){
            //no request received for this (cabId, rideId) pair.
            //probably /requestRide is bypassed
            return -1;
        }
        return 1;
    }

    @RequestMapping(value="/rideEnded", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public Integer rideEnded(@RequestParam Integer cabId, @RequestParam Integer rideId){
        try {
            ActiveRide ride = activeRideRepository.findActiveRidesByCabIdAndRideId(cabId, rideId).get(0);
            if(!ride.cabState.equals(ActiveRide.CAB_STATE_GIVING_RIDE)){
                //ride not yet started.
                return -1;
            }

            //notify /rideService/rideEnded endpoint
            Integer result = restTemplate.getForObject(RIDE_SERVICE_URL+"/rideEnded?rideId="+rideId, Integer.class);
            if(result!=1)
                return -1;

            //update the location in active_cab table
            try {
                ActiveCab cab = activeCabsRepository.findActiveCabsByCabId(ride.cabId).get(0);
                cab.lastStableLocation = ride.dstLoc;
                cab.rideCnt += 1;
                activeCabsRepository.save(cab);
            }catch (IndexOutOfBoundsException e) {
                System.err.println("Found a record in active_ride table with no " +
                        "corresponding cabId in active_cab table. CabId: " + ride.cabId);
                return -1;
            }
            //now remove this ride entry from active_ride table
            activeRideRepository.removeActiveRidesByCabIdAndRideId(cabId, rideId);
        }catch (IndexOutOfBoundsException e){
            //no request received for this (cabId, rideId) pair.
            //probably /requestRide is bypassed
            return -1;
        }
        return 1;
    }

    @RequestMapping(value = "/numRides", method = RequestMethod.GET)
    @ResponseBody
    public Integer numRides(@RequestParam Integer cabId){
        try{
            ActiveCab cab = activeCabsRepository.findActiveCabsByCabId(cabId).get(0);
            return cab.rideCnt;
        } catch (IndexOutOfBoundsException e){
            // cabId is invalid or not signed in.
            return -1;
        }
    }
}
