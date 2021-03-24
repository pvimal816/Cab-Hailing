package com.pods.ridecompany.rideservice;

import javax.persistence.*;

@Entity
public class ActiveCab {
    @Id
    Integer cabId;
    @Column(nullable = false, columnDefinition = "numeric default '0'")
    Integer rideCnt;
    @Column(nullable = false)
    Integer lastStableLocation;
    @Column(nullable = false)
    boolean isInterested;
    @Column(nullable = false)
    boolean isAvailable;

    public ActiveCab(Integer cabId, Integer lastStableLocation){
        this.cabId = cabId;
        this.rideCnt = 0;
        this.lastStableLocation = lastStableLocation;
        this.isInterested = true;
        this.isAvailable = true;
    }

    public ActiveCab(){}

    public Integer getCabId() {
        return cabId;
    }

    public void setCabId(Integer cabId) {
        this.cabId = cabId;
    }

    public Integer getRideCnt() {
        return rideCnt;
    }

    public void setRideCnt(Integer rideCnt) {
        this.rideCnt = rideCnt;
    }

    public Integer getLastStableLocation() {
        return lastStableLocation;
    }

    public void setLastStableLocation(Integer lastStableLocation) {
        this.lastStableLocation = lastStableLocation;
    }

    public boolean getIsInterested() {
        return isInterested;
    }

    public void setIsInterested(boolean isInterested) {
        this.isInterested = isInterested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActiveCab activeCab = (ActiveCab) o;

        return cabId != null ? cabId.equals(activeCab.cabId) : activeCab.cabId == null;
    }

    @Override
    public int hashCode() {
        return cabId != null ? cabId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ActiveCab{" +
                "cabId=" + cabId +
                ", rideCnt=" + rideCnt +
                ", lastStableLocation=" + lastStableLocation +
                ", isInterested=" + isInterested +
                '}';
    }
}
