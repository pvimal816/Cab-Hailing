package com.pods.ridecompany.rideservice;

import org.springframework.data.repository.Repository;

public interface CustomerRepository extends Repository<Customer, Integer> {
    public void save(Customer customer);
    public boolean existsByCustIdAndPassword(Integer custId, String password);
}
