package org.tdod.dod;

import java.io.FileInputStream;
import java.util.logging.LogManager;

public class Genesis {

    private GameFrame frame;
    
    public Genesis() {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("log.properties"));
        } catch (Exception e1) {}
        
        frame = new GameFrame(false);
        Thread thread = new Thread(frame);
        thread.start();
    }

    public static void main(String[] args) {
        new Genesis();
    }

}
