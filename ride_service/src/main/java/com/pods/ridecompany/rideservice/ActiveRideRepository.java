package com.pods.ridecompany.rideservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActiveRideRepository extends Repository<ActiveRide, Integer>{
//    void save(ActiveRide activeRide);
    ActiveRide save(ActiveRide activeRide);

    List<ActiveRide> findActiveRideByCabId(Integer cabId);
    List<ActiveRide> findActiveRidesByCabIdAndRideId(Integer cabId, Integer rideId);
    List<ActiveRide> findRidesByRideId(Integer rideId);
    
    //TODO: Check why following annotation is needed.
    @Transactional
    void removeActiveRideByCabId(Integer cabId);
    @Transactional
    void removeActiveRidesByRideId(Integer rideId);
    @Transactional
    void removeActiveRidesByCabIdAndRideId(Integer cabId, Integer rideId);
    
    boolean existsByCabId(Integer cabId);

    List<ActiveRide> findAll();
}