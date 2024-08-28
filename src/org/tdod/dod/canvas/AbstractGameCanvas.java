package org.tdod.dod.canvas;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.tdod.dod.gui.GameUIComponents;
import org.tdod.dod.utils.Constants;

public abstract class AbstractGameCanvas extends Canvas implements GameCanvas {

    private static final long serialVersionUID = -4286796696715178846L;

    private Font introGameFont = new Font("pixelmix", Font.TRUETYPE_FONT, 20);
    private Font gameFont = new Font("pixelmix", Font.TRUETYPE_FONT, 14);

    private Rectangle border = new Rectangle(0, 0, Constants.X_RESOLUTION, Constants.Y_RESOLUTION);

    private GameCanvas nextCanvas = null;

    @Override
    public void addListeners() {
        this.addKeyListener(this);
    }
    
    @Override
    public void removeListeners() {
        this.removeKeyListener(this);
    }

    @Override
    public GameCanvas getNextCanvas() {
        return nextCanvas;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    protected void initializeUIComponents() {
    }
    
    protected void drawBorder(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        GameUIComponents.drawDialogBox(g2, border.x, border.y, border.width, border.height, false, false);
    }

    protected Font getGameFont() {
        return gameFont;
    }
    
    protected Font getIntroGameFont() {
        return introGameFont;
    }
    
    protected void initializeBufferStrategy() {
        this.createBufferStrategy(3);
    }
    
    protected void setNextCanvas(GameCanvas nextCanvas) {
        this.nextCanvas = nextCanvas;
    }
 
    protected void drawBlackBackdrop(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, Constants.X_RESOLUTION, Constants.Y_RESOLUTION);
    }

    protected void displayTextList(Graphics2D g2, int x, int y, String... texts) {
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        int height = 25;
        for (String text : texts) {
            String modifiedText = text;
            if (text.startsWith("&")) {
                Color color = Color.WHITE;
                if (text.substring(1).startsWith("Y")) {
                    color = Color.YELLOW;
                } else if (text.substring(1).startsWith("B")) {
                    color = Color.BLUE;                    
                } else if (text.substring(1).startsWith("C")) {
                    color = Color.CYAN;
                }
                g2.setColor(color);

                modifiedText = text.substring(text.indexOf("&") + 2);
            } else {
                g2.setColor(Color.WHITE);                
            }
            g2.drawString(modifiedText, x, y);
            y += height;
        }        
        g2.setColor(Color.WHITE);                

    }
}
