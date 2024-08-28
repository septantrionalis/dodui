package org.tdod.dod.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String SOUND_DIR = "sounds/";
    
    private int bufferSize = 3;
    
    private int walkIndex = 0;
    private List<Clip> walkBuffer = new ArrayList<Clip>(bufferSize);
    
    private int blockedIndex = 0;
    private List<Clip> blockedBuffer = new ArrayList<Clip>(bufferSize);

    private int selectIndex1 = 0;
    private List<Clip> selectBuffer1 = new ArrayList<Clip>(bufferSize);

    private int selectIndex2 = 0;
    private List<Clip> selectBuffer2 = new ArrayList<Clip>(bufferSize);

    public Sound() {
        log.info("Reading sounds from " + SOUND_DIR);
        try {
            for (int index = 0; index < bufferSize; index++) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(SOUND_DIR + "footstep2.wav"));
                Clip walk = AudioSystem.getClip();
                walk.open(audioIn);
                walkBuffer.add(walk);
            }

            for (int index = 0; index < bufferSize; index++) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(SOUND_DIR + "blocked.wav"));
                Clip blocked = AudioSystem.getClip();
                blocked.open(audioIn);
                blockedBuffer.add(blocked);
            }
            
            for (int index = 0; index < bufferSize; index++) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(SOUND_DIR + "menuSelect4.wav"));
                Clip select = AudioSystem.getClip();
                select.open(audioIn);
                selectBuffer1.add(select);
            }

            for (int index = 0; index < bufferSize; index++) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(SOUND_DIR + "menuSelect1.wav"));
                Clip select = AudioSystem.getClip();
                select.open(audioIn);
                selectBuffer2.add(select);
            }

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }
    
    public void playWalk() {
        Clip walk = walkBuffer.get(walkIndex);
        
        walk.flush();
        walk.stop();
        walk.setFramePosition(0);
        walk.start();
        
        walkIndex++;
        if (walkIndex >= bufferSize) {
            walkIndex = 0;
        }
    }
    
    public void playBlocked() {
        Clip blocked = blockedBuffer.get(blockedIndex);

        blocked.stop();
        blocked.setFramePosition(0);
        blocked.start();
        
        blockedIndex++;
        if (blockedIndex >= bufferSize) {
            blockedIndex = 0;
        }
    }
    
    public void playSelect1() {
        Clip select = selectBuffer1.get(selectIndex1);

        select.stop();
        select.setFramePosition(0);
        select.start();
        
        selectIndex1++;
        if (selectIndex1 >= bufferSize) {
            selectIndex1 = 0;
        }
    }

    public void playSelect2() {
        Clip select = selectBuffer2.get(selectIndex2);

        select.stop();
        select.setFramePosition(0);
        select.start();
        
        selectIndex2++;
        if (selectIndex2 >= bufferSize) {
            selectIndex2 = 0;
        }
    }

}
