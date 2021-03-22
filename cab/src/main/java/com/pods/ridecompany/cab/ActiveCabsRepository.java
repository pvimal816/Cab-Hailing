package com.pods.ridecompany.cab;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface ActiveCabsRepository extends Repository<ActiveCab, Integer> {
    void save(ActiveCab activeCabs);
    List<ActiveCab> findActiveCabsByCabId(Integer cabId);
    void removeActiveCabByCabId(Integer cabId);
    boolean existsByCabId(Integer cabId);
}
