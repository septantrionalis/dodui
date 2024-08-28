package org.tdod.dod.canvas.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tdod.dod.GameContext;
import org.tdod.dod.player.AbilityScoreEnum;
import org.tdod.dod.player.CharacterClass;
import org.tdod.dod.player.impl.AbilityScores;

public class ExchangeAbilityScoresCanvas extends AbstractCharacterCreationCanvas {

    private static final long serialVersionUID = 1084729127202389250L;

    private GameContext context;

    private int selectedMenuItem = 0;
    private int secondarySelectionMenuItem = 0;

    private AbilityScores originalAbilityScores = new AbilityScores();
    private List<AbilityScoreEnum> secondaryScoreList = new ArrayList<AbilityScoreEnum>();
    private List<String> secondaryScoreListNames = new ArrayList<String>();
    
    private ExchangeAbilityScoreState state = ExchangeAbilityScoreState.FIRST_SELECTION;
    
    public ExchangeAbilityScoresCanvas(GameContext context) {
        this.context = context;
    }

    @Override
    public void update() {
        updateAll();
    }

    @Override
    public void throttledUpdate() {
    }

    @Override
    public void initialize() {
        initializeUIComponents();
        initializeBufferStrategy();
        initializeOriginalAbilityScores();
        this.requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            context.getGameLibraries().getSounds().playSelect1();
            if (ExchangeAbilityScoreState.FIRST_SELECTION.equals(state)) {
                selectedMenuItem--;
                if (selectedMenuItem < 0) {
                    selectedMenuItem = primaryScoreList.size() + 1;
                }                
            } else if (ExchangeAbilityScoreState.SECOND_SELECTION.equals(state)) {
                secondarySelectionMenuItem--;
                if (secondarySelectionMenuItem < 0) {
                    secondarySelectionMenuItem = secondaryScoreList.size();
                }                                
            }
            
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            context.getGameLibraries().getSounds().playSelect1();
            if (ExchangeAbilityScoreState.FIRST_SELECTION.equals(state)) {
                selectedMenuItem++;            
                if (selectedMenuItem > primaryScoreList.size() + 1) {
                    selectedMenuItem = 0;
                }
            } else if (ExchangeAbilityScoreState.SECOND_SELECTION.equals(state)) {
                secondarySelectionMenuItem++;            
                if (secondarySelectionMenuItem > secondaryScoreList.size()) {
                    secondarySelectionMenuItem = 0;
                }                
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (ExchangeAbilityScoreState.FIRST_SELECTION.equals(state)) {
                if (selectedMenuItem == primaryScoreList.size()) {
                    setNextCanvas(new HpGoldSummaryCanvas(context));                
                } else if (selectedMenuItem == (primaryScoreList.size() + 1)) {
                    context.getPlayer().getAbilityScores().setAbilityScores(originalAbilityScores);
                } else {
                    state = ExchangeAbilityScoreState.SECOND_SELECTION;
                }                
            } else if (ExchangeAbilityScoreState.SECOND_SELECTION.equals(state)) {
                if (secondarySelectionMenuItem != secondaryScoreList.size()) {
                    handleAbilityScoreExchange();
                }
                state = ExchangeAbilityScoreState.FIRST_SELECTION;                    
                secondarySelectionMenuItem = 0;
            }
        }
    }

    private void updateAll() {
        synchronized (this) {
            BufferStrategy bufferStrategy = this.getBufferStrategy();
            Graphics graphics = bufferStrategy.getDrawGraphics();

            drawBlackBackdrop(graphics);
            drawText(graphics);
            drawBorder(graphics);
            
            bufferStrategy.show();        
        }
    }

    private void drawText(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;

        intializePrimaryScoreList();

        displayAbilityScores(graphics, context.getPlayer().getAbilityScores(), "Ability score to lower");

        g2.setFont(getIntroGameFont());
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        if (ExchangeAbilityScoreState.FIRST_SELECTION.equals(state)) {
            g2.drawString("FINISHED", 100, 125 + (primaryScoreList.size() + 1) * 25);
            
            g2.drawString("RESET", 100, 125 + (primaryScoreList.size() + 2) * 25);            
        }
        
        g2.drawString("Class: " + context.getPlayer().getCharacterClass().getDisplayName(), 100, 350);

        if (ExchangeAbilityScoreState.FIRST_SELECTION.equals(state)) {
            if (primaryScoreList.size() > 0) {
                // g2.drawString("Select an ability score to lower", 100, 400);                
            } else {
                g2.drawString("Current ability scores cannot be exchanged.", 100, 400);                                
                g2.drawString("Press ENTER to continue.", 100, 425);
            }
        } else if (ExchangeAbilityScoreState.SECOND_SELECTION.equals(state)) {
            g2.drawString("Ability Score To Raise", 500, 100);
        }

        drawSelectedItems(g2, 83, 318, 125, selectedMenuItem, primaryScoreList.size());

        // Ability Score Exchanges.
        if (ExchangeAbilityScoreState.SECOND_SELECTION.equals(state)) {
            calculateValidSecondaryStats();
            
            if (secondaryScoreList.isEmpty()) {
                g2.drawString("The selected ability score cannot be exchanged.", 100, 400);                                
                g2.drawString("Press ENTER to continue.", 100, 425);
            }
            
            displayTextList(g2, 500, 125, secondaryScoreListNames.toArray(new String[secondaryScoreListNames.size()]));
            int x1 = 500;
            int x2 = 700;
            int y = 125 + (secondaryScoreListNames.size() + 1) * 25;
            g2.drawString("CANCEL", x1, y);

            drawSelectedItems(g2, x1 - 17, x2 - 17, 125, secondarySelectionMenuItem, secondaryScoreListNames.size());
        }
        
        g2.setColor(Color.WHITE);
        g2.drawString("Cyan = Prime Requisite", 100, 550);

    }
        
    private void drawSelectedItems(Graphics2D g2, int x1, int x2, int yStart, int menuIndex, int maxItems) {
        if (menuIndex == maxItems) {
            // Finish
            g2.drawString(">", x1, yStart + ((menuIndex + 1) * 25));
            g2.drawString("<", x2, yStart + ((menuIndex + 1) * 25));            
        } else if (menuIndex == (maxItems + 1)) {
            // Reset
            g2.drawString(">", x1, yStart + ((menuIndex + 1) * 25));
            g2.drawString("<", x2, yStart + ((menuIndex + 1) * 25));            
        } else {
            // Attributes
            g2.drawString(">", x1, yStart + (menuIndex * 25));
            g2.drawString("<", x2, yStart + (menuIndex * 25));
        }        
    }
    
    private void intializePrimaryScoreList() {
        primaryScoreList.clear();
        AbilityScores scores = context.getPlayer().getAbilityScores();
        // Can't lower a stat below the minimum score.        
        if (scores.getStrength() > AbilityScores.MIN_ABILITY_SCORE) {
            primaryScoreList.add(AbilityScoreEnum.STRENGTH);            
        }
        if (scores.getIntelligence() > AbilityScores.MIN_ABILITY_SCORE) {
            primaryScoreList.add(AbilityScoreEnum.INTELLIGENCE);
        }
        if (scores.getWisdom() > AbilityScores.MIN_ABILITY_SCORE) {
            primaryScoreList.add(AbilityScoreEnum.WISDOM);
        }
        if (scores.getConstitution() > AbilityScores.MIN_ABILITY_SCORE) {
            primaryScoreList.add(AbilityScoreEnum.CONSTITUTION);
        }
        if (scores.getCharisma() > AbilityScores.MIN_ABILITY_SCORE) {
            primaryScoreList.add(AbilityScoreEnum.CHARISMA);
        }

        primaryScoreNameList.clear();
        List<AbilityScoreEnum> prs = context.getPlayer().getCharacterClass().getPrimeRequisites();
        for (AbilityScoreEnum abilityScoreEnum : primaryScoreList) {
            String displayName = abilityScoreEnum.getDisplayName();
            for (AbilityScoreEnum pr : prs) {
                if (pr.equals(abilityScoreEnum)) {
                    displayName = "&C" + abilityScoreEnum.getDisplayName();
                    break;                    
                }
            }

            primaryScoreNameList.add(displayName);            
        }
    }
    
    private void calculateValidSecondaryStats() {
        secondaryScoreList.clear();
        secondaryScoreList.add(AbilityScoreEnum.STRENGTH);            
        secondaryScoreList.add(AbilityScoreEnum.INTELLIGENCE);
        secondaryScoreList.add(AbilityScoreEnum.WISDOM);
        secondaryScoreList.add(AbilityScoreEnum.DEXTERITY);
        secondaryScoreList.add(AbilityScoreEnum.CONSTITUTION);
        secondaryScoreList.add(AbilityScoreEnum.CHARISMA);
        
        AbilityScoreEnum selectedStat = primaryScoreList.get(selectedMenuItem);

        // Cant exchange a stat for itself.
        secondaryScoreList.remove(selectedStat);
        
        // Con and Cha cannot be exchanged.
        if (AbilityScoreEnum.CONSTITUTION.equals(selectedStat)) {
            secondaryScoreList.remove(AbilityScoreEnum.CHARISMA);
        }
        if (AbilityScoreEnum.CHARISMA.equals(selectedStat)) {
            secondaryScoreList.remove(AbilityScoreEnum.CONSTITUTION);
        }
        
        // Dex cannot be lowered, but can be raised if thief or halfling
        if (!CharacterClass.THIEF.equals(context.getPlayer().getCharacterClass()) &&
            !CharacterClass.HALFLING.equals(context.getPlayer().getCharacterClass())) {
            secondaryScoreList.remove(AbilityScoreEnum.DEXTERITY);            
        }
        
        // Stats cannot be higher than 18.
        for (Iterator<AbilityScoreEnum> iterator = secondaryScoreList.iterator(); iterator.hasNext();) {
            AbilityScoreEnum abilityScore = iterator.next();
            Integer value = context.getPlayer().getAbilityScores().getValue(abilityScore);
            if (value >= AbilityScores.MAX_ABILITY_SCORE) {
                iterator.remove();
            }
        }
        
        secondaryScoreListNames.clear();
        List<AbilityScoreEnum> prs = context.getPlayer().getCharacterClass().getPrimeRequisites();
        for (AbilityScoreEnum abilityScoreEnum : secondaryScoreList) {
            String displayName = abilityScoreEnum.getDisplayName();
            for (AbilityScoreEnum pr : prs) {
                if (pr.equals(abilityScoreEnum)) {
                    displayName = "&C" + abilityScoreEnum.getDisplayName();
                    break;                    
                }
            }
            secondaryScoreListNames.add(displayName);
        }
    }

    private void handleAbilityScoreExchange() {
        AbilityScoreEnum selectedPrimaryScore = primaryScoreList.get(selectedMenuItem);
        AbilityScoreEnum selectedSecondaryScore = secondaryScoreList.get(secondarySelectionMenuItem);

        int selectedPrimaryScoreValue = context.getPlayer().getAbilityScores().getValue(selectedPrimaryScore);
        int selectedSecondaryScoreValue = context.getPlayer().getAbilityScores().getValue(selectedSecondaryScore);
        
        List<AbilityScoreEnum> primeRequisiteList = context.getPlayer().getCharacterClass().getPrimeRequisites();

        int reduction = 1;
        for (AbilityScoreEnum primeRequisite : primeRequisiteList) {            
            if (primeRequisite.equals(selectedSecondaryScore)) {
                reduction = 2;
                break;
            }                
        }
        
        context.getPlayer().getAbilityScores().setValue(selectedPrimaryScore, selectedPrimaryScoreValue - reduction);
        context.getPlayer().getAbilityScores().setValue(selectedSecondaryScore, selectedSecondaryScoreValue + 1);
        
    }
    
    private void initializeOriginalAbilityScores() {
        AbilityScores scores = context.getPlayer().getAbilityScores();
        originalAbilityScores.setAbilityScores(scores);
    }
    
    private enum ExchangeAbilityScoreState {
        FIRST_SELECTION,
        SECOND_SELECTION;
    }

}
