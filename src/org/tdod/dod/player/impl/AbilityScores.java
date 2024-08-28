package org.tdod.dod.player.impl;

import java.util.HashMap;
import java.util.Map;

import org.tdod.dod.player.AbilityScoreEnum;

public class AbilityScores {

    public static final int MAX_ABILITY_SCORE = 18;
    public static final int MIN_ABILITY_SCORE = 10;
    
    private Map<AbilityScoreEnum, Integer> abilityScores = new HashMap<AbilityScoreEnum, Integer>();
    
    public AbilityScores() {
        AbilityScoreEnum[] scores = AbilityScoreEnum.values();
        for (AbilityScoreEnum score : scores) {
            abilityScores.put(score, 0);
        }
    }
    
    public int getStrength() {
        return abilityScores.get(AbilityScoreEnum.STRENGTH);
    }
    
    public void setStrength(int strength) {
        abilityScores.put(AbilityScoreEnum.STRENGTH, strength);
    }
    
    public int getIntelligence() {
        return abilityScores.get(AbilityScoreEnum.INTELLIGENCE);
    }
    
    public void setIntelligence(int intelligence) {
        abilityScores.put(AbilityScoreEnum.INTELLIGENCE, intelligence);
    }
    
    public int getWisdom() {
        return abilityScores.get(AbilityScoreEnum.WISDOM);
    }
    
    public void setWisdom(int wisdom) {
        abilityScores.put(AbilityScoreEnum.WISDOM, wisdom);
    }
    
    public int getDexterity() {
        return abilityScores.get(AbilityScoreEnum.DEXTERITY);
    }
    
    public void setDexterity(int dexterity) {
        abilityScores.put(AbilityScoreEnum.DEXTERITY, dexterity);
    }
    
    public int getConstitution() {
        return abilityScores.get(AbilityScoreEnum.CONSTITUTION);
    }
    
    public void setConstitution(int constitution) {
        abilityScores.put(AbilityScoreEnum.CONSTITUTION, constitution);
    }
    
    public int getCharisma() {
        return abilityScores.get(AbilityScoreEnum.CHARISMA);
    }
    
    public void setCharisma(int charisma) {
        abilityScores.put(AbilityScoreEnum.CHARISMA, charisma);
    }

    public Integer getValue(AbilityScoreEnum score) {
        return abilityScores.get(score);
    }
    
    public void setValue(AbilityScoreEnum score, Integer value) {
        abilityScores.put(score, value);
    }

    public void setAbilityScores(AbilityScores scores) {
        setStrength(scores.getStrength());
        setIntelligence(scores.getIntelligence());
        setWisdom(scores.getWisdom());
        setDexterity(scores.getDexterity());
        setConstitution(scores.getConstitution());
        setCharisma(scores.getCharisma());
    }
}
