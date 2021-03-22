package com.pods.ridecompany.rideservice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Cab {
    @Id
    Integer cabId;
    @Column(nullable = false, columnDefinition = "numeric default '0'")
    String password;

    public Cab(Integer cabId, String password) {
        this.cabId = cabId;
        this.password = password;
    }

    public Cab(Integer cabId) {
        this.cabId = cabId;
        this.password = "0";
    }

    public Cab() {
    }
}
