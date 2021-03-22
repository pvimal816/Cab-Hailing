package com.pods.ridecompany.cab;

import javax.persistence.*;

@Entity
public class ActiveRide {
    public static final String CAB_STATE_COMMITTED = "COMMITTED";
    public static final String CAB_STATE_GIVING_RIDE = "GIVING_RIDE";
    @Id
    Integer cabId;

    @Column(nullable = false)
    Integer rideId;

    @Column(nullable = false)
    Integer srcLoc;

    @Column(nullable = false)
    Integer dstLoc;

    @Column(nullable = false, columnDefinition = "varchar(256) default 'committed'")
    String cabState;

    public ActiveRide(Integer cabId, Integer rideId, Integer srcLoc, Integer dstLoc, String cabState) {
        this.cabId = cabId;
        this.rideId = rideId;
        this.srcLoc = srcLoc;
        this.dstLoc = dstLoc;
        this.cabState = cabState;
    }

    public ActiveRide(){}
}
