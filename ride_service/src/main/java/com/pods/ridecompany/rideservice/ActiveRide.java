package com.pods.ridecompany.rideservice;

import javax.persistence.*;

@Entity
public class ActiveRide{
    public static final String CAB_STATE_COMMITTED = "COMMITTED";
    public static final String CAB_STATE_GIVING_RIDE = "GIVING-RIDE";
    Integer cabId;
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="rideSeqGen")
    @SequenceGenerator(name = "rideSeqGen",
            initialValue = 1, allocationSize = 1
    )
    @Id
    Integer rideId;
    @Column(nullable = false)
    Integer srcLoc;
    @Column(nullable = false)
    Integer dstLoc;
    @Column(nullable = false, columnDefinition = "varchar(256) default 'COMMITTED'")
    String cabState;
    @Column(nullable = false)
    Integer custId;

    public ActiveRide(Integer cabId, Integer srcLoc, Integer dstLoc, String cabState, Integer custId) {
        this.cabId = cabId;
        this.srcLoc = srcLoc;
        this.dstLoc = dstLoc;
        this.cabState = cabState;
        this.custId = custId;
    }

    public ActiveRide(){}

    public Integer getCabId() {
        return cabId;
    }

    public void setCabId(Integer cabId) {
        this.cabId = cabId;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public Integer getSrcLoc() {
        return srcLoc;
    }

    public void setSrcLoc(Integer srcLoc) {
        this.srcLoc = srcLoc;
    }

    public Integer getDstLoc() {
        return dstLoc;
    }

    public void setDstLoc(Integer dstLoc) {
        this.dstLoc = dstLoc;
    }

    public String getCabState() {
        return cabState;
    }

    public void setCabState(String cabState) {
        this.cabState = cabState;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
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
