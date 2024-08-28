package org.tdod.dod;

public class Editor {

    private GameFrame gameFrame;
    
    public Editor() {
        gameFrame = new GameFrame(true);
        Thread thread = new Thread(gameFrame);
        thread.start();
    }

    public static void main(String[] args) {
        new Editor();
    }

}
