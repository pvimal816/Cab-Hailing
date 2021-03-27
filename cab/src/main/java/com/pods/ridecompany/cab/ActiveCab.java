package com.pods.ridecompany.cab;

import javax.persistence.*;

@Entity
public class ActiveCab {
    @Id
    Long cabId;
    @Column(nullable = false, columnDefinition = "numeric default '0'")
    Long rideCnt;
    @Column(nullable = false)
    Long lastStableLocation;
    @Column(nullable = false, columnDefinition = "numeric default '0'")
    Integer isInterested;

    public ActiveCab(Long cabId, Long lastStableLocation){
        this.cabId = cabId;
        this.rideCnt = 0L;
        this.lastStableLocation = lastStableLocation;
        this.isInterested = 1;
    }

    public ActiveCab(){}
}