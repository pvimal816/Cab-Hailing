package com.pods.ridecompany.rideservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ActiveCabsRepository extends Repository<ActiveCab, Integer> {
    void save(ActiveCab activeCabs);
    List<ActiveCab> findActiveCabsByCabId(Integer cabId);
    void removeActiveCabByCabId(Integer cabId);
    boolean existsByCabId(Integer cabId);

    List<ActiveCab> findAll();

    @Query(value = "SELECT TOP 3 * FROM active_cab WHERE is_available=true ORDER BY ABS(last_stable_location-:loc)", nativeQuery = true)
    List<ActiveCab> findNearestThreeCabs(Integer loc);
}
