package com.pods.ridecompany.cab;

import javax.persistence.*;

@Entity
public class ActiveRide {
    public static final String CAB_STATE_COMMITTED = "COMMITTED";
    public static final String CAB_STATE_GIVING_RIDE = "GIVING_RIDE";
    @Id
    Long cabId;

    @Column(nullable = false)
    Long rideId;

    @Column(nullable = false)
    Long srcLoc;

    @Column(nullable = false)
    Long dstLoc;

    @Column(nullable = false, columnDefinition = "varchar(256) default 'committed'")
    String cabState;

    public ActiveRide(Long cabId, Long rideId, Long srcLoc, Long dstLoc, String cabState) {
        this.cabId = cabId;
        this.rideId = rideId;
        this.srcLoc = srcLoc;
        this.dstLoc = dstLoc;
        this.cabState = cabState;
    }

    public ActiveRide(){}
}
