package com.pods.ridecompany.cab;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;


@Controller
public class RideController {
    public final ActiveRideRepository activeRideRepository;
    public final ActiveCabsRepository activeCabsRepository;
    public final CabCredentialsRepository cabRepo;

    private static String RIDE_SERVICE_URL;
    private static RestTemplate restTemplate = new RestTemplate();
    
    @PersistenceContext
    private EntityManager em;

    @Autowired
    ConsumeRestAPI consumeRestAPI;

    @Autowired
    public RideController(ActiveRideRepository activeRideRepository, ActiveCabsRepository activeCabsRepository, CabCredentialsRepository cabRepo){
        RIDE_SERVICE_URL = "http://ride-service-cluster:8081";
        this.activeRideRepository = activeRideRepository;
        this.activeCabsRepository = activeCabsRepository;
        this.cabRepo = cabRepo;
    }

    @RequestMapping(value = "/requestRide", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public boolean requestRide(@RequestParam Long cabId, @RequestParam Long rideId,
                               @RequestParam Long sourceLoc, @RequestParam Long destinationLoc){
        ActiveCab cab;

        if (activeRideRepository.existsActiveRideByCabId(cabId)) {
            // the cab is busy
            return false;
        }

        try {
            // cab = activeCabsRepository.findActiveCabsByCabId(cabId).get(0);
            cab = em.find(ActiveCab.class, cabId, LockModeType.PESSIMISTIC_WRITE);
            if(cab==null)
                return false;
        }catch (Throwable e){
            //No cab present with this cabId
            System.err.println("Potential error in /requestRide.");
            e.printStackTrace();
            return false;
        }
    
        if(cab.isInterested==0){
            //cab is not interested in taking this ride.
            cab.isInterested = 1;
            activeCabsRepository.save(cab);
            return false;
        }

        ActiveRide ride = new ActiveRide(cabId, rideId, sourceLoc, destinationLoc, ActiveRide.CAB_STATE_COMMITTED);
        activeRideRepository.save(ride);
        cab.isInterested = 0;
        activeCabsRepository.save(cab);
        return true;
    }

    @RequestMapping(value="/rideStarted", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public boolean rideStarted(@RequestParam Long cabId, @RequestParam Long rideId){
        try {
            ActiveRide ride = activeRideRepository.findActiveRidesByCabIdAndRideId(cabId, rideId).get(0);
            if(!ride.cabState.equals(ActiveRide.CAB_STATE_COMMITTED)){
                //ride already started
                return false;
            }
            ride.cabState = ActiveRide.CAB_STATE_GIVING_RIDE;
            activeRideRepository.save(ride);
        }catch (IndexOutOfBoundsException e){
            //no request received for this (cabId, rideId) pair.
            //probably /requestRide is bypassed
            return false;
        }
        return true;
    }

    @RequestMapping(value="/rideCanceled", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public boolean rideCanceled(@RequestParam Long cabId, @RequestParam Long rideId){
        try {
            ActiveRide ride = activeRideRepository.findActiveRidesByCabIdAndRideId(cabId, rideId).get(0);
            if(!ride.cabState.equals(ActiveRide.CAB_STATE_COMMITTED)){
                //ride already started. Can not cancel now.
                return false;
            }
            activeRideRepository.removeActiveRidesByCabIdAndRideId(cabId, rideId);
        }catch (IndexOutOfBoundsException e){
            //no request received for this (cabId, rideId) pair.
            //probably /requestRide is bypassed
            return false;
        }
        return true;
    }

    @RequestMapping(value="/rideEnded", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public boolean rideEnded(@RequestParam Long cabId, @RequestParam Long rideId){
        
        try {
            // ActiveRide ride = activeRideRepository.findActiveRidesByCabIdAndRideId(cabId, rideId).get(0);
            ActiveRide ride = em.find(ActiveRide.class, cabId, LockModeType.PESSIMISTIC_WRITE);
            if(ride == null)
                return false;
            if(!ride.cabState.equals(ActiveRide.CAB_STATE_GIVING_RIDE)){
                //ride not yet started.
                return false;
            }

            //notify /rideService/rideEnded endpoint
            boolean result = restTemplate.getForObject(RIDE_SERVICE_URL+"/rideEnded?rideId="+rideId, Boolean.class);
            if(!result)
                return false;

            //update the location in active_cab table
            try {
                ActiveCab cab = em.find(ActiveCab.class, ride.cabId, LockModeType.PESSIMISTIC_WRITE);
                // ActiveCab cab = activeCabsRepository.findActiveCabsByCabId(ride.cabId).get(0);
                cab.lastStableLocation = ride.dstLoc;
                cab.rideCnt += 1;
                activeCabsRepository.save(cab);
            }catch (Throwable e) {
                // System.err.println("Found a record in active_ride table with no " +
                //         "corresponding cabId in active_cab table. CabId: " + ride.cabId);
                System.err.println("Potential error in rideEnded: " + e.getMessage());
                return false;
            }
            //now remove this ride entry from active_ride table
            activeRideRepository.removeActiveRidesByCabIdAndRideId(cabId, rideId);
        } catch (Throwable e) {
            System.err.println("Potential error in rideEnded: " + e.getMessage());
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/numRides", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public Long numRides(@RequestParam Long cabId){
        if(!cabRepo.existsByCabId(cabId)){
            // Invalid cabId
            return -1L;
        }
        try{
            ActiveCab cab = activeCabsRepository.findActiveCabsByCabId(cabId).get(0);
            if(activeRideRepository.existsActiveRideByCabIdAndCabState(cabId, ActiveRide.CAB_STATE_GIVING_RIDE))
                return cab.rideCnt+1;
            return cab.rideCnt;
        } catch (IndexOutOfBoundsException e){
            // cab is not signed ins
            return 0L;
        }
    }
}
