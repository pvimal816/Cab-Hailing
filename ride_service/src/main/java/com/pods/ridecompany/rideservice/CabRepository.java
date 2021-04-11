package com.pods.ridecompany.rideservice;


import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.Repository;

import javax.persistence.LockModeType;

public interface CabRepository extends Repository<Cab, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void save(Cab cab);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByCabId(Long cabId);
}
