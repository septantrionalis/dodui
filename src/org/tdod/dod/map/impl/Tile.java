package org.tdod.dod.map.impl;

import org.tdod.dod.map.OverlandMap;

public class Tile {

    private int spriteValue;
    private boolean isImpassable = false;
    private boolean isNpcImpassable = false;
    private int openDoorValue = -1;
    private Teleport teleport = null;
    
    private transient boolean isClosed = true;
    
    public int getSpriteValue() {
        return spriteValue;
    }

    public void setSpriteValue(int spriteValue) {
        this.spriteValue = spriteValue;
    }

    public boolean isImpassable() {
        return isImpassable;
    }

    public void setImpassable(boolean isImpassable) {
        this.isImpassable = isImpassable;
    }

    public boolean isNpcImpassable() {
        return isNpcImpassable;
    }

    public void setNpcImpassable(boolean isNpcImpassable) {
        this.isNpcImpassable = isNpcImpassable;
    }
    
    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public int getOpenDoorValue() {
        return openDoorValue;
    }

    public void setOpenDoorValue(int door) {
        this.openDoorValue = door;
    }

    public boolean isDoor() {
        return (getOpenDoorValue() >= 0) ? true : false;
    }

    public void removeDoor() {
        setOpenDoorValue(-1);
    }
    
    public void setDefaultDoor() {
        setOpenDoorValue(5);
    }
    
    public Teleport getTeleport() {
        return teleport;
    }

    public void setTeleport(Teleport teleport) {
        this.teleport = teleport;
    }
    
    public boolean isTeleporter() {
        return (getTeleport() != null) ? true : false;
    }
    
    public void removeTeleporter() {
        setTeleport(null);
    }
    
    public void setDefaultTeleporter() {
        Teleport t = new Teleport();
        t.setTeleportMap(OverlandMap.DEFAULT_MAP);
        t.setTeleportX(50);
        t.setTeleportY(50);
        this.teleport = t;
    }
    
    public void copyProperties(Tile tile) {
        this.isImpassable = tile.isImpassable();
        this.isNpcImpassable = tile.isNpcImpassable();
        this.openDoorValue = tile.getOpenDoorValue();
        this.teleport = tile.getTeleport();
    }
    
    public boolean hasPropertiesSet() {
        if (isImpassable == true) {
            return true;
        }
        if (isNpcImpassable == true) {
            return true;
        }
        if (openDoorValue > -1) {
            return true;
        }
        if (null != teleport) {
            return true;
        }
        
        return false;
        
    }
}
