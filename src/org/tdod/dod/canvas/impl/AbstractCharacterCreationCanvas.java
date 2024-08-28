package org.tdod.dod.canvas.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import org.tdod.dod.canvas.AbstractGameCanvas;
import org.tdod.dod.player.AbilityScoreEnum;
import org.tdod.dod.player.impl.AbilityScores;

public abstract class AbstractCharacterCreationCanvas extends AbstractGameCanvas {
    
    private static final long serialVersionUID = 9075267728055789475L;

    protected List<AbilityScoreEnum> primaryScoreList = new ArrayList<AbilityScoreEnum>();
    protected List<String> primaryScoreNameList = new ArrayList<String>();

    protected void displayAbilityScores(Graphics graphics, AbilityScores abilityScores, String title) {
        Graphics2D g2 = (Graphics2D) graphics;

        g2.setFont(getIntroGameFont());
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g2.drawString(title, 100, 100);
        displayTextList(g2, 100, 125, primaryScoreNameList.toArray(new String[primaryScoreNameList.size()]));
        
        List<String> scoreValueList = new ArrayList<String>();
        for (AbilityScoreEnum score : primaryScoreList) {
            scoreValueList.add("" + abilityScores.getValue(score));
        }
        
        displayTextList(g2, 280, 125, scoreValueList.toArray(new String[scoreValueList.size()]));

    }
    
}
