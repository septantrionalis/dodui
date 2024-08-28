package org.tdod.dod.sprite.impl;

import java.awt.image.BufferedImage;

public class Sprite {
    private BufferedImage image;
    private int index;
    private int startIndex = -1;
    private int size = 0;
    private boolean isAnimationTimingRandom = true;
    
    public Sprite(BufferedImage image, int index) {
        this.image = image;
        this.index = index;
        this.startIndex = index;
    }
    
    public BufferedImage getImage() {
        return image;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isAnimationTimingRandom() {
        return isAnimationTimingRandom;
    }

    public void setAnimationTimingRandom(boolean isAnimationTimingRandom) {
        this.isAnimationTimingRandom = isAnimationTimingRandom;
    }
    
    
}
