package com.pods.ridecompany.cab;


import org.springframework.data.repository.Repository;

import java.util.List;

public interface CabCredentialsRepository extends Repository<CabCredentials, Integer> {
    void save(CabCredentials cabCredentials);
    boolean existsByCabIdAndPassword(Integer cabId, String password);
}
