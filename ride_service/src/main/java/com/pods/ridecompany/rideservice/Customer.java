package com.pods.ridecompany.rideservice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Customer {
    @Id
    Integer custId;
    @Column(nullable = false)
    String password;

    public Customer(Integer custId) {
        this.custId = custId;
        this.password = "0";
    }

    public Customer() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return custId != null ? custId.equals(customer.custId) : customer.custId == null;
    }

    @Override
    public int hashCode() {
        return custId != null ? custId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", password='" + password + '\'' +
                '}';
    }
}
