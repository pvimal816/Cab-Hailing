package com.pods.ridecompany.cab;

import javax.persistence.*;

@Entity
public class ActiveCab {
    @Id
    Integer cabId;
    @Column(nullable = false, columnDefinition = "numeric default '0'")
    Integer rideCnt;
    @Column(nullable = false)
    Integer lastStableLocation;
    @Column(nullable = false, columnDefinition = "numeric default '0'")
    Integer isInterested;

    public ActiveCab(Integer cabId, Integer lastStableLocation){
        this.cabId = cabId;
        this.rideCnt = 0;
        this.lastStableLocation = lastStableLocation;
        this.isInterested = 1;
    }

    public ActiveCab(){}
}