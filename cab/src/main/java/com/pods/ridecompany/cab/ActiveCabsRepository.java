package com.pods.ridecompany.cab;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface ActiveCabsRepository extends Repository<ActiveCab, Long> {
    void save(ActiveCab activeCabs);
    List<ActiveCab> findActiveCabsByCabId(Long cabId);
    void removeActiveCabByCabId(Long cabId);
    boolean existsByCabId(Long cabId);
}
