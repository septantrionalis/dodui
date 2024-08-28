package org.tdod.dod.canvas.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tdod.dod.GameContext;
import org.tdod.dod.gui.GameUIComponents;
import org.tdod.dod.player.AbilityScoreEnum;
import org.tdod.dod.player.CharacterClass;
import org.tdod.dod.player.impl.AbilityScores;

import com.sun.javafx.geom.Rectangle;

public class ChooseClassCanvas extends AbstractCharacterCreationCanvas {

    private static final long serialVersionUID = 617998936198164531L;

    private static final Rectangle classRectangle = new Rectangle(450, 301, 154, 204);
    
    private List<CharacterClass> validClasses = new ArrayList<CharacterClass>();
    private List<String> validClassNames = new ArrayList<String>();
    private List<BufferedImage> validImages = new ArrayList<BufferedImage>();
    
    private GameContext context;

    private int selectedMenuItem = 0;

    public ChooseClassCanvas(GameContext context) {
        this.context = context;
        initializeValidClasses();
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
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            context.getGameLibraries().getSounds().playSelect1();
            selectedMenuItem--;
            if (selectedMenuItem < 0) {
                selectedMenuItem = validClasses.size() - 1;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            context.getGameLibraries().getSounds().playSelect1();
            selectedMenuItem++;            
            if (selectedMenuItem > validClasses.size() - 1) {
                selectedMenuItem = 0;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            context.getPlayer().setCharacterClass(validClasses.get(selectedMenuItem));
            setNextCanvas(new ExchangeAbilityScoresCanvas(context));
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
        
        AbilityScores abilityScores = context.getPlayer().getAbilityScores();
        displayAbilityScores(graphics, abilityScores, "Base Ability Scores");

        g2.drawString("Choose your class:", 100, 300);
        displayTextList(g2, 150, 325, validClassNames.toArray(new String[validClassNames.size()]));
        
        g2.drawString(">", 133, 325 + (selectedMenuItem * 25));
        g2.drawString("<", 300, 325 + (selectedMenuItem * 25));

        BufferedImage classImage = validImages.get(selectedMenuItem);
        
        g2.drawImage(classImage, classRectangle.x, classRectangle.y, classRectangle.width, classRectangle.height, null);
        GameUIComponents.drawDialogBox(g2, classRectangle.x - 16, classRectangle.y - 16, classRectangle.width + (16 * 2), classRectangle.height + (16 * 2), false, true);

        g2.setColor(Color.WHITE);
        g2.drawString("Cyan = Prime Requisite", 100, 550);    
    }
    
    private void initializeValidClasses() {
        validClasses.clear();
        validClasses.add(CharacterClass.CLERIC);
        validClasses.add(CharacterClass.FIGHTER);
        validClasses.add(CharacterClass.THIEF);
        validClasses.add(CharacterClass.MAGIC_USER);
        validClasses.add(CharacterClass.ELF);
        validClasses.add(CharacterClass.HALFLING);
        validClasses.add(CharacterClass.DWARF);
        
        List<CharacterClass> classValidationList = new ArrayList<CharacterClass>();
        classValidationList.add(CharacterClass.ELF);
        classValidationList.add(CharacterClass.HALFLING);
        classValidationList.add(CharacterClass.DWARF);
        
        for (CharacterClass characterClass : classValidationList) {
            Map<AbilityScoreEnum, Integer> minStatMap = characterClass.getMinimumStats();
            for (Map.Entry<AbilityScoreEnum, Integer> entry : minStatMap.entrySet()) {
                AbilityScoreEnum score = entry.getKey();
                Integer minimumValue = entry.getValue();
                
                int currentValue = context.getPlayer().getAbilityScores().getValue(score);
                if (currentValue < minimumValue) {
                    validClasses.remove(characterClass);
                    continue;
                }
                
            }
        }
        
        validClassNames.clear();
        for (CharacterClass characterClass : validClasses) {
            validClassNames.add(characterClass.getDisplayName());
            
            switch (characterClass) {
            case CLERIC:
                validImages.add(GameUIComponents.getComponent(GameUIComponents.CLERIC_CLASS_IMAGE));
                break;
            case FIGHTER:
                validImages.add(GameUIComponents.getComponent(GameUIComponents.FIGHTER_CLASS_IMAGE));
                break;
            case THIEF:
                validImages.add(GameUIComponents.getComponent(GameUIComponents.THIEF_CLASS_IMAGE));
                break;
            case MAGIC_USER:
                validImages.add(GameUIComponents.getComponent(GameUIComponents.MAGIC_USER_CLASS_IMAGE));
                break;
            case ELF:
                validImages.add(GameUIComponents.getComponent(GameUIComponents.ELF_CLASS_IMAGE));
                break;
            case HALFLING:
                validImages.add(GameUIComponents.getComponent(GameUIComponents.HALFLING_CLASS_IMAGE));
                break;
            case DWARF:
                validImages.add(GameUIComponents.getComponent(GameUIComponents.DWARF_CLASS_IMAGE));
                break;
            default:
                validImages.add(GameUIComponents.getComponent(GameUIComponents.FIGHTER_CLASS_IMAGE));
                break;
                
            }
        }
    }
    
    private void intializePrimaryScoreList() {
        primaryScoreList.clear();
        primaryScoreList.add(AbilityScoreEnum.STRENGTH);
        primaryScoreList.add(AbilityScoreEnum.INTELLIGENCE);
        primaryScoreList.add(AbilityScoreEnum.WISDOM);
        primaryScoreList.add(AbilityScoreEnum.DEXTERITY);
        primaryScoreList.add(AbilityScoreEnum.CONSTITUTION);
        primaryScoreList.add(AbilityScoreEnum.CHARISMA);

        primaryScoreNameList.clear();
        CharacterClass characterClass = validClasses.get(selectedMenuItem);
        List<AbilityScoreEnum> prs = characterClass.getPrimeRequisites();
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

}
