package org.tdod.dod.player.impl;

import java.util.ArrayList;
import java.util.List;

import org.tdod.dod.player.Alignment;
import org.tdod.dod.player.Armor;
import org.tdod.dod.player.CharacterClass;
import org.tdod.dod.player.Equipment;
import org.tdod.dod.player.Player;
import org.tdod.dod.player.Shield;
import org.tdod.dod.player.Weapon;

public class PlayerImpl implements Player {

    private static final long serialVersionUID = 2669222071874800897L;

    private int x;
    private int y;
    private String map = "northbridge";
    private CharacterClass characterClass;
    private String title;
    private byte level;
    private AbilityScores abilityScores = new AbilityScores();
    private SavingThrows savingThrows = new SavingThrows();
    private int exp;
    private int maxHp;
    private int currentHp;
    private int gold;
    private Weapon weapon = null;
    private Armor armor = null;
    private Shield shield = null;
    private List<Equipment> equipment = new ArrayList<Equipment>();
    private String name;
    private Alignment alignment;
    private ThiefAbilities thiefAbilities = new ThiefAbilities();
    private RacialAbilities racialAbilities = new RacialAbilities();
    private List<Languages> languages = new ArrayList<Languages>();
    
    public PlayerImpl() {
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String getMap() {
        return map;
    }

    @Override
    public void setMap(String map) {
        this.map = map;
    }
    
    @Override 
    public byte getArmorClass() {
        return 0;
    }
    
    @Override
    public byte getHitRoll() {
        return 0;
    }

    @Override
    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    @Override
    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public byte getLevel() {
        return level;
    }

    @Override
    public void setLevel(byte level) {
        this.level = level;
    }

    @Override
    public AbilityScores getAbilityScores() {
        return abilityScores;
    }

    @Override
    public void setAbilityScores(AbilityScores abilityScores) {
        this.abilityScores = abilityScores;
    }

    @Override
    public SavingThrows getSavingThrows() {
        return savingThrows;
    }

    @Override
    public void setSavingThrows(SavingThrows savingThrows) {
        this.savingThrows = savingThrows;
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
        this.exp = exp;
    }

    @Override
    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }
    
    @Override
    public int getGold() {
        return gold;
    }

    @Override
    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public Armor getArmor() {
        return armor;
    }

    @Override
    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    @Override
    public Shield getShield() {
        return shield;
    }

    @Override
    public void setShield(Shield shield) {
        this.shield = shield;
    }

    @Override
    public List<Equipment> getEquipment() {
        return equipment;
    }

    @Override
    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public ThiefAbilities getThiefAbilities() {
        return thiefAbilities;
    }

    @Override
    public void setThiefAbilities(ThiefAbilities thiefAbilities) {
        this.thiefAbilities = thiefAbilities;
    }

    @Override
    public RacialAbilities getRacialAbilities() {
        return racialAbilities;
    }

    @Override
    public void setRacialAbilities(RacialAbilities racialAbilities) {
        this.racialAbilities = racialAbilities;
    }

    @Override
    public List<Languages> getLanguages() {
        return languages;
    }

    @Override
    public void setLanguages(List<Languages> languages) {
        this.languages = languages;
    }

}
