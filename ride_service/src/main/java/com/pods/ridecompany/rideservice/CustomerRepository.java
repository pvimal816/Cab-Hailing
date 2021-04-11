package com.pods.ridecompany.rideservice;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.Repository;

public interface CustomerRepository extends Repository<Customer, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void save(Customer customer);
    public boolean existsByCustIdAndPassword(Long custId, String password);
}
