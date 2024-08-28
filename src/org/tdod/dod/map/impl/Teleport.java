package org.tdod.dod.map.impl;

public class Teleport {

    public static final String NULL_KEYWORD = "null";
    
    private int teleportX = 0;
    private int teleportY = 0;
    private String teleportMap;
    
    public int getTeleportX() {
        return teleportX;
    }
    
    public void setTeleportX(int teleportX) {
        this.teleportX = teleportX;
    }
    
    public int getTeleportY() {
        return teleportY;
    }
    
    public void setTeleportY(int teleportY) {
        this.teleportY = teleportY;
    }
    
    public String getTeleportMap() {
        return teleportMap;
    }
    
    public void setTeleportMap(String teleportMap) {
        this.teleportMap = teleportMap;
    }


}
