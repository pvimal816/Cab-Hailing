package com.pods.ridecompany.rideservice;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ActiveCabsRepository extends Repository<ActiveCab, Long> {
    void save(ActiveCab activeCabs);
    List<ActiveCab> findActiveCabsByCabId(Long cabId);
    void removeActiveCabByCabId(Long cabId);
    boolean existsByCabId(Long cabId);

    List<ActiveCab> findAll();

    @Query(value = "SELECT TOP 3 * FROM active_cab WHERE is_available=true ORDER BY ABS(last_stable_location-:loc)", nativeQuery = true)
    List<ActiveCab> findNearestThreeCabs(Long loc);
}
