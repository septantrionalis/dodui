package org.tdod.dod;

import org.tdod.dod.canvas.GameCanvas;
import org.tdod.dod.player.Player;
import org.tdod.dod.utils.GameLibraries;

public class GameContext {

    private Player player;
    private boolean playMusic = true;
    private GameCanvas currentCanvas;
    private GameLibraries gameLibraries;
    
    public GameContext() {
        gameLibraries = new GameLibraries();
    }

    public GameCanvas getCurrentCanvas() {
        return currentCanvas;
    }

    public void setNewCanvas(GameCanvas currentCanvas) {
        this.currentCanvas = currentCanvas;
    }

    public GameLibraries getGameLibraries() {
        return gameLibraries;
    }

    public boolean isPlayMusic() {
        return playMusic;
    }

    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
    
}
