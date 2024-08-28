package org.tdod.dod.canvas.impl;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import org.tdod.dod.GameContext;
import org.tdod.dod.player.AbilityScoreEnum;
import org.tdod.dod.player.impl.AbilityScores;

public class AbilityScoreRollCanvas extends AbstractCharacterCreationCanvas {

    private static final long serialVersionUID = -6241789534192195391L;

    private GameContext context;
    private AbilityScores abilityScores = new AbilityScores();

    public AbilityScoreRollCanvas(GameContext context) {
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
        rollAbilityScores();
        
        initializeUIComponents();
        initializeBufferStrategy();
        intializePrimaryScoreList();
        this.requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            context.getPlayer().setAbilityScores(abilityScores);
            setNextCanvas(new ChooseClassCanvas(context));            
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            rollAbilityScores();
        }
        return;
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

        displayAbilityScores(graphics, abilityScores, "Base Ability Scores");
        
        g2.drawString("Press <Space> to reroll and <Enter> to accept.", 100, 300);
    }

    private void rollAbilityScores() {
        Random r = new Random();
        
        abilityScores.setStrength(r.nextInt(15) + 3);
        abilityScores.setIntelligence(r.nextInt(15) + 3);
        abilityScores.setWisdom(r.nextInt(15) + 3);
        abilityScores.setDexterity(r.nextInt(15) + 3);
        abilityScores.setConstitution(r.nextInt(15) + 3);
        abilityScores.setCharisma(r.nextInt(15) + 3);
    }
    
    private void intializePrimaryScoreList() {
        primaryScoreList.add(AbilityScoreEnum.STRENGTH);
        primaryScoreList.add(AbilityScoreEnum.INTELLIGENCE);
        primaryScoreList.add(AbilityScoreEnum.WISDOM);
        primaryScoreList.add(AbilityScoreEnum.DEXTERITY);
        primaryScoreList.add(AbilityScoreEnum.CONSTITUTION);
        primaryScoreList.add(AbilityScoreEnum.CHARISMA);
        
        for (AbilityScoreEnum abilityScoreEnum : primaryScoreList) {
            primaryScoreNameList.add(abilityScoreEnum.getDisplayName());            
        }
    }

}
