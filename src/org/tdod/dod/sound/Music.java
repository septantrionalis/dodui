package org.tdod.dod.sound;

import java.io.File;
import java.util.logging.Logger;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {

    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private MediaPlayer mediaPlayer;
    
    final JFXPanel fxPanel = new JFXPanel();

    public Music() {
    }

    public void play(SongEnum song) {
        log.info("Starting song for " + song);
        if (mediaPlayer != null) {
            mediaPlayer.stop();            
        }
        String bip = song.getFilename();
        Media hit = new Media(new File(bip).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);

        mediaPlayer.play();
    }

    public void stop() {
        if (null != mediaPlayer) {
            mediaPlayer.stop();
        }
    }

}
