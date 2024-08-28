package org.tdod.dod.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class GameUIComponents {

    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    public static final String CHECKED_CHECKBOX = "CHECKED_CHECKBOX";
    public static final String UNCHECKED_CHECKBOX = "UNCHECKED_CHECKBOX";
    public static final String LEFT_ARROW = "LEFT_ARROW";
    public static final String RIGHT_ARROW = "RIGHT_ARROW";
    public static final String SMALL_EDIT_BUTTON = "SMALL_EDIT_BUTTON";
    
    public static final String INTRO_IMAGE = "INTRO_IMAGE";
    public static final String CREDIT_IMAGE = "CREDIT_IMAGE";
    
    public static final String CLERIC_CLASS_IMAGE = "CLERIC_CLASS_IMAGE";
    public static final String FIGHTER_CLASS_IMAGE = "FIGHTER_CLASS_IMAGE";
    public static final String THIEF_CLASS_IMAGE = "THIEF_CLASS_IMAGE";
    public static final String MAGIC_USER_CLASS_IMAGE = "MAGIC_USER_CLASS_IMAGE";
    public static final String ELF_CLASS_IMAGE = "ELF_CLASS_IMAGE";
    public static final String HALFLING_CLASS_IMAGE = "HALFLING_CLASS_IMAGE";
    public static final String DWARF_CLASS_IMAGE = "DWARF_CLASS_IMAGE";
    
    private static final String UI_DIR = "ui/";
    
    private static Map<String, BufferedImage> components = new HashMap<String, BufferedImage>();
    
    static {
        try {
            components.put(CHECKED_CHECKBOX, ImageIO.read(new File(UI_DIR + "checked_box.png")));
            components.put(UNCHECKED_CHECKBOX, ImageIO.read(new File(UI_DIR + "unchecked_box.png")));
            components.put(LEFT_ARROW, ImageIO.read(new File(UI_DIR + "left_arrow.png")));
            components.put(RIGHT_ARROW, ImageIO.read(new File(UI_DIR + "right_arrow.png")));
            components.put(SMALL_EDIT_BUTTON, ImageIO.read(new File(UI_DIR + "small_edit_button.png")));
            components.put(INTRO_IMAGE, ImageIO.read(new File(UI_DIR + "intro.jpg")));
            components.put(CREDIT_IMAGE, ImageIO.read(new File(UI_DIR + "credit.jpg")));
            components.put(CLERIC_CLASS_IMAGE, ImageIO.read(new File(UI_DIR + "clericClass.png")));
            components.put(FIGHTER_CLASS_IMAGE, ImageIO.read(new File(UI_DIR + "fighterClass.png")));
            components.put(THIEF_CLASS_IMAGE, ImageIO.read(new File(UI_DIR + "thiefClass.png")));
            components.put(MAGIC_USER_CLASS_IMAGE, ImageIO.read(new File(UI_DIR + "magicuserClass.png")));
            components.put(ELF_CLASS_IMAGE, ImageIO.read(new File(UI_DIR + "elfClass.png")));
            components.put(HALFLING_CLASS_IMAGE, ImageIO.read(new File(UI_DIR + "halflingClass.png")));
            components.put(DWARF_CLASS_IMAGE, ImageIO.read(new File(UI_DIR + "dwarfClass.png")));

            log.info(components.size() + " UI components loaded.");
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
    public static BufferedImage getComponent(String key) {
        return components.get(key);
    }
    
    public static BufferedImage getCheckbox(boolean isChecked) {
        if (isChecked) {
            return GameUIComponents.getComponent(GameUIComponents.CHECKED_CHECKBOX);
        }
        
        return GameUIComponents.getComponent(GameUIComponents.UNCHECKED_CHECKBOX);
    }

    public static void drawDialogBox(Graphics2D g2, int x, int y, int width, int height, boolean colorFill, boolean rounded) {
        int strokeWidth = 16;
        if (colorFill == true) {
            g2.setColor(new Color(0xd3, 0xbf, 0x93));
            g2.setBackground(Color.BLACK);
            g2.fillRect(x + strokeWidth, y + strokeWidth, width - (strokeWidth * 2), height - (strokeWidth * 2));
        }
        Color c = new Color(0xb2, 0xa1, 0x7a);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(strokeWidth));
        if (rounded) {
            g2.drawRoundRect(x + (strokeWidth/2), y + (strokeWidth/2), width - strokeWidth, height - strokeWidth, 15, 15);
        } else {
            g2.drawRect(x + (strokeWidth/2), y + (strokeWidth/2), width - strokeWidth, height - strokeWidth);            
        }
        
        g2.setStroke(new BasicStroke(4));
        g2.drawLine(x + strokeWidth + 2, y + strokeWidth + 2, x + strokeWidth + 2, y + strokeWidth + 2);
        g2.drawLine(x + strokeWidth + 2, y + height - strokeWidth - 2, x + strokeWidth + 2, y + height - strokeWidth - 2);
        g2.drawLine(x + width - strokeWidth - 2, y + strokeWidth + 2, x + width - strokeWidth - 2, y + strokeWidth + 2);
        g2.drawLine(x + width - strokeWidth - 2, y + height - strokeWidth - 2, x + width - strokeWidth - 2, y + height - strokeWidth - 2);
    }
    
    private GameUIComponents() {
    }

}
