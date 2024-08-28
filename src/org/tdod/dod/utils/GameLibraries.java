package org.tdod.dod.utils;

import org.tdod.dod.sound.Music;
import org.tdod.dod.sound.SongEnum;
import org.tdod.dod.sound.Sound;
import org.tdod.dod.sprite.SpriteSheet;
import org.tdod.dod.sprite.impl.SpriteSheetImpl;

public class GameLibraries {
    
    private Sound sounds = new Sound();
    private Music music = new Music();
    private SpriteSheet sheet = new SpriteSheetImpl();

    public GameLibraries() {
        sheet.initialize();
    }

    public void startMusic(SongEnum song) {
        music.play(song);
    }

    public void stopMusic() {
        music.stop();
    }

    public Sound getSounds() {
        return sounds;
    }
    
    public SpriteSheet getSpriteSheet() {
        return sheet;
    }
}
