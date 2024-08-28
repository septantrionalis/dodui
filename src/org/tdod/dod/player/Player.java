package org.tdod.dod.player;

import java.io.Serializable;
import java.util.List;

import org.tdod.dod.player.impl.AbilityScores;
import org.tdod.dod.player.impl.Languages;
import org.tdod.dod.player.impl.RacialAbilities;
import org.tdod.dod.player.impl.SavingThrows;
import org.tdod.dod.player.impl.ThiefAbilities;

public interface Player extends Serializable {

    public int getX();

    public void setX(int x);

    public int getY();

    public void setY(int y);

    public String getMap();

    public void setMap(String map);

    public byte getArmorClass();

    public byte getHitRoll();

    public CharacterClass getCharacterClass();

    public void setCharacterClass(CharacterClass characterClass);

    public String getTitle();

    public void setTitle(String title);

    public byte getLevel();

    public void setLevel(byte level);

    public AbilityScores getAbilityScores();

    public void setAbilityScores(AbilityScores abilityScores);

    public SavingThrows getSavingThrows();

    public void setSavingThrows(SavingThrows savingThrows);
    
    public int getExp();

    public void setExp(int exp);

    public int getMaxHp();

    public void setMaxHp(int maxHp);

    public int getCurrentHp();

    public void setCurrentHp(int currentHp);

    public int getGold();

    public void setGold(int gold);

    public Weapon getWeapon();

    public void setWeapon(Weapon weapon);

    public Armor getArmor();

    public void setArmor(Armor armor);

    public Shield getShield();

    public void setShield(Shield shield);

    public List<Equipment> getEquipment();

    public void setEquipment(List<Equipment> equipment);

    public String getName();

    public void setName(String name);

    public Alignment getAlignment();

    public void setAlignment(Alignment alignment);

    public ThiefAbilities getThiefAbilities();

    public void setThiefAbilities(ThiefAbilities thiefAbilities);

    public RacialAbilities getRacialAbilities();

    public void setRacialAbilities(RacialAbilities racialAbilities);

    public List<Languages> getLanguages();

    public void setLanguages(List<Languages> languages);

}
