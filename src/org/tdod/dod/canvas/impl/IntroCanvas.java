package org.tdod.dod.canvas.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import org.tdod.dod.GameContext;
import org.tdod.dod.canvas.AbstractGameCanvas;
import org.tdod.dod.gui.GameUIComponents;
import org.tdod.dod.sound.SongEnum;

public class IntroCanvas extends AbstractGameCanvas {

    private static final long serialVersionUID = -4180383479590231971L;

    private static final int MAX_MENU_ITEMS = 3;
    
    private GameContext context;
    
    private int selectedMenuItem = 0;
    
    
    public IntroCanvas(GameContext context) {
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
        if (context.isPlayMusic()) {
            context.getGameLibraries().startMusic(SongEnum.INTRO);            
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (selectedMenuItem == 0) {
                setNextCanvas(new MapCanvas(context));
            } else if (selectedMenuItem == 1) {
                setNextCanvas(new AbilityScoreRollCanvas(context));
            } else if (selectedMenuItem == 2) {
                setNextCanvas(new CreditCanvas(context));                
            }
            context.getGameLibraries().stopMusic();
            return;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            context.getGameLibraries().getSounds().playSelect1();
            selectedMenuItem--;
            if (selectedMenuItem < 0) {
                selectedMenuItem = MAX_MENU_ITEMS - 1;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            context.getGameLibraries().getSounds().playSelect1();
            selectedMenuItem++;            
            if (selectedMenuItem > MAX_MENU_ITEMS - 1) {
                selectedMenuItem = 0;
            }
        }

    }

    private void updateAll() {
        synchronized (this) {
            BufferStrategy bufferStrategy = this.getBufferStrategy();
            Graphics graphics = bufferStrategy.getDrawGraphics();

            drawBackdrop(graphics);
            drawText(graphics);
            
            bufferStrategy.show();        
        }
    }
    
    private void drawBackdrop(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.drawImage(GameUIComponents.getComponent(GameUIComponents.INTRO_IMAGE),0,0,null);
        
    }
    
    private void drawText(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setFont(getIntroGameFont());
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        displayTextList(g2, 437, 350, "LOAD GAME", "NEW GAME", "CREDITS");

        g2.drawString(">", 420, 350 + (selectedMenuItem * 25));
        g2.drawString("<", 575, 350 + (selectedMenuItem * 25));
    }

}
