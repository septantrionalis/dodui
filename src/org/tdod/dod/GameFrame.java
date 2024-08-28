package org.tdod.dod;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.JFrame;

import org.tdod.dod.canvas.GameCanvas;
import org.tdod.dod.canvas.impl.IntroCanvas;
import org.tdod.dod.canvas.impl.MapSelectionCanvas;
import org.tdod.dod.player.Player;
import org.tdod.dod.player.impl.AdminPlayerImpl;
import org.tdod.dod.player.impl.PlayerImpl;
import org.tdod.dod.utils.Constants;

import com.apple.eawt.FullScreenUtilities;

public class GameFrame extends JFrame implements Runnable {
    
    private final static Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final long serialVersionUID = 5142157066547547629L;

    GameContext context = new GameContext();
    
    private boolean isEditor = false;
    
    public GameFrame(boolean isEditor) {
        // On my mac, everytime I put the game in full screen, the window moves up.  Odd.
        FullScreenUtilities.setWindowCanFullScreen(this,true);
        
        this.isEditor = isEditor;
        
        initialize();
    }
 
    @Override
    public void run() {
        long lastSecond = 0;
        while(true) {
            GameCanvas nextCanvas = context.getCurrentCanvas().getNextCanvas();
            if (null == nextCanvas) {
                long second = System.currentTimeMillis() / 1000;
                if (second != lastSecond) {
                    lastSecond = second;
                    context.getCurrentCanvas().throttledUpdate();
                }
                context.getCurrentCanvas().update();                
            } else {
                log.info("Switching to " + nextCanvas.getClass().getName());

                GameCanvas oldCanvas = context.getCurrentCanvas();
                oldCanvas.removeListeners();
                this.remove((Canvas)oldCanvas);
                this.add((Canvas)nextCanvas);
                
                context.setNewCanvas(nextCanvas);
                
                nextCanvas.addListeners();
                nextCanvas.initialize();
                
                setVisible(true);
            }
            
            // A small sleep timer so we dont peg the CPU.
            try {
                Thread.sleep(50);                
            } catch (Exception e) {}
        }        
    }

    private void initialize() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("data/pixelmix.ttf")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isEditor) {
            Player player = new AdminPlayerImpl();
            context.setPlayer(player);
            GameCanvas canvas = new MapSelectionCanvas(context);
            context.setNewCanvas(canvas);
        } else {
            Player player = new PlayerImpl();
            context.setPlayer(player);
            GameCanvas canvas = new IntroCanvas(context);
            context.setNewCanvas(canvas);
        }
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setBounds(0, 0, Constants.X_RESOLUTION, Constants.Y_RESOLUTION );
        setSize(Constants.X_RESOLUTION, Constants.Y_RESOLUTION + 20);
        setLocationRelativeTo(null);
        add((Canvas)context.getCurrentCanvas());
        setResizable(false);
        setVisible(true);
        context.getCurrentCanvas().addListeners();
        context.getCurrentCanvas().initialize();
    }
}
