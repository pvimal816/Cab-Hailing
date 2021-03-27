package com.pods.ridecompany.cab;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;

public interface ActiveRideRepository extends Repository<ActiveRide, Long>{
    void save(ActiveRide activeRide);
    List<ActiveRide> findActiveRidesByCabIdAndRideId(Long cabId, Long rideId);
    void removeActiveRidesByCabIdAndRideId(Long cabId, Long rideId);
    boolean existsActiveRideByCabId(Long cabId);
    boolean existsActiveRideByCabIdAndCabState(Long cabId, String cabState);
}