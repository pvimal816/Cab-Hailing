package com.pods.ridecompany.rideservice;

import javax.persistence.*;

@Entity
public class ActiveCab {
    @Id
    Long cabId;
    @Column(nullable = false, columnDefinition = "numeric default '0'")
    Long rideCnt;
    @Column(nullable = false)
    Long lastStableLocation;
    @Column(nullable = false)
    boolean isInterested;
    @Column(nullable = false)
    boolean isAvailable;

    public ActiveCab(Long cabId, Long lastStableLocation){
        this.cabId = cabId;
        this.rideCnt = 0L;
        this.lastStableLocation = lastStableLocation;
        this.isInterested = true;
        this.isAvailable = true;
    }

    public ActiveCab(){}

    public Long getCabId() {
        return cabId;
    }

    public void setCabId(Long cabId) {
        this.cabId = cabId;
    }

    public Long getRideCnt() {
        return rideCnt;
    }

    public void setRideCnt(Long rideCnt) {
        this.rideCnt = rideCnt;
    }

    public Long getLastStableLocation() {
        return lastStableLocation;
    }

    public void setLastStableLocation(Long lastStableLocation) {
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
