package org.tdod.dod.canvas;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public interface GameCanvas extends KeyListener, MouseListener {

    public void update();

    public void throttledUpdate();

    public void initialize();
    
    public void addListeners();
    
    public void removeListeners();
    
    public GameCanvas getNextCanvas();

}
