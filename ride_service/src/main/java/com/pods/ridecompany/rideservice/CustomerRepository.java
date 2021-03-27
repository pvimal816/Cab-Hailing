package com.pods.ridecompany.rideservice;

import org.springframework.data.repository.Repository;

public interface CustomerRepository extends Repository<Customer, Long> {
    public void save(Customer customer);
    public boolean existsByCustIdAndPassword(Long custId, String password);
}
