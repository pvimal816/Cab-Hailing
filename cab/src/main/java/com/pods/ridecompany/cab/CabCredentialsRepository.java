package com.pods.ridecompany.cab;


import org.springframework.data.repository.Repository;

import java.util.List;

public interface CabCredentialsRepository extends Repository<CabCredentials, Long> {
    void save(CabCredentials cabCredentials);
    boolean existsByCabIdAndPassword(Long cabId, String password);
    boolean existsByCabId(Long cabId);
}
