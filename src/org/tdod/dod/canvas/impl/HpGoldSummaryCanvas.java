package org.tdod.dod.canvas.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import org.tdod.dod.GameContext;
import org.tdod.dod.player.AbilityScoreEnum;
import org.tdod.dod.player.impl.AbilityScores;

public class HpGoldSummaryCanvas extends AbstractCharacterCreationCanvas {

    private GameContext context;
    private AbilityScores abilityScores = new AbilityScores();

    public HpGoldSummaryCanvas(GameContext context) {
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
        this.requestFocusInWindow();
        
        abilityScores = context.getPlayer().getAbilityScores();
        intializePrimaryScoreList();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            setNextCanvas(new MapCanvas(context));
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

        displayAbilityScores(graphics, abilityScores, "Base Ability Scores");
        
        g2.drawString("Press any key to continue.", 100, 300);
        
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
