package org.tdod.dod.player;

public enum Alignment {

    LAW("Law"),
    NEUTRALITY("Neutrality"),
    CHAOS("Chaos");
    
    private String displayName;
    
    private Alignment(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
}
