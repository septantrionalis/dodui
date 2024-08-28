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
import org.tdod.dod.utils.Constants;

public class CreditCanvas extends AbstractGameCanvas {

    private static final long serialVersionUID = -4584441079476524594L;

    private GameContext context;
    
    private static final int MIN_SCROLL_VALUE = -400;
    
    private int index = 800;
    
    public CreditCanvas(GameContext context) {
        this.context = context;
    }

    @Override
    public void update() {
        updateAll();
        if (index > MIN_SCROLL_VALUE) {
            index--;            
        }
    }

    @Override
    public void throttledUpdate() {
    }

    @Override
    public void initialize() {
        initializeUIComponents();
        initializeBufferStrategy();
        this.requestFocusInWindow();
        context.getGameLibraries().startMusic(SongEnum.CREDITS);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        setNextCanvas(new IntroCanvas(context));
        context.getGameLibraries().stopMusic();
        return;
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
        g2.drawImage(GameUIComponents.getComponent(GameUIComponents.CREDIT_IMAGE),0,0,null);
        
    }

    private void drawText(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;

        g2.setFont(getIntroGameFont());
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        if (index > 50 && index < Constants.Y_RESOLUTION + 25) {
            g2.drawString("PROGRAMMING: ", 200, index - 25);            
        }
        if (index > 25 && index < Constants.Y_RESOLUTION) {
            g2.drawString("           RON KINNEY", 200, index);            
        }
        
        if (index > -25 && index < Constants.Y_RESOLUTION - 50) {
            g2.drawString("GRAPHICS: ", 200, index + 50);            
        }
        if (index > -50 && index < Constants.Y_RESOLUTION - 75) {
            g2.drawString("           MORTIMER DIPTHONG", 200, index + 75);            
        }
        if (index > -75 && index < Constants.Y_RESOLUTION - 100) {
            g2.drawString("           RON KINNEY", 200, index + 100);
        }

        if (index > -125 && index < Constants.Y_RESOLUTION - 150) {
            g2.drawString("MUSIC: ", 200, index + 150);            
        }
        if (index > -150 && index < Constants.Y_RESOLUTION - 175) {
            g2.drawString("           VARIOUS SITES", 200, index + 175);            
        }
        
        if (index > -200 && index < Constants.Y_RESOLUTION - 225) {
            g2.drawString("STORY: ", 200, index + 225);            
        }
        if (index > -225 && index < Constants.Y_RESOLUTION - 250) {
            g2.drawString("           RON KINNEY", 200, index + 250);            
        }

        if (index > -275 && index < Constants.Y_RESOLUTION - 325) {
            g2.drawString("DEDICATED TO: ", 200, index + 300);            
        }
        if (index > -300 && index < Constants.Y_RESOLUTION - 350) {
            g2.drawString("           KARNAK(SNACKERS), THE CAIRN TERRIER", 200, index + 325);            
        }
        if (index > -325 && index < Constants.Y_RESOLUTION - 375) {
            g2.drawString("           MINYA, THE WESTIE", 200, index + 350);            
        }
        
    }
}
