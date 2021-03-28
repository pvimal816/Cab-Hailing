package com.pods.ridecompany.rideservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActiveRideRepository extends Repository<ActiveRide, Long>{
//    void save(ActiveRide activeRide);
    ActiveRide save(ActiveRide activeRide);

    List<ActiveRide> findActiveRideByCabId(Long cabId);
    List<ActiveRide> findActiveRidesByCabIdAndRideId(Long cabId, Long rideId);
    List<ActiveRide> findRidesByRideId(Long rideId);
    boolean existsByCustId(Long custId);
    //TODO: Check why following annotation is needed.
    @Transactional
    void removeActiveRideByCabId(Long cabId);
    @Transactional
    void removeActiveRidesByRideId(Long rideId);
    @Transactional
    void removeActiveRidesByCabIdAndRideId(Long cabId, Long rideId);
    
    boolean existsByCabId(Long cabId);

    List<ActiveRide> findAll();
}