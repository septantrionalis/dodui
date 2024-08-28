package org.tdod.dod.player.impl;

public class RacialAbilities {
    private byte detectTrap = 0;
    private byte detectHiddenDoors = 0;
    private boolean ghoulParalysisImmunity = false;
    private byte hide = 0;
    private boolean acBonusVsLargeCreatures = false;
    private boolean hrBonusWithMissileWeapons = false;
    private boolean initiativeBonus = false;
    private boolean infrared = false;
    
    public byte getDetectTrap() {
        return detectTrap;
    }
    
    public void setDetectTrap(byte detectTrap) {
        this.detectTrap = detectTrap;
    }
    
    public byte getDetectHiddenDoors() {
        return detectHiddenDoors;
    }
    
    public void setDetectHiddenDoors(byte detectHiddenDoors) {
        this.detectHiddenDoors = detectHiddenDoors;
    }
    
    public boolean isGhoulParalysisImmunity() {
        return ghoulParalysisImmunity;
    }
    
    public void setGhoulParalysisImmunity(boolean ghoulParalysisImmunity) {
        this.ghoulParalysisImmunity = ghoulParalysisImmunity;
    }
    
    public byte getHide() {
        return hide;
    }
    
    public void setHide(byte hide) {
        this.hide = hide;
    }
    
    public boolean isAcBonusVsLargeCreatures() {
        return acBonusVsLargeCreatures;
    }
    
    public void setAcBonusVsLargeCreatures(boolean acBonusVsLargeCreatures) {
        this.acBonusVsLargeCreatures = acBonusVsLargeCreatures;
    }
    
    public boolean isHrBonusWithMissileWeapons() {
        return hrBonusWithMissileWeapons;
    }
    
    public void setHrBonusWithMissileWeapons(boolean hrBonusWithMissileWeapons) {
        this.hrBonusWithMissileWeapons = hrBonusWithMissileWeapons;
    }
    
    public boolean isInitiativeBonus() {
        return initiativeBonus;
    }
    
    public void setInitiativeBonus(boolean initiativeBonus) {
        this.initiativeBonus = initiativeBonus;
    }

    public boolean isInfrared() {
        return infrared;
    }

    public void setInfrared(boolean infrared) {
        this.infrared = infrared;
    }
    
    
}
