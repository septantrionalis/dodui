package org.tdod.dod.sound;

public enum SongEnum {
    INTRO("fall_leaves2.mp3"), 
    EXPLORE1("outcast.mp3"), 
    EXPLORE2("outcast.mp3"),
    CREDITS("i_hear_you_crying.mp3");

    private static final String MUSIC_DIR = "music/";

    private String filename;
    
    private SongEnum(String filename) {
        this.filename = MUSIC_DIR + filename;
    }
    
    public String getFilename() {
        return filename;
    }
}
