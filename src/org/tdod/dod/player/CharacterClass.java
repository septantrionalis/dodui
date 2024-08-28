package org.tdod.dod.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CharacterClass {
    CLERIC("Cleric"),
    FIGHTER("Fighter"),
    THIEF("Thief"),
    MAGIC_USER("Magic User"),
    ELF("Elf"),
    HALFLING("Halfling"),
    DWARF("Dwarf");
    
    private String displayName;
    private List<AbilityScoreEnum> primeRequisites = new ArrayList<AbilityScoreEnum>();
    private Map<AbilityScoreEnum, Integer> minimumStats = new HashMap<AbilityScoreEnum, Integer>();
    
    private CharacterClass(String displayName) {
        this.displayName = displayName;
        
        if ("Cleric".equals(displayName)) {
            primeRequisites.add(AbilityScoreEnum.WISDOM);
        } else if ("Fighter".equals(displayName)) {
            primeRequisites.add(AbilityScoreEnum.STRENGTH);            
        } else if ("Thief".equals(displayName)) {
            primeRequisites.add(AbilityScoreEnum.DEXTERITY);
        } else if ("Magic User".equals(displayName)) {
            primeRequisites.add(AbilityScoreEnum.INTELLIGENCE);
        } else if ("Elf".equals(displayName)) {
            primeRequisites.add(AbilityScoreEnum.STRENGTH);
            primeRequisites.add(AbilityScoreEnum.INTELLIGENCE);
            minimumStats.put(AbilityScoreEnum.INTELLIGENCE, 9);
        } else if ("Halfling".equals(displayName)) {
            primeRequisites.add(AbilityScoreEnum.STRENGTH);
            primeRequisites.add(AbilityScoreEnum.DEXTERITY);
            minimumStats.put(AbilityScoreEnum.CONSTITUTION, 9);
            minimumStats.put(AbilityScoreEnum.DEXTERITY, 9);
        } else if ("Dwarf".equals(displayName)) {
            primeRequisites.add(AbilityScoreEnum.STRENGTH);            
            minimumStats.put(AbilityScoreEnum.CONSTITUTION, 9);
        }
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public List<AbilityScoreEnum> getPrimeRequisites() {
        return primeRequisites;
    }
    
    public Map<AbilityScoreEnum, Integer> getMinimumStats() {
        return minimumStats;
    }
}
