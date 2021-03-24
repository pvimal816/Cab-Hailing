package com.pods.ridecompany.cab;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;

public interface ActiveRideRepository extends Repository<ActiveRide, Integer>{
    void save(ActiveRide activeRide);
    List<ActiveRide> findActiveRidesByCabIdAndRideId(Integer cabId, Integer rideId);
    void removeActiveRidesByCabIdAndRideId(Integer cabId, Integer rideId);
    boolean existsActiveRideByCabId(Integer cabId);
    boolean existsActiveRideByCabIdAndCabState(Integer cabId, String cabState); 
}