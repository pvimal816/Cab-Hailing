package com.pods.ridecompany.cab;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.LockModeType;

public interface ActiveRideRepository extends Repository<ActiveRide, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void save(ActiveRide activeRide);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ActiveRide> findActiveRidesByCabIdAndRideId(Long cabId, Long rideId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void removeActiveRidesByCabIdAndRideId(Long cabId, Long rideId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsActiveRideByCabId(Long cabId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsActiveRideByCabIdAndCabState(Long cabId, String cabState);
}