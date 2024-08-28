package org.tdod.dod.player.impl;

import org.tdod.dod.player.Alignment;

public enum Languages {

    COMMON("Common"),
    LAW(Alignment.LAW.getDisplayName()),
    NEUTRALITY(Alignment.NEUTRALITY.getDisplayName()),
    CHAOS(Alignment.CHAOS.getDisplayName()),
    ELF("Elf"),
    GNOLL("Gnoll"), 
    HOBGOBLIN("Hobgoblin"),
    ORC("Orc"),
    DWARF("Dwarf"), 
    GNOME("Gnome"),
    GOBLIN("Goblin"),
    KOBOLD("Kobold");
    
    private String displayName;
    
    private Languages(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    
}
