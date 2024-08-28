package org.tdod.dod.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import org.tdod.dod.EditableFieldEnum;

public class UITextEditor {

    private boolean isEditing = false;
    private Rectangle location = new Rectangle(0,0,0,0);
    private String text = new String();
    private Color backgroundColor = Color.BLACK;
    private int maxLength = 0;
    private EditableFieldEnum field = EditableFieldEnum.NONE;
    private String regex;
    
    public UITextEditor() {
        
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean isEditing, int maxLength, Rectangle location, Color backgroundColor, EditableFieldEnum field, String regex) {
        this.isEditing = isEditing;
        this.location = location;
        this.backgroundColor = backgroundColor;
        this.maxLength = maxLength;
        this.field = field;
        this.regex = regex;
    }

    public void setEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }
     
    public Rectangle getLocation() {
        return location;
    }

    public void setLocation(Rectangle location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public EditableFieldEnum getField() {
        return field;
    }

    public void setField(EditableFieldEnum field) {
        this.field = field;
    }

    public void appendText(char c) {
        if (c == '\n') {
            // Carriage Return
            isEditing = false;
            
        } else if (c == '\b') {
            // Delete
            if (text != null && text.length() > 0) {
                text = text.substring(0, text.length() - 1);
            }
        } else {
            // Only take in alphanumeric characters.
            String validate = new String("" + c);
            if (validate.matches(regex) && text.length() < maxLength) {
                this.text = this.text + c;                            
            }
        }
    }
    
    public void setFont(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setColor(Color.YELLOW);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }
}
