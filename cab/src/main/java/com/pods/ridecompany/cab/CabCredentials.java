package com.pods.ridecompany.cab;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CabCredentials {
    @Id
    Long cabId;
    @Column(nullable = false, columnDefinition = "numeric default '0'")
    String password;

    public CabCredentials(Long cabId, String password) {
        this.cabId = cabId;
        this.password = password;
    }

    public CabCredentials(Long cabId) {
        this.cabId = cabId;
        this.password = "0";
    }

    public CabCredentials() {
    }
}
