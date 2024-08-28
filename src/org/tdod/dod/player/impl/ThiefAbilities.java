package org.tdod.dod.player.impl;

public class ThiefAbilities {

    private byte openLocks = 0;
    private byte findTraps = 0;
    private byte removeTraps = 0;
    private byte climbWalls = 0;
    private byte moveSilently = 0;
    private byte hideInShadows = 0;
    private byte pickPockets = 0;
    private byte hearNoise = 0;
    
    public byte getOpenLocks() {
        return openLocks;
    }
    
    public void setOpenLocks(byte openLocks) {
        this.openLocks = openLocks;
    }
    
    public byte getFindTraps() {
        return findTraps;
    }
    
    public void setFindTraps(byte findTraps) {
        this.findTraps = findTraps;
    }
    
    public byte getRemoveTraps() {
        return removeTraps;
    }
    
    public void setRemoveTraps(byte removeTraps) {
        this.removeTraps = removeTraps;
    }
    
    public byte getClimbWalls() {
        return climbWalls;
    }
    
    public void setClimbWalls(byte climbWalls) {
        this.climbWalls = climbWalls;
    }
    
    public byte getMoveSilently() {
        return moveSilently;
    }
    
    public void setMoveSilently(byte moveSilently) {
        this.moveSilently = moveSilently;
    }
    
    public byte getHideInShadows() {
        return hideInShadows;
    }
    
    public void setHideInShadows(byte hideInShadows) {
        this.hideInShadows = hideInShadows;
    }
    
    public byte getPickPockets() {
        return pickPockets;
    }
    
    public void setPickPockets(byte pickPockets) {
        this.pickPockets = pickPockets;
    }
    
    public byte getHearNoise() {
        return hearNoise;
    }
    
    public void setHearNoise(byte hearNoise) {
        this.hearNoise = hearNoise;
    }
    
}
