package org.tdod.dod.sprite.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.tdod.dod.canvas.impl.EditorCanvas;
import org.tdod.dod.sprite.SpriteSheet;
import org.tdod.dod.utils.Utilities;

public class SpriteSheetImpl implements SpriteSheet {

    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String SPRITE_DIR = "sprites/";
    
    private static final String ANIMATED_SPRITES_FILE = SPRITE_DIR + "animatedsprites.dat";

    private List<Sprite> sprites = new ArrayList<Sprite>();
    
    // This should contain the first sprite in the animation series.
    private Map<Integer, Sprite> animatedSprites = new HashMap<Integer, Sprite>();
    private Map<Integer, Long> lastUpdated = new HashMap<Integer, Long>();
    
    private Map<Integer, List<Integer>> builderMap = new HashMap<Integer, List<Integer>>();
    private List<Integer> displayableSpriteList = new ArrayList<Integer>();
    
    public SpriteSheetImpl() {
    }

    @Override
    public void initialize() {
        if (sprites.size() > 0) {
            throw new RuntimeException("Sprites already has data.  Why is this being called more than once?");
        }
        try {
            String filename = SPRITE_DIR + "sprites.txt";
            log.info("Loading " + filename);
            Scanner scanner = new Scanner(new File(filename));
            int index = 0;
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    if (line.contains(".png")) {
                        BufferedImage image = ImageIO.read(new File(SPRITE_DIR + line));
                        sprites.add(new Sprite(image, index));
                        index++;
                    }
                } catch (IOException e) {
                    log.info("Error loading sprite " + line);
                }
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        initSpriteProperties();
        
        initBuilderMap();            
        
        log.info(sprites.size() + " static sprites loaded.");
        log.info(animatedSprites.size() + " animated sprites loaded.");
        log.info(displayableSpriteList.size() + " displayable sprites.");
        
    }

    @Override
    public Sprite getSprite(int index) {
        Sprite sprite = sprites.get(index);
        if (sprite.getSize() == 0) {
            return sprite;            
        }

        long currentMillis = System.currentTimeMillis();
        sprite = animatedSprites.get(index);
        long lastMillis = lastUpdated.get(index);
        
        // Randomize the sprite and interval of sprites for animation.
        int delta;
        if (sprite.isAnimationTimingRandom()) {
            delta = 500 + ThreadLocalRandom.current().nextInt(100, 2000);
        } else {
            delta = 1000;
        }
        if (currentMillis >= (lastMillis + delta)) {
            int randomSpriteIndex;
            if (sprite.isAnimationTimingRandom()) {
                // Just find some "random" sprite in the series.
                randomSpriteIndex = (int)(System.currentTimeMillis() % sprite.getSize()) + sprite.getStartIndex();                
            } else {
                // Loop through the list of sprites in the series.  If the max sprite is hit, start back at the bottom.
                randomSpriteIndex = sprite.getIndex() + 1;
                if (randomSpriteIndex > sprite.getStartIndex() + (sprite.getSize() - 1)) {
                    randomSpriteIndex = sprite.getStartIndex();
                }
            }
            animatedSprites.put(index, sprites.get(randomSpriteIndex));
            lastUpdated.put(index, currentMillis);
        }

        return sprite;
    }

    @Override
    public Sprite getActualSprite(int index) {
        return sprites.get(index);
    }
    
    @Override
    public List<Integer> getBuilderList(int index) {
        return builderMap.get(index);
    }

    @Override
    public int getBuilderListRows() {
        return builderMap.size();
    }

    @Override
    public int getDisplayableSpriteCount() {
        return displayableSpriteList.size();
    }

    private void initSpriteProperties() {
        // Tile#, Number of frames.
        log.info("Loading " + ANIMATED_SPRITES_FILE);
        Map<Integer, Integer> animationData = new HashMap<Integer, Integer>();
        Set<Integer> staticAnimationTimingList = new HashSet<Integer>();
        try {
            Scanner scanner = new Scanner(new File(ANIMATED_SPRITES_FILE));
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] split = line.split(",");
                int spriteIndex = Integer.valueOf(split[0]);
                int spriteSize = Integer.valueOf(split[1]);
                boolean isAnimationTimingRandom = Utilities.booleanFromString((split[2]));
                animationData.put(spriteIndex, spriteSize);
                if (!isAnimationTimingRandom) {
                    // Add the entire series to the list.
                    for (int count = 0; count < spriteSize; count++) {
                        staticAnimationTimingList.add(spriteIndex + count);
                    }                    
                }
            }
            scanner.close();
                
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        for (Map.Entry<Integer, Integer> entry : animationData.entrySet()) {
            Integer spriteIndex = entry.getKey();
            Integer spriteFrames = entry.getValue();
            for (int count = (spriteFrames-1); count >= 0; count--) {
                Sprite sprite = sprites.get(spriteIndex + count);
                sprite.setStartIndex(spriteIndex);
                sprite.setSize(spriteFrames);

                if (staticAnimationTimingList.contains(spriteIndex)) {
                    sprite.setAnimationTimingRandom(false);
                }
                
                // The first in the series will always be in this list.
                animatedSprites.put(spriteIndex, sprite);
                lastUpdated.put(spriteIndex, Long.MAX_VALUE);
            }
        }
        
    }
    
    private void initBuilderMap() {
        List<Integer> list = new ArrayList<Integer>();
        int index = 0;
        for (int count = 0; count < sprites.size(); count++) {
            Sprite sprite = sprites.get(count);
            list.add(sprite.getIndex());
            if (list.size() > (EditorCanvas.SPRITE_INTERFACE_COLUMNS - 1)) {
                builderMap.put(index, list);
                list = new ArrayList<Integer>();
                index++;
                
            }
            if (sprite.getSize() > 0) {
                count += (sprite.getSize() - 1);                
            }
        }
        
        for (Map.Entry<Integer, List<Integer>> entry : builderMap.entrySet()) {
            List<Integer> spriteList = entry.getValue();
            for (Integer spriteIndex : spriteList) {
                displayableSpriteList.add(spriteIndex);
            }
        }        
    }
    
    protected void displayBuilderMap() {
        for (Map.Entry<Integer, List<Integer>> entry : builderMap.entrySet()) {
            Integer key = entry.getKey();
            List<Integer> value = entry.getValue();
            log.info(key + "=" + value);
        }        
    }
}
