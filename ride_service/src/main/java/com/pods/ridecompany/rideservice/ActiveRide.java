package com.pods.ridecompany.rideservice;

import javax.persistence.*;

@Entity
public class ActiveRide{
    public static final String CAB_STATE_COMMITTED = "COMMITTED";
    public static final String CAB_STATE_GIVING_RIDE = "GIVING-RIDE";
    Long cabId;
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="rideSeqGen")
    @SequenceGenerator(name = "rideSeqGen",
            initialValue = 1, allocationSize = 1
    )
    @Id
    Long rideId;
    @Column(nullable = false)
    Long srcLoc;
    @Column(nullable = false)
    Long dstLoc;
    @Column(nullable = false, columnDefinition = "varchar(256) default 'COMMITTED'")
    String cabState;
    @Column(nullable = false)
    Long custId;

    public ActiveRide(Long cabId, Long srcLoc, Long dstLoc, String cabState, Long custId) {
        this.cabId = cabId;
        this.srcLoc = srcLoc;
        this.dstLoc = dstLoc;
        this.cabState = cabState;
        this.custId = custId;
    }

    public ActiveRide(){}

    public Long getCabId() {
        return cabId;
    }

    public void setCabId(Long cabId) {
        this.cabId = cabId;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Long getSrcLoc() {
        return srcLoc;
    }

    public void setSrcLoc(Long srcLoc) {
        this.srcLoc = srcLoc;
    }

    public Long getDstLoc() {
        return dstLoc;
    }

    public void setDstLoc(Long dstLoc) {
        this.dstLoc = dstLoc;
    }

    public String getCabState() {
        return cabState;
    }

    public void setCabState(String cabState) {
        this.cabState = cabState;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    @Override
    public String toString() {
        return "ActiveRide{" +
                "cabId=" + cabId +
                ", rideId=" + rideId +
                ", srcLoc=" + srcLoc +
                ", dstLoc=" + dstLoc +
                ", cabState='" + cabState + '\'' +
                '}';
    }

}
